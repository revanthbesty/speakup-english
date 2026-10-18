package com.speakup.app

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
internal fun MintBusinessWritingScreen(onVerified: (String) -> Unit = {}) {
 var topic by remember { mutableStateOf("Email") }
 var index by remember { mutableIntStateOf(0) }
 var selected by remember { mutableIntStateOf(-1) }
 var checked by remember { mutableStateOf(false) }
 var draft by remember { mutableStateOf("") }
 val items=MintBusinessWriting.items.filter{it.topic==topic}
 val item=items[index]
 Column(verticalArrangement=Arrangement.spacedBy(10.dp)) {
  MintScreenHeading("Email & Business Writing", "Practise professional written communication privately.", "PROFESSIONAL ENGLISH")
  Text("Practise clear written English. Your optional draft stays on this screen and is not sent or automatically graded.")
  Row(horizontalArrangement=Arrangement.spacedBy(8.dp)) {
   listOf("Email","Business Writing").forEach { name ->
    FilterChip(selected=topic==name,onClick={topic=name;index=0;selected=-1;checked=false;draft=""},label={Text(name)})
   }
  }
  Text("Question ${index+1} of ${items.size}")
  Text(item.context,style=MaterialTheme.typography.titleMedium)
  item.options.forEachIndexed { i,answer ->
   OutlinedButton(onClick={selected=i;checked=false},modifier=Modifier.fillMaxWidth().heightIn(min=48.dp)) {
    Text("${if(selected==i) "●" else "○"} ${i+1}. $answer",modifier=Modifier.fillMaxWidth())
   }
  }
  Button(onClick={checked=true;if(selected==item.best)onVerified(item.id)},enabled=selected>=0,modifier=Modifier.fillMaxWidth()){Text("Check answer")}
  if(checked) { Text(if(selected==item.best)"Suitable response!" else "Try a clearer choice.");Text(item.explanation) }
  Text(item.draftPrompt,color=MaterialTheme.colorScheme.onSurfaceVariant)
  OutlinedTextField(value=draft,onValueChange={draft=it.take(600)},label={Text("Optional private writing practice")},modifier=Modifier.fillMaxWidth(),minLines=2)
  Text("Your draft is not graded, saved or shared.",style=MaterialTheme.typography.bodySmall)
  OutlinedButton(onClick={index=(index+1)%items.size;selected=-1;checked=false;draft=""},modifier=Modifier.fillMaxWidth()){Text("Next writing exercise →")}
 }
}
