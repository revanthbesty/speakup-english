package com.speakup.app

import org.junit.Assert.*
import org.junit.Test

class MintGrammarQualityTest {
    @Test fun flagsAmbiguousAndBrokenOptionStructure() {
        val bad=MintGrammarQuality.Item("Choose.",listOf("is","is","are"),"is","")
        val issues=MintGrammarQuality.issues(bad)
        assertTrue("duplicate options" in issues)
        assertTrue("exactly one matching answer required" in issues)
        assertTrue("missing explanation" in issues)
    }
    @Test fun acceptsOneAnswerWithExplicitRuleAndDetectsDuplicatePrompts() {
        val item=MintGrammarQuality.Item("Pick past tense",listOf("walk","walked","walking"),"walked","Past simple uses walked.")
        assertTrue(MintGrammarQuality.issues(item).isEmpty())
        assertTrue(MintGrammarQuality.collectionIssues(listOf(item,item)).any { it.contains("duplicate prompt") })
    }
}
