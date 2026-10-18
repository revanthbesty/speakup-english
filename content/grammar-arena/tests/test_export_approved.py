"""Safety tests: unreviewed questions cannot be exported."""
import csv
import importlib.util
import tempfile
import unittest
from pathlib import Path

ROOT = Path(__file__).resolve().parents[1] / "tools"
import sys
sys.path.insert(0, str(ROOT))
from export_approved import export_payload

class ExportTests(unittest.TestCase):
    def test_draft_is_blocked(self):
        with tempfile.TemporaryDirectory() as d:
            path = Path(d) / "draft.csv"
            fields = ["id","version","status","topic_id","subtopic_id","level",
                      "age_segment","learning_outcome","format","prompt",
                      "option_a","option_b","option_c","correct_key","explanation",
                      "distractor_rationale","locale","source_or_author","rights_status",
                      "estimated_seconds","accessibility_flags","tags","duplicate_hash",
                      "reviewer_id","approved_at","last_reviewed_at"]
            row = {field:"value" for field in fields}
            row.update(id="fixture-1",version="1",status="draft",level="Easy",
                       option_a="one",option_b="two",option_c="three",
                       correct_key="A",explanation="The first answer fits this specific grammatical context.",
                       rights_status="original_text_unreviewed",
                       reviewer_id="",approved_at="",last_reviewed_at="")
            with path.open("w",encoding="utf-8",newline="") as handle:
                writer=csv.DictWriter(handle,fieldnames=fields)
                writer.writeheader()
                writer.writerow(row)
            with self.assertRaises(ValueError):
                export_payload(path)

if __name__ == "__main__":
    unittest.main()
