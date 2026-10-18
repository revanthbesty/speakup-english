package com.speakup.app
import org.junit.Assert.*
import org.junit.Test
class MintLifePassportPolicyTest {
 @Test fun noConsentMeansNoRecording(){assertFalse(MintLifePassportPolicy.retain(MintPassportDraft("id",1,false,"file:/record",null,null,false)))}
 @Test fun deleteDropsAllSensitiveMetadata(){val x=MintPassportDraft("id",1,true,"file:/record","hello",3,false);assertNull(MintLifePassportPolicy.redact(x).transcript);assertFalse(MintLifePassportPolicy.retain(MintLifePassportPolicy.redact(x)))}
}
