package com.speakup.app
internal data class MintLifeCityTurn(val prompt:String,val options:List<String>,val best:Int,val explanation:String)
internal object MintLifeVirtualCity {
 val cafe=listOf(
  MintLifeCityTurn("Barista: What would you like?",listOf("A cup of tea, please.","I am late yesterday.","I not want."),0,"A polite request is appropriate."),
  MintLifeCityTurn("Barista: Would you like milk?",listOf("Tomorrow morning.","No, thank you.","The bill was last week."),1,"Answer the offer politely."),
  MintLifeCityTurn("Barista: Anything else?",listOf("Yesterday is tomorrow.","I are ready.","That's all, thank you."),2,"Close the order courteously.")
 )
 fun valid()=cafe.size==3&&cafe.all{it.prompt.isNotBlank()&&it.options.size==3&&it.options.distinct().size==3&&it.best in 0..2&&it.explanation.isNotBlank()}
 fun advance(index:Int,choice:Int)=if(index in cafe.indices&&choice==cafe[index].best)index+1 else index
}
