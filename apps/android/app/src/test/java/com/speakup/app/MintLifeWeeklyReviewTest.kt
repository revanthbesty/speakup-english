package com.speakup.app
import org.junit.Assert.*
import org.junit.Test
class MintLifeWeeklyReviewTest {
 @Test fun noObservationsMeansNoInventedScore(){assertNull(MintLifeWeeklyReview.summarize(listOf(MintLifeWeekSample(MintLifeSkill.SPEAKING,null,1,1))).observedMean)}
 @Test fun badSamplesAreExcluded(){assertEquals(1,MintLifeWeeklyReview.summarize(listOf(MintLifeWeekSample(MintLifeSkill.GRAMMAR,70,0,1),MintLifeWeekSample(MintLifeSkill.GRAMMAR,200,0,2))).assessments)}
}
