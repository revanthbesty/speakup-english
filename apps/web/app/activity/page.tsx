"use client";
import {useState} from "react";
import Link from "next/link";

const tokens=["I","eat","rice","for","breakfast","today"];
const acceptedSentences=[
  "i eat rice for breakfast today",
  "i eat rice for breakfast"
];
export default function Activity(){
  const [picked,setPicked]=useState<string[]>([]);
  const [submitted,setSubmitted]=useState(false);
  const [checked,setChecked]=useState(false);
  const sentence=picked.join(" ").replace(/[.!?]+$/,"").trim().toLowerCase();
  const correct=acceptedSentences.includes(sentence);
  const toggle=(token:string)=>{
    if(submitted)return;
    setPicked(current=>current.includes(token)?current.filter(item=>item!==token):[...current,token]);
  };
  const check=()=>{setChecked(true);setSubmitted(true);};
  const retry=()=>{setPicked([]);setSubmitted(false);setChecked(false);};
  return <main className="app-shell narrow">
    <Link href="/journey" className="back">← Journey</Link>
    <div className="step">LESSON 1 • SENTENCE BUILDER</div>
    <h1>Build your sentence</h1>
    <p className="muted">Put the words in a natural order. Don’t worry about being perfect.</p>
    <div className="sentence-box" aria-live="polite" aria-label="Your sentence">
      {picked.length?picked.map((word,index)=><span key={word} className="chosen">{word}</span>):<span className="placeholder">Tap words below to build your sentence</span>}
    </div>
    <div className="token-grid" aria-label="Available words">
      {tokens.map(token=><button key={token} type="button" className={picked.includes(token)?"token selected":"token"} aria-pressed={picked.includes(token)} disabled={submitted} onClick={()=>toggle(token)}>{token}</button>)}
    </div>
    {!submitted?<button className="button wide" disabled={!picked.length} onClick={check}>Check my sentence</button>:<div className={correct?"feedback good":"feedback tip"} role="status">
      <span>{correct?"✓":"↻"}</span><div>
        <b>{correct?"Nice! That works.":"Good try — let's adjust the word order."}</b>
        <p>{correct?"Your sentence is clear and natural.":"Try: “I eat rice for breakfast today.” Start with who, then the action, then what and when."}</p>
        {correct?<Link className="button" href="/speaking">Now say it →</Link>:<button className="button" onClick={retry}>Try again</button>}
      </div>
    </div>}
  </main>;
}
