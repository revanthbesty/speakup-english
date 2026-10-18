#!/usr/bin/env python3
"""Independent, read-only editorial risk audit over one or more English Mint packs.

Returns 1 when structural errors exist, or template concentration crosses the review
threshold; it never marks linguistic correctness as approved.
"""
import argparse
import json
import re
import sys
from collections import Counter, defaultdict
from pathlib import Path

def normalize(text):
    text = str(text).casefold()
    text = re.sub(r"\\b\\d+\\b", "#", text)
    return re.sub(r"\\s+", " ", text).strip()

def audit(paths):
    issues = []
    records = []
    for path in paths:
        try:
            data = json.loads(Path(path).read_text(encoding="utf-8"))
            rows = data["activities"] if isinstance(data, dict) else data
            if not isinstance(rows, list):
                raise ValueError("activities must be an array")
            records.extend(rows)
        except (OSError, ValueError, KeyError, TypeError) as exc:
            issues.append(f"{path}: invalid pack: {exc}")
    ids = Counter(str(x.get("id", "")) for x in records if isinstance(x, dict))
    duplicate_ids = [k for k, v in ids.items() if v > 1]
    issues.extend(f"duplicate ID: {k}" for k in duplicate_ids)
    grouped = defaultdict(list)
    for x in records:
        if not isinstance(x, dict):
            issues.append("non-object activity")
            continue
        key = (x.get("category"), normalize(x.get("content")), normalize(x.get("prompt")))
        grouped[key].append(x.get("id"))
    for ids_group in grouped.values():
        if len(ids_group) > 1:
            issues.append(f"duplicate content/prompt: {ids_group[:4]}")
    by_category = defaultdict(list)
    for x in records:
        if isinstance(x, dict):
            by_category[x.get("category")].append(x)
    warnings = []
    for category, rows in sorted(by_category.items(), key=lambda t: str(t[0])):
        signatures = Counter()
        for x in rows:
            # Lead text highlights repeated template structures; it is a heuristic.
            text = normalize(x.get("content", ""))
            signatures[re.sub(r"[^a-z# ]", "", text)[:38]] += 1
            if x.get("review_status") != "approved":
                pass
        leader, leader_count = signatures.most_common(1)[0]
        if leader_count >= 6 and leader_count / len(rows) >= .2:
            warnings.append(f"{category}: repeated opening in {leader_count}/{len(rows)} records: {leader!r}")
    approved = sum(x.get("review_status") == "approved" for x in records if isinstance(x, dict))
    print(f"Records: {len(records)}; independently approved: {approved}; structural issues: {len(issues)}; template warnings: {len(warnings)}")
    for message in issues[:100]:
        print("ERROR:", message)
    for message in warnings[:100]:
        print("EDITORIAL REVIEW:", message)
    return bool(issues), bool(warnings)

def main(argv=None):
    parser = argparse.ArgumentParser()
    parser.add_argument("packs", nargs="+")
    parser.add_argument("--strict-templates", action="store_true")
    args = parser.parse_args(argv)
    errors, warnings = audit(args.packs)
    return 1 if errors or (warnings and args.strict_templates) else 0

if __name__ == "__main__":
    sys.exit(main())
