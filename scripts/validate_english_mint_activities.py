#!/usr/bin/env python3
"""Offline-first English Mint activity-pack structural validation (stdlib only).

Usage:
  python3 scripts/validate_english_mint_activities.py path/to/activities.json
Activity pack format: JSON array or {"activities": [ ... ]}.
Structural validation is necessary, not sufficient, for linguistic approval.
"""
import json
import re
import sys
from collections import Counter
from pathlib import Path

TARGETS = {"word_builder", "riddles", "vocabulary", "situations", "listening", "reading", "dialogues", "stories"}
LEVELS = {"A1": 100, "A2": 125, "B1": 125, "B2": 100, "C1": 50}
REQUIRED = {"id", "category", "cefr", "title", "objective", "content", "prompt",
            "correct_answers", "explanation", "estimated_seconds", "content_version"}
TEXT_REQUIRED = {"title", "objective", "content", "prompt", "explanation", "content_version"}

def validate(items, require_complete=True):
    errors = []
    if not isinstance(items, list):
        return ["Activity pack must be a JSON array."]
    counts, ids, bodies = Counter(), set(), set()
    for index, item in enumerate(items):
        tag = f"activity[{index}]"
        if not isinstance(item, dict):
            errors.append(f"{tag}: expected object")
            continue
        missing = REQUIRED - item.keys()
        if missing:
            errors.append(f"{tag}: missing {sorted(missing)}")
            continue
        ident, category, cefr = item["id"], item["category"], item["cefr"]
        if not isinstance(ident, str) or not re.fullmatch(r"[a-zA-Z0-9][a-zA-Z0-9_.:-]{3,}", ident):
            errors.append(f"{tag}: invalid stable id")
        elif ident in ids:
            errors.append(f"{tag}: duplicate id {ident}")
        ids.add(ident if isinstance(ident, str) else "")
        if category not in TARGETS:
            errors.append(f"{tag}: invalid category")
        if cefr not in LEVELS:
            errors.append(f"{tag}: invalid CEFR level")
        if category in TARGETS and cefr in LEVELS:
            counts[(category, cefr)] += 1
        for field in TEXT_REQUIRED:
            if not isinstance(item[field], str) or not item[field].strip():
                errors.append(f"{tag}: empty or invalid {field}")
        answers = item["correct_answers"]
        if not isinstance(answers, list) or not answers or any(not isinstance(a, str) or not a.strip() for a in answers):
            errors.append(f"{tag}: correct_answers must contain nonempty strings")
        options = item.get("options")
        if options is not None:
            if not isinstance(options, list) or len(options) != len(set(map(str, options))) or len(options) < 2:
                errors.append(f"{tag}: invalid or duplicate options")
            elif isinstance(answers, list) and any(answer not in options for answer in answers):
                errors.append(f"{tag}: correct answer absent from options")
        seconds = item["estimated_seconds"]
        if not isinstance(seconds, int) or isinstance(seconds, bool) or not 10 <= seconds <= 3600:
            errors.append(f"{tag}: estimated_seconds outside 10..3600")
        if category == "listening" and (not isinstance(item.get("transcript"), str) or not item["transcript"].strip()):
            errors.append(f"{tag}: missing listening transcript")
        if category == "reading" and (not isinstance(item.get("passage"), str) or not item["passage"].strip()):
            errors.append(f"{tag}: missing reading passage")
        if category == "dialogues" and (not isinstance(item.get("turns"), list) or len(item["turns"]) < 2):
            errors.append(f"{tag}: missing dialogue turns")
        if category == "stories" and (not isinstance(item.get("story"), str) or not item["story"].strip()):
            errors.append(f"{tag}: missing story")
        body = (str(category), str(cefr), str(item.get("content", "")).casefold().strip(),
                str(item.get("prompt", "")).casefold().strip())
        if body in bodies:
            errors.append(f"{tag}: exact duplicate category/level/content/prompt")
        bodies.add(body)
    if require_complete:
        if len(items) != 4000:
            errors.append(f"Expected exactly 4000 activities; found {len(items)}")
        for category in sorted(TARGETS):
            for cefr, expected in LEVELS.items():
                actual = counts[(category, cefr)]
                if actual != expected:
                    errors.append(f"{category}/{cefr}: expected {expected}, found {actual}")
    return errors

def main():
    if len(sys.argv) not in (2, 3) or (len(sys.argv) == 3 and sys.argv[2] != "--allow-partial"):
        print("Usage: validate_english_mint_activities.py PACK.json [--allow-partial]", file=sys.stderr)
        return 2
    path = Path(sys.argv[1])
    try:
        data = json.loads(path.read_text(encoding="utf-8"))
    except (OSError, ValueError) as exc:
        print(f"Cannot read JSON: {exc}", file=sys.stderr)
        return 2
    items = data.get("activities") if isinstance(data, dict) else data
    errors = validate(items, require_complete="--allow-partial" not in sys.argv)
    for error in errors[:100]:
        print("FAIL:", error)
    if len(errors) > 100:
        print(f"...and {len(errors) - 100} more errors")
    if errors:
        return 1
    print(f"PASS: {len(items)} structurally valid activity records (not linguistic approval)")
    return 0

if __name__ == "__main__":
    sys.exit(main())
