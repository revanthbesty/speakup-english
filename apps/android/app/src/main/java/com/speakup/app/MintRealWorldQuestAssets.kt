package com.speakup.app

import android.content.Context
import java.io.FileNotFoundException

/** Offline asset boundary for the future 100-quest catalog.
 *  All authoring drafts remain unpublished. Missing or invalid assets fail closed.
 */
internal object MintRealWorldQuestAssets {
    const val PATH = "real_world_quests.json"

    fun loadApproved(context: Context): List<MintRealWorldQuestRecord> {
        val payload = try {
            context.assets.open(PATH).bufferedReader(Charsets.UTF_8).use { it.readText() }
        } catch (_: FileNotFoundException) {
            return emptyList()
        }
        return try {
            MintRealWorldQuestJson.published(payload)
        } catch (_: IllegalArgumentException) {
            emptyList()
        } catch (_: org.json.JSONException) {
            emptyList()
        }
    }
}
