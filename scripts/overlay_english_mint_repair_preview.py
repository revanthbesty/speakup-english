#!/usr/bin/env python3
"""Safely overlay reviewed replacement records on external English Mint JSON packs.

Does not import into Android. Default behaviour is read-only. Fails if source IDs,
categories, levels or record counts do not match the existing bank.
"""
import argparse
import json
import zipfile
from collections import Counter
from pathlib import Path

CATEGORIES = ("word_builder", "riddles", "vocabulary", "situations",
              "listening", "reading", "dialogues", "stories")
TARGET_LEVELS = {"A1": 100, "A2": 125, "B1": 125, "B2": 100, "C1": 50}
NEEDS_TEXT = {"reading": "reading_passage", "listening": "audio_transcript",
              "stories": "story_text", "riddles": "riddle_text"}
NEEDS_TURNS = "dialogue_turns_json"

def parse_rows(text, label):
    data = json.loads(text)
    rows = data.get("activities") if isinstance(data, dict) else data
    if not isinstance(rows, list) or any(not isinstance(x, dict) for x in rows):
        raise ValueError(f"{label}: activities must be an array of objects")
    return rows

def validate_answer(item):
    key = item.get("correct_answer")
    if key in ("A", "B", "C", "D"):
        opts = [item.get(f"option_{c}") for c in "abcd"]
        if any(not isinstance(o, str) or not o.strip() for o in opts[:3]):
            raise ValueError(f"{item['activity_id']}: missing multiple choice options")
        if len(set(o.strip().casefold() for o in opts if isinstance(o, str) and o.strip())) != sum(bool(o and str(o).strip()) for o in opts):
            raise ValueError(f"{item['activity_id']}: duplicate multiple choice options")
        actual = item.get("option_" + key.lower())
        if not isinstance(actual, str) or not actual.strip():
            raise ValueError(f"{item['activity_id']}: correct answer option is blank")
        accepted = item.get("accepted_answers_json", [])
        if isinstance(accepted, list) and accepted and actual not in accepted:
            raise ValueError(f"{item['activity_id']}: option answer disagrees with accepted answers")
    elif not isinstance(key, str) or not key.strip():
        raise ValueError(f"{item['activity_id']}: missing typed correct answer")

def validate_replacement(item, old):
    ident = item.get("activity_id")
    if not ident or ident not in old:
        raise ValueError(f"Replacement ID does not exist in source: {ident}")
    before = old[ident]
    for field in ("category", "cefr_level"):
        if item.get(field) != before.get(field):
            raise ValueError(f"{ident}: unexpected {field} change")
    if item.get("review_status") != "needs_human_review":
        raise ValueError(f"{ident}: explicit review status required")
    if item.get("source_type") != "original_ai_draft_rewrite":
        raise ValueError(f"{ident}: repair source provenance required")
    if not item.get("content") or not item.get("question") or not item.get("explanation"):
        raise ValueError(f"{ident}: incomplete core content")
    validate_answer(item)
    cat = item["category"]
    field = NEEDS_TEXT.get(cat)
    if field and not str(item.get(field, "")).strip():
        raise ValueError(f"{ident}: {field} missing")
    if cat == "word_builder" and not str(item.get("target_word", "")).strip():
        raise ValueError(f"{ident}: target_word missing")
    if cat == "vocabulary" and not str(item.get("definition", "")).strip():
        raise ValueError(f"{ident}: vocabulary definition missing")
    if cat == "situations" and not str(item.get("situation_context", "")).strip():
        raise ValueError(f"{ident}: situation context missing")
    if cat == "dialogues":
        turns = item.get(NEEDS_TURNS)
        if not isinstance(turns, list) or len(turns) < 2 or any(not isinstance(t, dict) or not t.get("speaker") or not t.get("text") for t in turns):
            raise ValueError(f"{ident}: invalid dialogue turns")

def overlay(zip_path, patch_path):
    existing, wrapper = {}, {}
    with zipfile.ZipFile(zip_path) as z:
        for cat in CATEGORIES:
            key = cat + ".json"
            rows = parse_rows(z.read(key).decode("utf-8"), key)
            if len(rows) != 500 or any(x.get("category") != cat for x in rows):
                raise ValueError(f"{key}: incorrect category/count")
            level_count = Counter(x.get("cefr_level") for x in rows)
            if level_count != TARGET_LEVELS:
                raise ValueError(f"{key}: incorrect CEFR distribution {level_count}")
            existing[cat] = rows
            raw = json.loads(z.read(key).decode("utf-8"))
            wrapper[cat] = raw if isinstance(raw, dict) else None
    old = {r["activity_id"]: r for rows in existing.values() for r in rows}
    if len(old) != 4000:
        raise ValueError("Source bank must contain 4000 unique activity IDs")
    patches = parse_rows(Path(patch_path).read_text(encoding="utf-8"), str(patch_path))
    if not patches:
        raise ValueError("No replacement activities provided")
    seen = set()
    for x in patches:
        if x.get("review_status") != "needs_human_review":
            raise ValueError(f"{x.get('activity_id')}: auto-approved replacement not permitted")
        ident = x.get("activity_id")
        if ident in seen:
            raise ValueError(f"Duplicate replacement ID: {ident}")
        seen.add(ident)
        validate_replacement(x, old)
    patched = {x["activity_id"]: x for x in patches}
    for cat, rows in existing.items():
        existing[cat] = [patched.get(x["activity_id"], x) for x in rows]
    return existing, wrapper, len(patches)

def main():
    ap = argparse.ArgumentParser()
    ap.add_argument("source_zip")
    ap.add_argument("replacement_json")
    ap.add_argument("--out", help="Explicitly export an UNREVIEWED preview ZIP; never app-import automatically")
    args = ap.parse_args()
    packs, wrappers, changed = overlay(args.source_zip, args.replacement_json)
    print(f"PASS: {changed} existing IDs matched; counts stay 4000; preview only; NOT RELEASE APPROVED")
    if args.out:
        dest = Path(args.out)
        if dest.resolve() == Path(args.source_zip).resolve():
            raise ValueError("Never overwrite the source ZIP")
        with zipfile.ZipFile(dest, "w", zipfile.ZIP_DEFLATED) as z:
            for cat, rows in packs.items():
                data = wrappers[cat]
                if data is None:
                    data = rows
                else:
                    data = dict(data, activities=rows)
                z.writestr(cat + ".json", json.dumps(data, ensure_ascii=False, indent=2))
        print(f"Preview written: {dest} (do not publish as approved content)")

if __name__ == "__main__":
    main()
