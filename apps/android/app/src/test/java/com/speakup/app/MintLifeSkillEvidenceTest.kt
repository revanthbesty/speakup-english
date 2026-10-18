package com.speakup.app
import org.junit.Assert.*
import org.junit.Test
class MintLifeSkillProfileTest {
 @Test fun evidenceIsVersionedAndUnique(){val x=MintLifeEvidence("e",MintLifeSkill.GRAMMAR,"grammar",1,1,90,null);assertEquals(1,MintLifeSkillProfile.accepted(listOf(x,x)).size);assertFalse(MintLifeSkillProfile.valid(x.copy(version=0)))}
 @Test fun doesNotTurnSelfConfidenceIntoProficiency(){val x=MintLifeEvidence("e",MintLifeSkill.SPEAKING,"self",1,1,null,80);assertNull(MintLifeSkillProfile.accepted(listOf(x))[0].observed)}
}
