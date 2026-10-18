"""Run with: python3 -m unittest discover -s content/grammar-arena/tests -v"""
import csv
import importlib.util
import tempfile
import unittest
from pathlib import Path

VALIDATOR = Path(__file__).resolve().parents[1] / "tools" / "validate_questions.py"
spec = importlib.util.spec_from_file_location("grammar_validator", VALIDATOR)
validator = importlib.util.module_from_spec(spec)
spec.loader.exec_module(validator)

BASE = dict.fromkeys(validator.REQUIRED, "value")
BASE.update(id="Q-001", version="1", status="draft", topic_id="GRAMMAR-01",
            subtopic_id="adverbs", level="Easy", age_segment="general",
            learning_outcome="Recognize manner adverbs", format="multiple_choice",
            prompt="Which word modifies a verb in this sentence?",
            option_a="brightly", option_b="lamp", option_c="quiet",
            correct_key="A", explanation="Brightly describes how an action happens in this example.",
            distractor_rationale="Noun and adjective are plausible distractors.",
            locale="en", source_or_author="test fixture", rights_status="original_text_unreviewed",
            estimated_seconds="30", accessibility_flags="text_only", tags="adverb",
            duplicate_hash="hash001")

class ValidationTests(unittest.TestCase):
    def run_rows(self, rows):
        with tempfile.TemporaryDirectory() as directory:
            path = Path(directory) / "questions.csv"
            with path.open("w", encoding="utf-8", newline="") as file:
                writer = csv.DictWriter(file, fieldnames=validator.REQUIRED)
                writer.writeheader()
                writer.writerows(rows)
            return validator.validate(path)

    def test_valid_draft(self):
        failures, summary = self.run_rows([BASE])
        self.assertFalse(failures, failures)
        self.assertEqual(summary["rows"], 1)

    def test_reject_duplicate_id(self):
        other = dict(BASE, duplicate_hash="hash002", prompt="Different prompt")
        failures, _ = self.run_rows([BASE, other])
        self.assertTrue(any(f["field"] == "id" for f in failures))

    def test_reject_normalized_duplicate_prompt(self):
        other = dict(BASE, id="Q-002", duplicate_hash="hash002",
                     prompt=" WHICH  word   modifies a verb in this sentence? ")
        failures, _ = self.run_rows([BASE, other])
        self.assertTrue(any(f["field"] == "prompt" for f in failures))

    def test_reject_duplicate_options(self):
        failures, _ = self.run_rows([dict(BASE, option_b="BRIGHTLY")])
        self.assertTrue(any(f["field"] == "options" for f in failures))

    def test_unapproved_cannot_be_published(self):
        failures, _ = self.run_rows([dict(BASE, status="published")])
        fields = {f["field"] for f in failures}
        self.assertIn("reviewer_id", fields)
        self.assertIn("rights_status", fields)

if __name__ == "__main__":
    unittest.main()
