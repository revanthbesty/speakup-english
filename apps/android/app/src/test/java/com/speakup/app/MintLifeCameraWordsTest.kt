package com.speakup.app
import org.junit.Assert.*
import org.junit.Test
class MintLifeCameraWordsTest {
 @Test fun cameraNeverStartsSilently(){assertFalse(MintLifeCameraWords.cameraPermitted(false,true,true));assertFalse(MintLifeCameraWords.cameraPermitted(true,true,false))}
 @Test fun manualFallbackNeedsNoCamera(){assertNotNull(MintLifeCameraWords.allowed("BOOK"));assertNull(MintLifeCameraWords.allowed("identity card"))}
}
