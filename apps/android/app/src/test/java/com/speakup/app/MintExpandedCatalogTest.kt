package com.speakup.app

import org.junit.Assert.*
import org.junit.Test

class MintExpandedCatalogTest {
    @Test fun seventyTwoAuthoredPlayableQuestsAcrossEightMechanics() {
        val all = MintGameCatalog.allDefinitions
        assertEquals(72, all.size)
        assertEquals(72, all.map { it.id }.toSet().size)
        assertEquals(8, all.map { it.mechanic }.toSet().size)
        assertTrue(all.all { it.isValid() })
        assertTrue(all.all { it.evaluate(it.answer) })
    }

    @Test fun everyNewMechanicProvidesFourUniqueQuestsAtEachDifficulty() {
        val added = MintExpansionPack.definitions
        assertEquals(24, added.size)
        listOf(MintMechanic.LISTEN_AND_CHOOSE,MintMechanic.STORY_DECISION).forEach { mechanic ->
            MintDifficulty.entries.forEach { level ->
                val quests = added.filter { it.mechanic == mechanic && it.difficulty == level }
                assertEquals(4, quests.size)
                assertEquals(4, quests.map { it.prompt }.toSet().size)
                assertTrue(quests.all { it.tokens.size == 3 && it.tokens.count { t -> t == it.answer } == 1 })
            }
        }
    }

    @Test fun visualVocabularyPackCoversAllLevelsAndValidPictures() {
        val pack = MintVisualVocabularyPack.definitions
        assertEquals(24, pack.size)
        listOf(MintMechanic.WORD_CONNECT,MintMechanic.PICTURE_MATCH).forEach { mechanic ->
            MintDifficulty.entries.forEach { level ->
                val items = pack.filter { it.mechanic == mechanic && it.difficulty == level }
                assertEquals(4, items.size)
                assertTrue(items.all { it.isValid() && it.evaluate(it.answer) })
            }
        }
        assertTrue(pack.filter { it.mechanic == MintMechanic.PICTURE_MATCH }
            .all { it.prompt.isNotBlank() && it.speakingPrompt.isNotBlank() })
    }

    @Test fun spokenListeningLinesExistOnlyForListeningMechanic() {
        val pack = MintExpansionPack.definitions
        assertTrue(pack.filter { it.mechanic == MintMechanic.LISTEN_AND_CHOOSE }
            .all { !it.listeningLine.isNullOrBlank() })
        assertTrue(pack.filter { it.mechanic == MintMechanic.STORY_DECISION }
            .all { it.listeningLine.isNullOrBlank() })
    }

    @Test fun oldRewardsStillResolveIdenticalContentAndKeys() {
        val oldId = "reply-greet"
        val definition = MintGameCatalog.byId(oldId)!!
        assertEquals(1, definition.version)
        assertEquals(15, MintGameEngine.reward(definition,definition.answer,emptySet()).xpAward)
        assertEquals(0,MintGameEngine.reward(definition,definition.answer,setOf("mint_game_reply-greet_v1")).xpAward)
        assertNotNull(MintGameCatalog.byId("listen-greeting"))
        assertNotNull(MintGameCatalog.byId("story-escalation"))
    }

    @Test fun levelsAndAgeRestrictionsHoldAcrossEntireCatalog() {
        assertTrue(MintGameCatalog.listFor(MintDifficulty.HARD,4).isEmpty())
        assertTrue(MintGameCatalog.listFor(MintDifficulty.MEDIUM,4).isEmpty())
        assertTrue(MintGameCatalog.listFor(MintDifficulty.EASY,4).isNotEmpty())
        MintDifficulty.entries.forEach { level ->
            assertEquals(24, MintGameCatalog.listFor(level,18).size)
        }
    }

    @Test fun questionsAreNotAcceptedWhenAnswerIsWrong() {
        MintExpansionPack.definitions.forEach { quest ->
            assertFalse(quest.evaluate(quest.tokens.first { it != quest.answer }))
            assertTrue(quest.hint.length > 8)
            assertTrue(quest.explanation.length > 8)
            assertTrue(quest.speakingPrompt.length > 8)
        }
    }
}
