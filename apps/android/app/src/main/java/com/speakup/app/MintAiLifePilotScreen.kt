package com.speakup.app

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import android.content.Context
import java.time.LocalDate
import androidx.compose.ui.unit.dp

/** Small safe entrance to original AI Life offline pilots. Nothing leaves this screen. */
@Composable
internal fun MintAiLifePilotScreen(onBack:()->Unit) {
 var section by remember { mutableStateOf("Home") }
 var cityTurn by remember { mutableIntStateOf(0) }
 var cityChoice by remember { mutableIntStateOf(-1) }
 var storyId by remember { mutableStateOf("start") }
 var cityFeedback by remember { mutableStateOf(false) }
 val context = LocalContext.current
 val missionPreferences = remember(context) {
  context.getSharedPreferences("speakup_learning", Context.MODE_PRIVATE)
 }
 var missionEvidence by remember(missionPreferences) {
  mutableStateOf(
   (missionPreferences.getStringSet(MintLifeMissionLedger.storageKey, emptySet()) ?: emptySet()).toSet()
  )
 }
 val today = LocalDate.now().toString()

 Column(Modifier.fillMaxWidth(),verticalArrangement=Arrangement.spacedBy(10.dp)) {
  TextButton(onClick={if(section=="Home")onBack() else section="Home"}) {Text("← "+if(section=="Home")"Home" else "AI Life")}
  Text("AI Life · offline pilot",style=MaterialTheme.typography.headlineSmall)
  Text("Private, original scripted learning. No local LLM installed, no paid AI, no camera capture or public family groups.")
  when(section) {
   "Home" -> {
    Text("Today's verified mission: ${MintLifeMissionLedger.completedMissionCount(missionEvidence,today)} of 1 original café conversation",style=MaterialTheme.typography.bodyMedium)
    Text("Finish all three correct dialogue turns to record a private daily completion. Opening an activity does not count.",style=MaterialTheme.typography.bodySmall)
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
     Button(onClick={
      if(cityFeedback && cityChoice==turn.best) {
       val newTurn = MintLifeVirtualCity.advance(cityTurn,cityChoice)
       if(newTurn==MintLifeVirtualCity.cafe.size && cityTurn==MintLifeVirtualCity.cafe.lastIndex) {
        val recorded=MintLifeMissionLedger.recordVerified(
         missionEvidence,LocalDate.now().toString(),MintLifeMissionLedger.cafeMissionId,true
        )
        if(recorded!=missionEvidence) {
         missionEvidence=recorded
         missionPreferences.edit().putStringSet(MintLifeMissionLedger.storageKey,recorded).apply()
        }
       }
       cityTurn=newTurn
       cityChoice=-1
       cityFeedback=false
      } else cityFeedback=true
     },enabled=cityChoice>=0){
      Text(if(cityFeedback&&cityChoice==turn.best)"Continue" else "Check response")
     }
    } else {
     Text("You completed all three café turns.")
     Text(if(MintLifeMissionLedger.completedToday(missionEvidence,today,MintLifeMissionLedger.cafeMissionId))
      "Today's verified completion is saved on this device." else "Completion hasn't been saved.")
     Text("Practice record only. No XP, Mint Bloom, or proficiency score awarded.")
     Button(onClick={cityTurn=0;cityChoice=-1;cityFeedback=false}){Text("Replay")}
    }
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
