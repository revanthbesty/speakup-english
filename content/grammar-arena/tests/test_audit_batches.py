import csv
import importlib.util
import tempfile
import unittest
from pathlib import Path

SCRIPT = Path(__file__).resolve().parents[1] / "tools" / "audit_batches.py"
spec = importlib.util.spec_from_file_location("batch_audit", SCRIPT)
audit = importlib.util.module_from_spec(spec)
spec.loader.exec_module(audit)

class BatchAuditTests(unittest.TestCase):
    def write(self, path, rows):
        with path.open("w", newline="", encoding="utf-8") as handle:
            writer = csv.DictWriter(handle, fieldnames=["id", "duplicate_hash", "prompt"])
            writer.writeheader()
            writer.writerows(rows)

    def test_cross_file_duplicates(self):
        with tempfile.TemporaryDirectory() as folder:
            one, two = Path(folder) / "a.csv", Path(folder) / "b.csv"
            self.write(one, [{"id":"q1","duplicate_hash":"hash1","prompt":"Which sentence is correct?"}])
            self.write(two, [{"id":"q1","duplicate_hash":"hash1","prompt":" WHICH   sentence is correct? "}])
            result = audit.audit([one, two])
            self.assertEqual(set(result["issues"][0]["reasons"]),
                             {"duplicate_id", "duplicate_hash", "exact_normalized_prompt"})

    def test_clean_input(self):
        with tempfile.TemporaryDirectory() as folder:
            path = Path(folder) / "a.csv"
            self.write(path, [
                {"id":"q1","duplicate_hash":"hash1","prompt":"Select an appropriate article."},
                {"id":"q2","duplicate_hash":"hash2","prompt":"Identify the plural noun in this phrase."}])
            self.assertEqual(audit.audit([path])["issues"], [])

    def test_near_matches_are_review_flags(self):
        with tempfile.TemporaryDirectory() as folder:
            path = Path(folder) / "a.csv"
            self.write(path, [
                {"id":"q1","duplicate_hash":"hash1","prompt":"The long train arrived at the station at noon."},
                {"id":"q2","duplicate_hash":"hash2","prompt":"The long train arrived at the station at night."}])
            result = audit.audit([path], threshold=0.80)
            self.assertIn("near_duplicate_prompt_review", result["issues"][0]["reasons"])
