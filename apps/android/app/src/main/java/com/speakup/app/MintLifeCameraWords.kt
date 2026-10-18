package com.speakup.app
internal data class MintCameraWord(val label:String,val example:String,val sensitive:Boolean)
internal object MintLifeCameraWords {
 val manualWords=listOf(MintCameraWord("book","I am reading a book.",false),MintCameraWord("cup","This is a cup.",false),MintCameraWord("chair","The chair is beside the table.",false))
 fun allowed(label:String)=manualWords.firstOrNull{it.label.equals(label.trim(),ignoreCase=true)&&!it.sensitive}
 fun cameraPermitted(explicitRequest:Boolean,permissionGranted:Boolean,onDeviceModelApproved:Boolean):Boolean =
  explicitRequest&&permissionGranted&&onDeviceModelApproved
}
