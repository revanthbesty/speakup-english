package com.speakup.app

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

/** Small safe entrance to original AI Life offline pilots. Nothing leaves this screen. */
@Composable
internal fun MintAiLifePilotScreen(onBack:()->Unit) {
 var section by remember { mutableStateOf("Home") }
 var cityTurn by remember { mutableIntStateOf(0) }
 var cityChoice by remember { mutableIntStateOf(-1) }
 var storyId by remember { mutableStateOf("start") }
 var cityFeedback by remember { mutableStateOf(false) }
 Column(Modifier.fillMaxWidth(),verticalArrangement=Arrangement.spacedBy(10.dp)) {
  TextButton(onClick={if(section=="Home")onBack() else section="Home"}) {Text("← "+if(section=="Home")"Home" else "AI Life")}
  Text("AI Life · offline pilot",style=MaterialTheme.typography.headlineSmall)
  Text("Private, original scripted learning. No local LLM installed, no paid AI, no camera capture or public family groups.")
  when(section) {
   "Home" -> {
    Button(onClick={section="City";cityTurn=0;cityChoice=-1;cityFeedback=false},modifier=Modifier.fillMaxWidth()){Text("Virtual City · café dialogue")}
    OutlinedButton(onClick={section="Story";storyId="start"},modifier=Modifier.fillMaxWidth()){Text("Branching story · library")}
    OutlinedButton(onClick={section="Words"},modifier=Modifier.fillMaxWidth()){Text("Camera Words · manual fallback")}
    OutlinedButton(onClick={section="Buddy"},modifier=Modifier.fillMaxWidth()){Text("Mint Buddy · offline text practice")}
    Text("Speaking Passport recording, learner history, daily plans and child collaboration need further privacy and device integration.",style=MaterialTheme.typography.bodySmall)
   }
   "Buddy" -> { MintBuddyOfflineScreen() }
   "City" -> {
    if(cityTurn<MintLifeVirtualCity.cafe.size) {
     val turn=MintLifeVirtualCity.cafe[cityTurn]
     Text("Café · turn ${cityTurn+1}/3")
     Text(turn.prompt,style=MaterialTheme.typography.titleMedium)
     turn.options.forEachIndexed { i,answer ->
      OutlinedButton(onClick={cityChoice=i;cityFeedback=false},modifier=Modifier.fillMaxWidth().heightIn(min=48.dp)) {Text("${i+1}. $answer")}
     }
     if(cityFeedback){Text(if(cityChoice==turn.best)"Good response!" else "Try another answer.");Text(turn.explanation)}
     Button(onClick={if(cityFeedback&&cityChoice==turn.best){cityTurn=MintLifeVirtualCity.advance(cityTurn,cityChoice);cityChoice=-1;cityFeedback=false}else cityFeedback=true},enabled=cityChoice>=0){
      Text(if(cityFeedback&&cityChoice==turn.best)"Continue" else "Check response")
     }
    } else { Text("You completed the café conversation!");Button(onClick={cityTurn=0;cityChoice=-1}){Text("Replay")}}
   }
   "Story" -> {
    val node=MintLifeStoryGraph.pilot.first{it.id==storyId}
    Text(node.caption,style=MaterialTheme.typography.titleMedium)
    node.choices.forEachIndexed { index,choice ->
     OutlinedButton(onClick={storyId=MintLifeStoryGraph.next(MintLifeStoryGraph.pilot,storyId,index)?:storyId},modifier=Modifier.fillMaxWidth().heightIn(min=48.dp)){Text(choice.caption)}
    }
    if(node.choices.isEmpty()) Button(onClick={storyId="start"}){Text("Read again")}
   }
   else -> {
    Text("Manual word practice",style=MaterialTheme.typography.titleMedium)
    MintLifeCameraWords.manualWords.forEach{item->
     Text("${item.label}: ${item.example}")
    }
    Text("This demo does not request the camera or transmit images.")
   }
  }
 }
}
