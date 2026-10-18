# English Mint — CEFR Topic Guidance and Originality Policy

Date: 2026-10-18. Status: editorial planning reference; **not an endorsement, CEFR certificate or permission to copy content**.

## Evidence from connected English Exercises catalogue
The plugin's `find_exercises` returned the following reference examples. This is catalogue metadata (titles, advertised levels, links), not a full independent review of questions or pedagogical accuracy.

| Proposed level | Search topic | Catalogue example | Label returned |
| --- | --- | --- | --- |
| A1 | present simple | [Present Simple Negative Statements](https://www.english-exercises.org/present-simple-negative) | A1 |
| A2 | past simple | [Past Simple Irregular Verbs](https://www.english-exercises.org/past-simple-irregular-verbs-multiple-choice) | A2 |
| B1 | present perfect | [Present Perfect in Business Reporting](https://www.english-exercises.org/business-present-perfect-exercises) | B1–B2 |
| B2 | conditionals | [Real-World Conditionals](https://www.english-exercises.org/conditionals-real-world-exercises) | B1–B2 |
| C1 | inversion | [Conditional Inversion (Advanced)](https://www.english-exercises.org/conditional-inversion-advanced) | C1 |

These are examples, not an exhaustive CEFR syllabus. Intermediate reference exercises are sometimes labeled with multiple levels. Use official CEFR descriptors and a qualified human reviewer to assess appropriateness.

## Authoring guidance (original development, NOT copied exercises)
- **A1:** familiar concrete nouns, simple present, greetings, dates, signs and direct retrieval from short texts.
- **A2:** past events, everyday transactions, sequences, predictable plans, short messages and simple inference.
- **B1:** experiences, routine work and education, reasons and outcomes, paraphrase, opinions and natural collocations.
- **B2:** conditionals, nuanced disagreement, argument structure, workplace problem-solving, implications and competing explanations.
- **C1:** precision, hedging, complex discourse, inference, counterargument, register, nuance and evidence evaluation; advanced inversion is one *possible* target, not a mandatory property of every C1 item.

## Requirements for the 8 activities
All eight categories (Word Builder, Riddles, Vocabulary, Situations, Listening, Reading, Dialogues, Stories) should draw from an increasing breadth of language tasks. A single grammar topic must not dominate the category. Every record needs a measurable objective, original prompt/source content, correct answer, explanation, stable ID and level justification; the category-specific model must be playable. Word builders should not reveal the answer. Listening requires device TTS or audio implementation plus replay and accessibility transcript, not merely stored text.

## Release safeguards
1. Content must be independently written and checked; do not copy third-party exercise text, answer keys or embedding material into packaged app assets without appropriate rights.
2. Record `review_status=needs_human_review` unless a real qualified reviewer approved it; an AI-generated answer cannot approve itself.
3. Fail content release when the exact category/level distribution, semantic originality, correctness or integration is not supported by evidence.
4. Do not auto-infer CEFR solely from a grammar keyword, passage length or reference catalogue label.
5. Expand the existing draft bank only after avoiding the repeated scenario patterns currently visible in the 360-item expansion.
6. Keep the offline-first Android architecture and existing learning progress and rewards ledger; do not merge, deploy, or use paid APIs without owner approval.

## Current implementation reality
40 editorial pilot + 360 expansion records = 400 *draft* records. Zero of the added records has verified Android integration, device TTS playback or editorial approval. This document is a planning input to authoring and review, not new playable content.
