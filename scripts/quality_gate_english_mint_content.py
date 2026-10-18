#!/usr/bin/env python3
"""Conservative editorial readiness audit. Never approves content automatically."""
import argparse
import json
from collections import Counter
from pathlib import Path

LEVELS = {"A1": (5, 12), "A2": (8, 20), "B1": (12, 35),
          "B2": (18, 55), "C1": (24, 85)}
REQUIRED_MEDIA = {"reading": "passage", "listening": "transcript",
                  "stories": "story", "dialogues": "turns"}

def words(value):
    return len(str(value).split())

def examine(rows):
    issues = []
    needs_review = 0
    shapes = Counter()
    for item in rows:
        ident = item.get("id", "<missing-id>")
        category = item.get("category")
        level = item.get("cefr")
        if item.get("review_status") != "approved":
            needs_review += 1
        options = item.get("options")
        if options is not None:
            if not isinstance(options, list) or len(options) < 3:
                issues.append(f"{ident}: fewer than three choice options")
            if isinstance(options, list) and len(set(options)) != len(options):
                issues.append(f"{ident}: duplicate answer options")
        if category in REQUIRED_MEDIA:
            media = item.get(REQUIRED_MEDIA[category], "")
            if category == "dialogues":
                if not isinstance(media, list) or len(media) < 2:
                    issues.append(f"{ident}: fewer than two dialogue turns")
            elif not isinstance(media, str) or not media.strip():
                issues.append(f"{ident}: missing {REQUIRED_MEDIA[category]}")
            elif level in LEVELS:
                minimum = LEVELS[level][0 if category == "listening" else 1]
                if words(media) < minimum:
                    issues.append(f"{ident}: {category} text too short for {level}: {words(media)} < {minimum}")
        if category == "word_builder":
            answer = item.get("correct_answers", [""])[0]
            if isinstance(answer, str) and answer.casefold() in str(item.get("content", "")).casefold():
                issues.append(f"{ident}: answer exposed in prompt content")
        if category in ("situations", "reading", "stories", "dialogues", "listening"):
            # Detect predictable repeated activity structures within a CEFR level.
            objective = str(item.get("objective", "")).strip().casefold()
            shapes[(category, level, objective)] += 1
    for (category, level, objective), count in sorted(shapes.items()):
        if count >= 8:
            issues.append(f"{category}/{level}: objective repeated {count} times ({objective[:70]})")
    return issues, needs_review

def main():
    parser = argparse.ArgumentParser()
    parser.add_argument("packs", nargs="+")
    parser.add_argument("--strict", action="store_true",
                        help="Fail when editorial risks occur (recommended for CI)")
    args = parser.parse_args()
    rows = []
    for file in args.packs:
        data = json.loads(Path(file).read_text(encoding="utf-8"))
        rows.extend(data["activities"] if isinstance(data, dict) else data)
    issues, pending = examine(rows)
    print(f"Activities: {len(rows)}; awaiting approval: {pending}; editorial flags: {len(issues)}")
    for issue in issues[:120]:
        print("REVIEW:", issue)
    if len(issues) > 120:
        print(f"... {len(issues)-120} more flags")
    return 1 if args.strict and (issues or pending) else 0

if __name__ == "__main__":
    raise SystemExit(main())
