"""Safety regression tests for English Mint replace-by-ID preview overlay."""
import importlib.util
import json
import tempfile
import unittest
import zipfile
from pathlib import Path

spec = importlib.util.spec_from_file_location("mint_overlay", Path(__file__).with_name("overlay_english_mint_repair_preview.py"))
mint = importlib.util.module_from_spec(spec)
spec.loader.exec_module(mint)

class OverlaySafetyTests(unittest.TestCase):
    def test_rejects_unrecognized_replacement_id(self):
        with self.assertRaisesRegex(ValueError, "does not exist"):
            mint.validate_replacement({"activity_id": "unknown"}, {})

    def test_rejects_level_mutation(self):
        old = {"A1-001": {"category": "reading", "cefr_level": "A1"}}
        with self.assertRaisesRegex(ValueError, "cefr_level"):
            mint.validate_replacement({"activity_id": "A1-001", "category": "reading", "cefr_level": "C1"}, old)

    def test_rejects_wrong_answer_reference(self):
        record = {"activity_id": "VOC-001", "correct_answer": "B",
                  "option_a": "one", "option_b": "two", "option_c": "three",
                  "accepted_answers_json": ["one"]}
        with self.assertRaisesRegex(ValueError, "disagrees"):
            mint.validate_answer(record)

    def test_accepts_clear_typed_word_answer(self):
        mint.validate_answer({"activity_id": "WB-001", "correct_answer": "raincoat"})

    def test_rejects_empty_reading_passage(self):
        old = {"REA-001": {"category": "reading", "cefr_level": "A1"}}
        rec = {"activity_id": "REA-001", "category": "reading", "cefr_level": "A1",
               "review_status": "needs_human_review", "content": "A note",
               "question": "What happened?", "explanation": "It is in the text.",
               "correct_answer": "B", "option_a": "no", "option_b": "yes",
               "option_c": "maybe", "accepted_answers_json": ["yes"]}
        with self.assertRaisesRegex(ValueError, "reading_passage"):
            mint.validate_replacement(rec, old)

if __name__ == "__main__":
    unittest.main()
