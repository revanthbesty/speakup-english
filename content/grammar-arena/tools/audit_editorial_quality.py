#!/usr/bin/env python3
"""Offline quality triage; generates a review queue, never approves questions."""
import argparse
import csv
import json
from collections import Counter
from pathlib import Path

GENERIC = {"not applicable here", "none of these", "all of the above",
           "none of the above", "n/a"}

def audit(rows):
    findings = []
    for row in rows:
        for option in "abc":
            value = (row.get("option_" + option) or "").strip()
            if value.casefold() in GENERIC:
                findings.append({
                    "id": row.get("id", ""), "topic_id": row.get("topic_id", ""),
                    "field": "option_" + option, "severity": "blocker",
                    "rule": "generic_distractor", "detail": value,
                    "suggested_action": "Replace with plausible incorrect alternative; independently check unique correct answer."
                })
    return findings

def main():
    parser = argparse.ArgumentParser()
    parser.add_argument("csv", type=Path)
    parser.add_argument("--queue", type=Path, required=True)
    parser.add_argument("--report", type=Path, required=True)
    args = parser.parse_args()
    with args.csv.open(encoding="utf-8-sig", newline="") as stream:
        rows = list(csv.DictReader(stream))
    findings = audit(rows)
    fields = ["id", "topic_id", "field", "severity", "rule", "detail", "suggested_action"]
    with args.queue.open("w", encoding="utf-8", newline="") as stream:
        writer = csv.DictWriter(stream, fieldnames=fields)
        writer.writeheader()
        writer.writerows(findings)
    report = {"questions_scanned": len(rows),
              "questions_flagged": len({f["id"] for f in findings}),
              "findings": len(findings),
              "rules": dict(Counter(f["rule"] for f in findings)),
              "status": "review_required"}
    args.report.write_text(json.dumps(report, indent=2) + "\n", encoding="utf-8")
    print(json.dumps(report, indent=2))

if __name__ == "__main__":
    main()
