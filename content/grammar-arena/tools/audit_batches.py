#!/usr/bin/env python3
"""Read-only CSV cross-batch question duplicate audit."""
import argparse
import csv
import json
import re
from collections import Counter
from difflib import SequenceMatcher
from pathlib import Path

def norm(text):
    return re.sub(r"\s+", " ", (text or "").strip()).casefold()

def audit(paths, threshold=0.90):
    rows, issues = [], []
    for path in paths:
        with open(path, encoding="utf-8-sig", newline="") as stream:
            for line, record in enumerate(csv.DictReader(stream), 2):
                rows.append((str(path), line, record))
    for index, (path, line, record) in enumerate(rows):
        for old_path, old_line, previous in rows[:index]:
            reasons = []
            for field, reason in (("id", "duplicate_id"), ("duplicate_hash", "duplicate_hash")):
                if norm(record.get(field)) and norm(record.get(field)) == norm(previous.get(field)):
                    reasons.append(reason)
            current, earlier = norm(record.get("prompt")), norm(previous.get("prompt"))
            if current and current == earlier:
                reasons.append("exact_normalized_prompt")
            elif current and earlier and SequenceMatcher(None, current, earlier).ratio() >= threshold:
                reasons.append("near_duplicate_prompt_review")
            if reasons:
                issues.append({"file": path, "row": line, "id": record.get("id"),
                               "other_file": old_path, "other_row": old_line,
                               "other_id": previous.get("id"), "reasons": reasons})
    return {"total_records": len(rows), "issues": issues,
            "issue_counts": dict(Counter(reason for issue in issues for reason in issue["reasons"])),
            "warning": "Similarity flags require editorial review; this is not semantic verification."}

def main():
    parser = argparse.ArgumentParser()
    parser.add_argument("csv", nargs="+", type=Path)
    parser.add_argument("--threshold", type=float, default=0.90)
    parser.add_argument("--report", type=Path)
    args = parser.parse_args()
    if not 0 < args.threshold <= 1:
        parser.error("threshold must be in (0,1]")
    report = audit(args.csv, args.threshold)
    data = json.dumps(report, ensure_ascii=False, indent=2)
    if args.report:
        args.report.write_text(data + "\n", encoding="utf-8")
    print(data)
    raise SystemExit(bool(report["issues"]))

if __name__ == "__main__":
    main()
