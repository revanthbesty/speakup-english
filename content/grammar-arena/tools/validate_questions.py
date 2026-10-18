#!/usr/bin/env python3
"""Offline, standard-library-only Grammar Arena CSV draft validator."""
import argparse
import csv
import json
import re
from collections import Counter
from pathlib import Path

REQUIRED = ("id", "version", "status", "topic_id", "subtopic_id", "level",
            "age_segment", "learning_outcome", "format", "prompt",
            "option_a", "option_b", "option_c", "correct_key",
            "explanation", "distractor_rationale", "locale",
            "source_or_author", "rights_status", "estimated_seconds",
            "accessibility_flags", "tags", "duplicate_hash")
ALLOWED_LEVELS = {"Easy", "Medium", "Hard"}
ALLOWED_STATUSES = {"draft", "validated", "in_review", "approved", "published", "rejected", "retired"}

def normalize(value):
    return re.sub(r"\s+", " ", str(value or "").strip().casefold())

def validate(path):
    failures = []
    with Path(path).open(encoding="utf-8-sig", newline="") as handle:
        reader = csv.DictReader(handle)
        if not reader.fieldnames:
            return [{"row": 1, "id": "", "field": "header", "rule": "missing columns"}], {}
        for field in REQUIRED:
            if field not in reader.fieldnames:
                failures.append({"row": 1, "id": "", "field": field, "rule": "missing column"})
        seen_ids, seen_prompts, seen_hashes = set(), set(), set()
        counts = Counter()
        for line, row in enumerate(reader, 2):
            identifier = row.get("id") or ""
            def error(field, rule):
                failures.append({"row": line, "id": identifier, "field": field, "rule": rule})
            for field in REQUIRED:
                if not normalize(row.get(field)):
                    error(field, "blank required value")
            for field, seen in (("id", seen_ids), ("duplicate_hash", seen_hashes)):
                key = normalize(row.get(field))
                if key and key in seen:
                    error(field, "duplicate")
                seen.add(key)
            prompt = normalize(row.get("prompt"))
            if prompt in seen_prompts:
                error("prompt", "duplicate normalized prompt")
            seen_prompts.add(prompt)
            options = [re.sub(r"\s+", " ", (row.get("option_" + option) or "").strip()) for option in "abc"]
            # Letter case is meaningful in capitalization assessment items.
            compare = options if row.get("topic_id") == "GRAMMAR-22" else [x.casefold() for x in options]
            if len(set(compare)) != 3 or any(not x for x in options):
                error("options", "must have three distinct nonblank options")
            if row.get("correct_key") not in ("A", "B", "C"):
                error("correct_key", "must be A, B or C")
            if row.get("level") not in ALLOWED_LEVELS:
                error("level", "invalid difficulty")
            if row.get("status") not in ALLOWED_STATUSES:
                error("status", "invalid status")
            if len(normalize(row.get("explanation")).split()) < 6:
                error("explanation", "too short for meaningful feedback")
            if row.get("status") in ("approved", "published"):
                for field in ("reviewer_id", "approved_at", "last_reviewed_at"):
                    if not normalize(row.get(field)):
                        error(field, "missing independent review evidence")
                if row.get("rights_status") == "original_text_unreviewed":
                    error("rights_status", "unreviewed rights cannot be published")
            if normalize(row.get("media_uri")):
                if not normalize(row.get("alt_text")) and not normalize(row.get("transcript")):
                    error("media_uri", "missing accessibility alternative")
            counts[(row.get("topic_id"), row.get("level"), row.get("status"))] += 1
    return failures, {"rows": sum(counts.values()), "by_topic_level_status": [
        {"topic": t, "level": l, "status": s, "count": n}
        for (t, l, s), n in sorted(counts.items())]}

def main():
    parser = argparse.ArgumentParser()
    parser.add_argument("csv")
    parser.add_argument("--report", help="write JSON report")
    args = parser.parse_args()
    failures, summary = validate(args.csv)
    report = {"summary": summary, "structural_failures": failures,
              "warning": "Automated checks cannot establish grammar correctness, unambiguous answers, semantic uniqueness or independent approval."}
    if args.report:
        Path(args.report).write_text(json.dumps(report, indent=2) + "\n", encoding="utf-8")
    print(json.dumps(report, indent=2))
    raise SystemExit(1 if failures else 0)

if __name__ == "__main__":
    main()
