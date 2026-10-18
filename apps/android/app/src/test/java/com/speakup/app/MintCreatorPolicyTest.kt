package com.speakup.app

import org.junit.Assert.*
import org.junit.Test

class MintCreatorPolicyTest {
    @Test fun draftCannotAdvanceWithoutRightsReview() {
        val d=MintCreatorDraft("draft1","A café","Could I have a tea?","",MintCreatorStatus.DRAFT)
        assertNull(MintCreatorPolicy.next(d,false,false,false))
        assertNull(MintCreatorPolicy.next(d,true,false,false))
        assertFalse(MintCreatorPolicy.publicPublishingAllowed(d))
    }
    @Test fun editorialStagesAreStrictAndNeverPublish() {
        var d=MintCreatorDraft("draft1","A café","Could I have a tea?","Original author",MintCreatorStatus.DRAFT)
        d=MintCreatorPolicy.next(d,true,false,false)!!
        assertEquals(MintCreatorStatus.RIGHTS_CHECKED,d.status)
        assertNull(MintCreatorPolicy.next(d,false,false,true))
        d=MintCreatorPolicy.next(d,false,true,false)!!
        assertNull(MintCreatorPolicy.next(d,false,false,false))
        d=MintCreatorPolicy.next(d,false,false,true)!!
        d=MintCreatorPolicy.next(d,false,false,false)!!
        assertEquals(MintCreatorStatus.READY_FOR_SCHEDULING,d.status)
        assertFalse(MintCreatorPolicy.publicPublishingAllowed(d))
    }
}
