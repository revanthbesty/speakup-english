export type Answer = "A" | "B" | "C";
export type Difficulty = "EASY" | "MEDIUM" | "HARD";
export type Quest = {
  quest_id: string;
  category_id: string;
  quest_title: string;
  difficulty: Difficulty;
  scenario_context: string;
  character_name: string;
  character_dialogue: string;
  question_prompt: string;
  option_a: string;
  option_b: string;
  option_c: string;
  correct_answer: Answer;
  correct_explanation: string;
  wrong_a_explanation: string;
  wrong_b_explanation: string;
  wrong_c_explanation: string;
  speaking_prompt: string;
  text_fallback_instruction: string;
  completion_message: string;
  content_status: string;
};
export type QuestAnswerFeedback = {
  correct: boolean;
  explanation: string;
  correctAnswer: Answer;
  correctText: string;
};
const answerKey = { A: "option_a", B: "option_b", C: "option_c" } as const;
const wrongKey = { A: "wrong_a_explanation", B: "wrong_b_explanation", C: "wrong_c_explanation" } as const;
export function evaluateQuestAnswer(quest: Quest, answer: Answer): QuestAnswerFeedback {
  if (!["A", "B", "C"].includes(answer)) throw new Error("Invalid quest answer");
  if (!["A", "B", "C"].includes(quest.correct_answer)) throw new Error("Invalid quest correct answer");
  const correct = quest.correct_answer === answer;
  return {
    correct,
    explanation: correct ? quest.correct_explanation : quest[wrongKey[answer]],
    correctAnswer: quest.correct_answer,
    correctText: quest[answerKey[quest.correct_answer]],
  };
}
export function selectPublishableQuests(quests: Quest[]): Quest[] {
  return quests.filter((quest) => quest.content_status.toLowerCase() === "approved");
}
export function summarizeQuestAnswers(answers: Array<{correct:boolean}>) {
  const correct = answers.filter((answer) => answer.correct).length;
  return { total: answers.length, correct, wrong: answers.length - correct };
}
