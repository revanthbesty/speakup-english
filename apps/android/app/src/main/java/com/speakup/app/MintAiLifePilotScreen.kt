package com.speakup.app

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import android.content.Context
import java.time.LocalDate
import androidx.compose.ui.unit.dp

/** Offline AI Life pilots. Only verified mission dates are saved on device; no transcript is uploaded. */
@Composable
internal fun MintAiLifePilotScreen(onBack:()->Unit) {
 var section by rememberSaveable { mutableStateOf("Home") }
 var cityTurn by rememberSaveable { mutableIntStateOf(0) }
 var cityChoice by rememberSaveable { mutableIntStateOf(-1) }
 var storyId by rememberSaveable { mutableStateOf("start") }
 var cityFeedback by rememberSaveable { mutableStateOf(false) }
 val context = LocalContext.current
 val missionPreferences = remember(context) {
  context.getSharedPreferences("speakup_learning", Context.MODE_PRIVATE)
 }
 var missionEvidence by remember(missionPreferences) {
  mutableStateOf(
   (missionPreferences.getStringSet(MintLifeMissionLedger.storageKey, emptySet()) ?: emptySet()).toSet()
  )
 }
 var showClearMissionPrompt by remember { mutableStateOf(false) }
 val today = LocalDate.now().toString()
 // Handle nested AI Life pages before the activity-level Back handler navigates Home.
 BackHandler(enabled = section != "Home" && !showClearMissionPrompt) {
  section = "Home"
 }

 if(showClearMissionPrompt) {
  AlertDialog(
   onDismissRequest={showClearMissionPrompt=false},
   title={Text("Delete mission history?")},
   text={Text("This clears only AI Life daily café completion dates on this device. It will not erase lessons, rewards or other learning records.")},
   confirmButton={
    TextButton(onClick={
     missionPreferences.edit().remove(MintLifeMissionLedger.storageKey).apply()
     missionEvidence=emptySet()
     showClearMissionPrompt=false
    }){Text("Delete mission history")}
   },
   dismissButton={TextButton(onClick={showClearMissionPrompt=false}){Text("Cancel")}}
  )
 }
 Column(Modifier.fillMaxWidth(),verticalArrangement=Arrangement.spacedBy(10.dp)) {
  TextButton(onClick={if(section=="Home")onBack() else section="Home"}) {Text("← "+if(section=="Home")"Home" else "AI Life")}
  MintScreenHeading("AI Life", "Practice everyday English in your own world.", "EXPLORE • OFFLINE")
  when(section) {
   "Home" -> {
    Text("Today's verified mission: ${MintLifeMissionLedger.completedMissionCount(missionEvidence,today)} of 1 original café conversation",style=MaterialTheme.typography.bodyMedium)
    Text("Finish all three correct dialogue turns to record a private daily completion. Opening an activity does not count.",style=MaterialTheme.typography.bodySmall)
    Text("Café practice recorded on ${MintLifeMissionLedger.daysPractised(missionEvidence,today)} of the last 7 days (this device only).",
      style=MaterialTheme.typography.bodySmall)
    if(missionEvidence.isNotEmpty()) {
     TextButton(onClick={showClearMissionPrompt=true}){Text("Delete AI Life daily mission history")}
    }

    MintSectionCard("Choose your activity") {
     MintPrimaryAction("☕ Virtual City · café dialogue", onClick={
      section="City";cityTurn=0;cityChoice=-1;cityFeedback=false
     })
     OutlinedButton(onClick={section="Story";storyId="start"},modifier=Modifier.fillMaxWidth()){Text("📚 Branching story")}
     OutlinedButton(onClick={section="Words"},modifier=Modifier.fillMaxWidth()){Text("🔤 Practice everyday words")}
     OutlinedButton(onClick={section="Buddy"},modifier=Modifier.fillMaxWidth()){Text("💬 Mint Buddy")}
    }
    Text("All activities on this screen are scripted, private offline previews. No generative AI model or camera is active.",
     style=MaterialTheme.typography.bodySmall,color=MaterialTheme.colorScheme.onSurfaceVariant)
   }
   "Buddy" -> { MintBuddyOfflineScreen() }
   "City" -> {
    if(cityTurn<MintLifeVirtualCity.cafe.size) {
     val turn=MintLifeVirtualCity.cafe[cityTurn]
     Text("Café · turn ${cityTurn+1}/3")
     Text(turn.prompt,style=MaterialTheme.typography.titleMedium)
     turn.options.forEachIndexed { i,answer ->
      MintAnswerChoice("${i+1}. $answer", selected=cityChoice==i, onClick={cityChoice=i;cityFeedback=false})
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
