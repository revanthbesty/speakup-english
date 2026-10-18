import type {Quest} from "./real-world-quests";

export type QuestCategory = {
  category_id: string;
  category_name: string;
  description: string;
  target_count: number;
};
export type QuestCatalog = {
  schema_version: string;
  publication_status: string;
  categories: QuestCategory[];
  quests: Quest[];
};
export function validateQuestCatalog(input: unknown): QuestCatalog {
  if (!input || typeof input !== "object") throw new Error("Quest catalog must be an object");
  const x = input as Record<string, unknown>;
  if (x.schema_version !== "1.0" || !Array.isArray(x.categories) || !Array.isArray(x.quests)) {
    throw new Error("Unsupported quest catalog structure");
  }
  if (x.quests.length !== 100 || x.categories.length !== 10) throw new Error("Expected 100 quests and 10 categories");
  const ids = new Set<string>();
  const categoryIds = new Set<string>();
  for (const c of x.categories as QuestCategory[]) {
    if (!c.category_id || !c.category_name || categoryIds.has(c.category_id)) throw new Error("Invalid or duplicate quest category");
    categoryIds.add(c.category_id);
  }
  const count:Record<string,number> = {EASY:0,MEDIUM:0,HARD:0};
  for (const q of x.quests as Quest[]) {
    if (typeof q.quest_id !== "string" || ids.has(q.quest_id)) throw new Error("Duplicate or invalid quest ID");
    ids.add(q.quest_id);
    if (!categoryIds.has(q.category_id)) throw new Error("Unknown category for " + q.quest_id);
    if (!(q.difficulty in count)) throw new Error("Invalid difficulty for " + q.quest_id);
    count[q.difficulty]++;
    if (!["A","B","C"].includes(q.correct_answer)) throw new Error("Invalid correct answer for " + q.quest_id);
    for(const key of ["quest_title","scenario_context","character_dialogue","question_prompt","option_a","option_b","option_c","correct_explanation","wrong_a_explanation","wrong_b_explanation","wrong_c_explanation","speaking_prompt","completion_message"] as const) {
      if(typeof q[key] !== "string" || !q[key].trim()) throw new Error("Missing " + key + " for " + q.quest_id);
    }
    if(!["draft","approved"].includes(q.content_status.toLowerCase())) throw new Error("Unknown editorial status for " + q.quest_id);
  }
  if(count.EASY !== 40 || count.MEDIUM !== 35 || count.HARD !== 25) throw new Error("Incorrect quest difficulty distribution");
  return x as unknown as QuestCatalog;
}
export function filterQuests(quests: Quest[], category = "all", difficulty = "all", includeDraft = false): Quest[] {
  return quests.filter(q => (includeDraft || q.content_status.toLowerCase() === "approved") &&
    (category === "all" || q.category_id === category) && (difficulty === "all" || q.difficulty === difficulty));
}
