"""Regression tests for cross-pack content audit."""
import importlib.util
import json
import tempfile
import unittest
from pathlib import Path

spec = importlib.util.spec_from_file_location("audit", Path(__file__).with_name("audit_english_mint_content.py"))
audit = importlib.util.module_from_spec(spec)
spec.loader.exec_module(audit)

class AuditTests(unittest.TestCase):
    def test_detects_duplicate_id_across_files(self):
        with tempfile.TemporaryDirectory() as folder:
            paths = [Path(folder) / f"p{i}.json" for i in (1,2)]
            for path in paths:
                path.write_text(json.dumps({"activities":[{"id":"repeat","category":"riddles","content":"One clue","prompt":"What is it?"}]}))
            errors, warnings = audit.audit(paths)
            self.assertTrue(errors)

    def test_allows_distinct_short_samples(self):
        with tempfile.TemporaryDirectory() as folder:
            path = Path(folder) / "pilot.json"
            path.write_text(json.dumps({"activities":[
                {"id":"one","category":"riddles","content":"I have four legs.","prompt":"Name me."},
                {"id":"two","category":"reading","content":"A note about the park","prompt":"Where is the park?"}
            ]}))
            errors, warnings = audit.audit([path])
            self.assertFalse(errors)
            self.assertFalse(warnings)

if __name__ == "__main__":
    unittest.main()
