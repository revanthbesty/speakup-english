#!/usr/bin/env python3
"""Offline, fail-closed export adapter. NEVER writes to a database."""
import argparse
import csv
import json
from pathlib import Path

from validate_questions import validate

APPROVED = {"approved"}
def export_payload(source):
    errors, summary = validate(source)
    with Path(source).open(encoding="utf-8-sig", newline="") as handle:
        rows = list(csv.DictReader(handle))
    for number, row in enumerate(rows, 2):
        if row.get("status") not in APPROVED:
            errors.append({"row": number, "id": row.get("id"), "field": "status",
                           "rule": "non-approved question cannot enter a publishable export"})
        for field in ("reviewer_id", "approved_at", "last_reviewed_at"):
            if not (row.get(field) or "").strip():
                errors.append({"row": number, "id": row.get("id"), "field": field,
                               "rule": "independent approval metadata required"})
        if row.get("rights_status") in ("", "original_text_unreviewed"):
            errors.append({"row": number, "id": row.get("id"), "field": "rights_status",
                           "rule": "rights not reviewed"})
    if errors:
        raise ValueError(json.dumps({"rejected": True, "errors": errors[:50],
                                      "total_errors": len(errors)}, indent=2))
    return {"schema_version": 1, "publication_status": "approved_for_staging_only",
            "count": len(rows),
            "questions": [{
                "source_id": row["id"], "source_version": int(row["version"]),
                "topic_id": row["topic_id"], "subtopic_id": row["subtopic_id"],
                "difficulty": row["level"], "format": row["format"],
                "prompt": row["prompt"],
                "options": [{"key": key, "text": row["option_" + key.lower()]} for key in "ABC"],
                "correct_key": row["correct_key"], "explanation": row["explanation"],
                "learning_outcome": row["learning_outcome"]
            } for row in rows]}

def main():
    parser = argparse.ArgumentParser()
    parser.add_argument("csv", type=Path)
    parser.add_argument("--output", type=Path, required=True)
    args = parser.parse_args()
    # Do not open output until every question has passed the release gate.
    try:
        payload = export_payload(args.csv)
    except ValueError as error:
        parser.exit(1, f"EXPORT BLOCKED: {error}\n")
    args.output.write_text(json.dumps(payload, ensure_ascii=False, indent=2) + "\n", encoding="utf-8")
    print(f"Exported {payload['count']} approved questions to staging-only JSON")

if __name__ == "__main__":
    main()
