package com.speakup.app

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import android.content.Context
import java.time.LocalDate
import androidx.compose.ui.unit.dp

/** Offline AI Life pilots. Only verified mission dates are saved on device; no transcript is uploaded. */
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
 var showClearMissionPrompt by remember { mutableStateOf(false) }
 val today = LocalDate.now().toString()

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
  MintSectionHeading("AI Life", "Real-world English practice • Private and offline")
  when(section) {
   "Home" -> {
    MintDashboardHero(
     kicker="Virtual City • Everyday English",
     title="Learn English for real life",
     description="Start with a café conversation. The scenarios use reviewed local scripts, not a live AI chat service.",
     action="Enter the café  →",
     onAction={section="City";cityTurn=0;cityChoice=-1;cityFeedback=false}
    )
    MintInfoBanner("Today's practice",
     "${MintLifeMissionLedger.completedMissionCount(missionEvidence,today)} of 1 café mission completed • ${MintLifeMissionLedger.daysPractised(missionEvidence,today)} of the last 7 days practiced.",
     MintVisualTokens.mint)
    MintSectionHeading("Choose an activity","All activities work without paid AI")
    Row(Modifier.fillMaxWidth(), horizontalArrangement=Arrangement.spacedBy(10.dp)) {
     MintDestinationTile("Branching story","Choose what happens","📖",MintVisualTokens.violet,Modifier.weight(1f)){
      section="Story";storyId="start"
     }
     MintDestinationTile("Mint Buddy","Text conversation","🎤",MintVisualTokens.blue,Modifier.weight(1f)){
      section="Buddy"
     }
    }
    MintDestinationTile("Words around you","Manual vocabulary, no camera needed","📷",
      MintVisualTokens.orange,Modifier.fillMaxWidth()){section="Words"}
    if(missionEvidence.isNotEmpty()) {
     TextButton(onClick={showClearMissionPrompt=true}){Text("Delete daily mission history")}
    }
    Text("Voice recording, automatic camera recognition and family sharing are not enabled in this pilot.",
      color=MaterialTheme.colorScheme.onSurfaceVariant,style=MaterialTheme.typography.bodySmall)
   }
   "Buddy" -> { MintBuddyOfflineScreen() }
   "City" -> {
    if(cityTurn<MintLifeVirtualCity.cafe.size) {
     val turn=MintLifeVirtualCity.cafe[cityTurn]
     MintInfoBanner("Café conversation · turn ${cityTurn+1}/3",turn.prompt,MintVisualTokens.blue)
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
    MintSectionHeading("Words around you", "Manual practice. Camera permission is not needed.")
    MintLifeCameraWords.manualWords.forEach{item->
     MintInfoBanner(item.label,item.example,MintVisualTokens.orange)
    }
    Text("This demo does not request the camera or transmit images.")
   }
  }
 }
}
