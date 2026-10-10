"use client";

import Link from "next/link";

export default function Home() {
  return (
    <main className="app-shell">
      <header className="top">
        <div>
          <div className="brand">
            SpeakUp <span>English</span>
          </div>
          <p className="muted">Good afternoon 👋 Ready to speak?</p>
        </div>
        <div className="xp">
          🔥 3 day streak <b>120 XP</b>
        </div>
      </header>

      <section className="welcome">
        <div>
          <p className="eyebrow">TODAY&apos;S PRACTICE</p>
          <h1>Breakfast conversations</h1>
          <p>
            Use the verb <strong>eat</strong> to build a natural sentence, then
            say it out loud.
          </p>
          <Link className="button" href="/activity">
            Continue lesson →
          </Link>
        </div>
        <div className="progress-ring">
          <strong>35%</strong>
          <span>today</span>
        </div>
      </section>

      <div className="grid3">
        <Link href="/activity" className="feature">
          <span className="icon">Aa</span>
          <b>Build a sentence</b>
          <small>WHO → ACTION → WHAT → WHEN</small>
        </Link>
        <Link href="/speaking" className="feature">
          <span className="icon">🎙</span>
          <b>Speak</b>
          <small>Practice your sentence with your coach</small>
        </Link>
        <Link href="/progress" className="feature">
          <span className="icon">↗</span>
          <b>My progress</b>
          <small>See mastery, streak and speaking growth</small>
        </Link>
      </div>

      <section className="coach">
        <div className="coach-avatar">S</div>
        <div>
          <b>Your Buddy</b>
          <p>
            “Small sentences are powerful. Let&apos;s make one about breakfast.”
          </p>
        </div>
        <Link className="text-link" href="/activity">
          Practice
        </Link>
      </section>
    </main>
  );
}
