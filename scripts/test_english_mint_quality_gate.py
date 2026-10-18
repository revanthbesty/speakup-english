"""Tests for conservative editorial readiness checks."""
import importlib.util
import unittest
from pathlib import Path

spec = importlib.util.spec_from_file_location("quality", Path(__file__).with_name("quality_gate_english_mint_content.py"))
quality = importlib.util.module_from_spec(spec)
spec.loader.exec_module(quality)

class QualityTests(unittest.TestCase):
    def test_flags_two_option_quiz(self):
        issues, pending = quality.examine([{"id":"v1","category":"vocabulary","cefr":"A1","options":["yes","no"],"review_status":"needs_human_review"}])
        self.assertTrue(any("three choice" in x for x in issues))
        self.assertEqual(1, pending)

    def test_flags_exposed_word_builder_answer(self):
        issues, _ = quality.examine([{"id":"w1","category":"word_builder","cefr":"A1","correct_answers":["apple"],"content":"Spell apple."}])
        self.assertTrue(any("answer exposed" in x for x in issues))

    def test_flags_short_advanced_story(self):
        issues, _ = quality.examine([{"id":"s1","category":"stories","cefr":"C1","story":"A brief story."}])
        self.assertTrue(any("too short" in x for x in issues))

    def test_clear_basic_item(self):
        issues, pending = quality.examine([{"id":"v2","category":"vocabulary","cefr":"A1","options":["red","green","blue"],"review_status":"approved"}])
        self.assertFalse(issues)
        self.assertEqual(0, pending)

if __name__ == "__main__":
    unittest.main()
