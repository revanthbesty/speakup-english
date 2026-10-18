package com.speakup.app
import org.junit.Assert.*
import org.junit.Test
class MintSpeechPrivacyTest {
 @Test fun unverifiedSpeechCannotPretendOffline(){
  assertFalse(MintSpeechPrivacy.permitOfflineSpeech(MintSpeechRequest(true,MintSpeechCapability.UNVERIFIED_SYSTEM,false)))
  assertFalse(MintSpeechPrivacy.permitOfflineSpeech(MintSpeechRequest(false,MintSpeechCapability.VERIFIED_OFFLINE,false)))
  assertTrue(MintSpeechPrivacy.permitOfflineSpeech(MintSpeechRequest(true,MintSpeechCapability.VERIFIED_OFFLINE,false)))
 }
}
