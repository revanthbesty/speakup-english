import importlib.util
import unittest
from pathlib import Path

p = Path(__file__).resolve().parents[1] / "tools" / "audit_editorial_quality.py"
spec = importlib.util.spec_from_file_location("editorial_audit", p)
module = importlib.util.module_from_spec(spec)
spec.loader.exec_module(module)

class EditorialAuditTests(unittest.TestCase):
    def test_flags_generic_options(self):
        rows = [{"id": "Q1", "topic_id": "GRAMMAR-01",
                 "option_a": "correct", "option_b": "not applicable here",
                 "option_c": "different"}]
        flags = module.audit(rows)
        self.assertEqual(len(flags), 1)
        self.assertEqual(flags[0]["field"], "option_b")

    def test_allows_distinct_specific_distractors(self):
        rows = [{"id": "Q2", "topic_id": "GRAMMAR-02",
                 "option_a": "children", "option_b": "childs", "option_c": "childes"}]
        self.assertEqual(module.audit(rows), [])

if __name__ == "__main__":
    unittest.main()
