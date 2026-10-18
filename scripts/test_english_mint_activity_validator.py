"""Lightweight unittest coverage for content-pack release gates."""
import importlib.util
import unittest
from pathlib import Path

path = Path(__file__).with_name("validate_english_mint_activities.py")
spec = importlib.util.spec_from_file_location("activity_validator", path)
module = importlib.util.module_from_spec(spec)
spec.loader.exec_module(module)

def sample():
    return {
        "id": "mint:word:0001", "category": "word_builder", "cefr": "A1",
        "title": "Build a word", "objective": "Spell a common word",
        "content": "A small domesticated animal", "prompt": "Build CAT",
        "correct_answers": ["cat"], "options": ["cat", "bat", "hat"],
        "explanation": "Cat is the animal described.",
        "estimated_seconds": 35, "content_version": "1"
    }

class ActivityPackTests(unittest.TestCase):
    def test_valid_partial_pack(self):
        self.assertEqual([], module.validate([sample()], require_complete=False))

    def test_release_gate_rejects_partial_pack(self):
        errors = module.validate([sample()])
        self.assertTrue(any("4000" in error for error in errors))

    def test_duplicate_ids_rejected(self):
        second = dict(sample(), content="Another description", prompt="Build another word")
        errors = module.validate([sample(), second], require_complete=False)
        self.assertTrue(any("duplicate id" in error for error in errors))

    def test_answer_must_be_among_options(self):
        item = sample()
        item["correct_answers"] = ["dog"]
        self.assertTrue(any("absent from options" in e for e in module.validate([item], False)))

    def test_listening_requires_transcript(self):
        item = dict(sample(), category="listening")
        self.assertTrue(any("transcript" in e for e in module.validate([item], False)))

if __name__ == "__main__":
    unittest.main()
