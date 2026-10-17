# Sprint 8 — spoken listening

Games → Listening now speaks all six existing sentences using the device's English
TextToSpeech voice. Play/replay replaces queued speech; Stop cancels it. Slower
speech uses 0.75× speed. Transcripts remain optional and available when audio fails.
Initialization, missing English voice data and synthesis errors have visible feedback.
No microphone permission is requested for listening.

The engine uses application context and exists only while Listening is visible.
Changing question, game, tab, onboarding state, or destroying the composition stops
and shuts down the engine. Backgrounding stops audio without automatic replay on
return. Engine callbacks are marshalled to the main thread; utterance tokens reject
stale completion, cancellation and failure callbacks. Disposal rejects late initialization.

## Compatibility

The application ID, preference file `speakup_learning`, all saved preference keys,
question ordering and first-correct-answer XP rules are unchanged. Existing
`listening_mastered` values remain the same zero-based IDs. Replaying audio, changing
speed and revealing transcripts do not award XP. Other learning modes are unchanged.

## Automated verification

Android CI runs `:app:testDebugUnitTest :app:lintDebug` before `:app:assembleDebug`.
Reports are uploaded even if verification fails. Session tests cover initialization,
missing voices, replay races, stop, engine cancellation, disposal and retry after
synthesis failure. These JVM tests do not verify physical audio output.

## Device acceptance checks

- Install as an update to the previous APK with the same signing key. Confirm XP,
  completed lessons, learning path, appearance and mastery are retained.
- Open Games → Listening; wait for Ready, play every sentence, replay rapidly,
  switch speed, stop, and confirm only the current sentence plays.
- During playback, change question, switch game/tab, background, rotate and leave
  the activity. Confirm audio stops and returning never automatically resumes it.
- Hide/reveal the transcript and answer incorrectly then correctly. Confirm only
  the first correct answer adds 10 XP, including after reopening the app.
- Test a device without an English TTS voice. Confirm helpful feedback and that
  transcript-based comprehension still works. Install English voice data and reopen.
- Verify both light/dark appearance and large text. Device media volume controls
  apply; a device voice may need downloaded data or a network connection.
