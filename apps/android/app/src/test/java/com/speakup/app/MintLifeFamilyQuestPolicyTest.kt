package com.speakup.app
import org.junit.Assert.*
import org.junit.Test
class MintLifeFamilyQuestPolicyTest {
 @Test fun childInvitationIsDeniedByDefault(){assertFalse(MintLifeFamilyQuestPolicy.canJoin(null,10));assertFalse(MintLifeFamilyQuestPolicy.publicVoiceRoomAllowed())}
 @Test fun revocationAndExpirationAreEnforced(){val x=MintFamilyQuestPermission("child",true,true,100,false,false);assertTrue(MintLifeFamilyQuestPolicy.canJoin(x,20));assertFalse(MintLifeFamilyQuestPolicy.canJoin(x.copy(revoked=true),20));assertFalse(MintLifeFamilyQuestPolicy.canJoin(x,100))}
}
