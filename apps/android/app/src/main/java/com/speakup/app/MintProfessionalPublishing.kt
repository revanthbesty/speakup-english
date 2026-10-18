package com.speakup.app

/** Publication is impossible without traceable, human editorial approval. */
internal data class MintProfessionalEditorialRecord(
 val itemId:String,val version:Int,val source:String,val rightsReference:String,
 val reviewer:String,val approvalReference:String,val approvalGranted:Boolean
)
internal object MintProfessionalPublishing {
 fun approved(record:MintProfessionalEditorialRecord):Boolean =
  record.itemId.isNotBlank()&&record.version>0&&record.source.isNotBlank()&&
  record.rightsReference.isNotBlank()&&record.reviewer.isNotBlank()&&
  record.approvalReference.isNotBlank()&&record.approvalGranted
 fun publishable(records:List<MintProfessionalEditorialRecord>,requiredIds:Set<String>):Boolean =
  requiredIds.isNotEmpty() && requiredIds.all { id ->
   records.count { it.itemId==id && approved(it) }==1
  } && records.filter { it.itemId in requiredIds }.all(::approved)
}
