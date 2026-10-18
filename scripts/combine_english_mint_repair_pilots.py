#!/usr/bin/env python3
"""Validate and combine all English Mint external workbook repair pilots.

Produces a single unapproved patch file; never modifies source content.
"""
import argparse
import json
from collections import Counter
from pathlib import Path

from overlay_english_mint_repair_preview import validate_answer

PATCH_FILES = (
    "external_workbook_repair_pilot_24_import_schema.json",
    "external_workbook_repair_b1_pilot_8.json",
    "external_workbook_repair_a2_b2_c1_pilot_24.json",
    "external_workbook_repair_a2_second_pilot_8.json",
    "external_workbook_repair_b1_second_pilot_8.json",
    "external_workbook_repair_b2_second_pilot_8.json",
)
EXPECTED_COUNTS = {"A1": 24, "A2": 16, "B1": 16, "B2": 16, "C1": 8}
CATEGORIES = {"word_builder", "riddles", "vocabulary", "situations",
              "listening", "reading", "dialogues", "stories"}


def build_repair_patch(base):
    rows = []
    seen = set()
    for filename in PATCH_FILES:
        source = json.loads((base / filename).read_text(encoding="utf-8"))
        current = source["activities"]
        if not isinstance(current, list):
            raise ValueError(f"{filename}: activities must be a list")
        for item in current:
            ident = item["activity_id"]
            if ident in seen:
                raise ValueError(f"Duplicate patch ID: {ident}")
            seen.add(ident)
            if item.get("review_status") != "needs_human_review":
                raise ValueError(f"{ident}: no automatic approval")
            if item.get("source_type") != "original_ai_draft_rewrite":
                raise ValueError(f"{ident}: provenance missing")
            if item.get("category") not in CATEGORIES:
                raise ValueError(f"{ident}: invalid category")
            if item.get("cefr_level") not in EXPECTED_COUNTS:
                raise ValueError(f"{ident}: invalid CEFR")
            if not all(str(item.get(k, "")).strip() for k in ("content", "question", "explanation")):
                raise ValueError(f"{ident}: incomplete educational content")
            validate_answer(item)
            rows.append(item)
    if len(rows) != 80:
        raise ValueError(f"Expected 80 pilots, received {len(rows)}")
    by_level = Counter(x["cefr_level"] for x in rows)
    if by_level != EXPECTED_COUNTS:
        raise ValueError(f"Incorrect level distribution: {by_level}")
    by_category = Counter(x["category"] for x in rows)
    if set(by_category) != CATEGORIES or any(n != 10 for n in by_category.values()):
        raise ValueError(f"Expected 10 drafts per category: {by_category}")
    return {"schema_version": "1.0", "review_status": "needs_human_review",
            "warning": "DRAFT replacement content; no editorial approval or Android import",
            "activities": rows}


def main():
    parser = argparse.ArgumentParser()
    parser.add_argument("--content-dir", type=Path, default=Path("content/english_mint_4000"))
    parser.add_argument("--out", type=Path, help="Write optional unapproved patch JSON")
    args = parser.parse_args()
    document = build_repair_patch(args.content_dir)
    if args.out:
        args.out.write_text(json.dumps(document, indent=2, ensure_ascii=False) + "\n", encoding="utf-8")
    print("PASS: 80 non-approved repair pilots, ten per category, levels 24/16/16/16/8")


if __name__ == "__main__":
    main()
