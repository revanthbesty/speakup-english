package com.speakup.app

import android.content.Context

/** Explicitly unscored authoring preview; never feeds the reward or learner-attempt system. */
internal object MintGrammarDraftPreview {
    private const val ASSET = "grammar_arena_500_draft.json"

    fun load(context: Context): List<MintGrammarDraft> =
        context.assets.open(ASSET).bufferedReader(Charsets.UTF_8).use { reader ->
            MintGrammarJsonLoader.parseDraftBank(reader.readText())
        }

    /** Approved-only gate is intentionally separate and must not be bypassed. */
    fun playable(context: Context): List<MintGrammarDraft> =
        MintGrammarGate.publishableBatch(load(context))
}
