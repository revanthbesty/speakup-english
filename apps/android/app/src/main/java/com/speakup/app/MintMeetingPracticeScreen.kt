package com.speakup.app

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
internal fun MintMeetingPracticeScreen(onVerified: (String) -> Unit = {}) {
 var category by remember { mutableStateOf("Meeting") }
 var index by remember { mutableIntStateOf(0) }
 var choice by remember { mutableIntStateOf(-1) }
 var checked by remember { mutableStateOf(false) }
 val items=MintMeetingPractice.items.filter { it.category==category }
 val item=items[index]
 Column(verticalArrangement=Arrangement.spacedBy(10.dp)) {
  MintScreenHeading("Meetings & Presentations", "Build workplace communication confidence.", "PROFESSIONAL ENGLISH")
  Text("Original communication practice, not a certified presentation score.")
  Row(horizontalArrangement=Arrangement.spacedBy(8.dp)) {
   listOf("Meeting","Presentation").forEach { name ->
    FilterChip(selected=category==name,onClick={category=name;index=0;choice=-1;checked=false},label={Text(name)})
   }
  }
  Text("Question ${index+1} of ${items.size} · $category")
  Text(item.situation,style=MaterialTheme.typography.titleMedium)
  item.options.forEachIndexed { i,answer ->
   OutlinedButton(onClick={choice=i;checked=false},modifier=Modifier.fillMaxWidth().heightIn(min=48.dp)) {
    Text("${if(choice==i) "●" else "○"} ${i+1}. $answer",modifier=Modifier.fillMaxWidth())
   }
  }
  Button(onClick={checked=true;if(choice==item.correct)onVerified(item.id)},enabled=choice>=0,modifier=Modifier.fillMaxWidth()){Text("Check reply")}
  if(checked){Text(if(choice==item.correct)"Suitable response!" else "Consider a clearer response.");Text(item.explanation)}
  Text("Optional speaking or writing: ${item.speakingPrompt}",color=MaterialTheme.colorScheme.onSurfaceVariant)
  OutlinedButton(onClick={index=(index+1)%items.size;choice=-1;checked=false},modifier=Modifier.fillMaxWidth()){Text("Next practice →")}
  Text("Preview only: no audio stored, no XP or extra Mint Bloom.")
 }
}
