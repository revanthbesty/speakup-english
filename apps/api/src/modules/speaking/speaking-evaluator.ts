import { evaluateSentence } from "../activities/mvp-activities.js";

export type SpeakingEvaluation = {
  status: "COMPLETED" | "LOW_CONFIDENCE" | "FAILED";
  outcome: "CORRECT" | "PARTIAL" | "INCORRECT" | "NEUTRAL";
  confidence: number;
  dimensions: {
    sentenceConstruction: number;
    grammar: number;
    vocabulary: number;
    completeness: number;
    contextualRelevance: number;
  };
  feedback: {
    headline: string;
    explanation: string;
    priority: "NONE" | "LOW" | "MEDIUM" | "HIGH";
    retryHint?: string;
  };
  evidence: Array<{skill:string;weight:number}>;
};

function normalize(value:string) {
  return value
    .toLowerCase()
    .replace(/[.,!?]/g,"")
    .trim()
    .split(/\s+/)
    .filter(Boolean);
}

export function evaluateSpeakingTranscript(
  transcript:string,
  expectedContext="I eat rice for breakfast."
): SpeakingEvaluation {
  const clean=transcript.trim();

  if(!clean){
    return {
      status:"COMPLETED",
      outcome:"INCORRECT",
      confidence:1,
      dimensions:{
        sentenceConstruction:0,
        grammar:0,
        vocabulary:0,
        completeness:0,
        contextualRelevance:0
      },
      feedback:{
        headline:"Let's try speaking once more.",
        explanation:"We couldn't find enough spoken English to evaluate the sentence.",
        priority:"HIGH",
        retryHint:"Try saying: I eat rice for breakfast."
      },
      evidence:[]
    };
  }

  const words=normalize(clean);
  const expected=normalize(expectedContext);
  const sentence=evaluateSentence(words);

  const vocabularyTerms=["eat","rice","breakfast"];
  const vocabulary=vocabularyTerms.filter(x=>words.includes(x)).length/vocabularyTerms.length;

  const hasSubject=words.includes("i");
  const hasVerb=words.includes("eat");
  const completeness=Math.min(1,(hasSubject?0.25:0)+(hasVerb?0.35:0)+(words.length>=3?0.4:0));

  const contextualRelevance=Math.min(1,
    (words.includes("eat")?0.5:0)+
    (words.includes("breakfast")?0.3:0)+
    (words.includes("rice")?0.2:0)
  );

  const grammar =
    hasSubject && hasVerb
      ? sentence.outcome==="CORRECT" ? 1 : 0.65
      : 0.3;

  const confidence=0.9;

  if(sentence.outcome==="CORRECT"){
    return {
      status:"COMPLETED",
      outcome:"CORRECT",
      confidence,
      dimensions:{
        sentenceConstruction:1,
        grammar,
        vocabulary,
        completeness:1,
        contextualRelevance:1
      },
      feedback:{
        headline:"Great speaking!",
        explanation:"Your spoken sentence is clear and fits the breakfast question.",
        priority:"NONE"
      },
      evidence:[
        {skill:"SPEAKING",weight:1},
        {skill:"SENTENCE_CONSTRUCTION",weight:1},
        {skill:"GRAMMAR",weight:.5},
        {skill:"VOCABULARY",weight:.5}
      ]
    };
  }

  if(sentence.outcome==="PARTIAL"){
    return {
      status:"COMPLETED",
      outcome:"PARTIAL",
      confidence,
      dimensions:{
        sentenceConstruction:sentence.score,
        grammar,
        vocabulary,
        completeness,
        contextualRelevance
      },
      feedback:{
        headline:"Good attempt. Let's improve one part.",
        explanation:"Your answer connects to the question. Let's make the sentence order clearer.",
        priority:"MEDIUM",
        retryHint:"Start with I, then say the action: I eat..."
      },
      evidence:[
        {skill:"SPEAKING",weight:.75},
        {skill:"SENTENCE_CONSTRUCTION",weight:.75}
      ]
    };
  }

  return {
    status:"COMPLETED",
    outcome:"INCORRECT",
    confidence,
    dimensions:{
      sentenceConstruction:sentence.score,
      grammar,
      vocabulary,
      completeness,
      contextualRelevance
    },
    feedback:{
      headline:"Good try. Let's build the sentence again.",
      explanation:"Your answer needs more of the target sentence pattern.",
      priority:"HIGH",
      retryHint:"Try saying: I eat rice for breakfast."
    },
    evidence:[
      {skill:"SPEAKING",weight:.5},
      {skill:"SENTENCE_CONSTRUCTION",weight:.5}
    ]
  };
}

export function lowConfidenceSpeakingResult():SpeakingEvaluation {
  return {
    status:"LOW_CONFIDENCE",
    outcome:"NEUTRAL",
    confidence:0.35,
    dimensions:{
      sentenceConstruction:0,
      grammar:0,
      vocabulary:0,
      completeness:0,
      contextualRelevance:0
    },
    feedback:{
      headline:"Let's try that again.",
      explanation:"We weren't confident enough in the speech recognition to judge your English fairly.",
      priority:"MEDIUM",
      retryHint:"Speak a little more clearly and try again."
    },
    evidence:[]
  };
}

export function combineSpeechSignals(base: SpeakingEvaluation, pronunciationScore?: number | null, fluencyScore?: number | null): SpeakingEvaluation {
  const p = pronunciationScore == null ? null : Math.max(0, Math.min(1, pronunciationScore));
  const f = fluencyScore == null ? null : Math.max(0, Math.min(1, fluencyScore));
  if (p !== null) base.dimensions.sentenceConstruction = Number((base.dimensions.sentenceConstruction * .75 + p * .25).toFixed(4));
  if (f !== null) base.dimensions.completeness = Number((base.dimensions.completeness * .8 + f * .2).toFixed(4));
  return base;
}
