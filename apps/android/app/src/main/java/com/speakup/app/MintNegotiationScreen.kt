package com.speakup.app

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
internal fun MintNegotiationScreen(onVerified: (String) -> Unit = {}) {
 var category by remember { mutableStateOf("Negotiation") }
 var index by remember { mutableIntStateOf(0) }
 var choice by remember { mutableIntStateOf(-1) }
 var checked by remember { mutableStateOf(false) }
 val items=MintNegotiationPilot.items.filter{it.category==category}
 val item=items[index]
 Column(verticalArrangement=Arrangement.spacedBy(10.dp)) {
  Text("Negotiation & Problem-Solving · offline pilot",style=MaterialTheme.typography.titleLarge)
  Text("Original practice for respectful workplace communication. Not professional certification.")
  Row(horizontalArrangement=Arrangement.spacedBy(8.dp)) {
   listOf("Negotiation","Problem-Solving").forEach { name ->
    FilterChip(selected=category==name,onClick={category=name;index=0;choice=-1;checked=false},label={Text(name)})
   }
  }
  Text("Scenario ${index+1} of ${items.size}")
  Text(item.context,style=MaterialTheme.typography.titleMedium)
  item.choices.forEachIndexed { i,answer ->
   OutlinedButton(onClick={choice=i;checked=false},modifier=Modifier.fillMaxWidth().heightIn(min=48.dp)) {
    Text("${if(choice==i) "●" else "○"} ${i+1}. $answer",modifier=Modifier.fillMaxWidth())
   }
  }
  Button(onClick={checked=true;if(choice==item.best)onVerified(item.id)},enabled=choice>=0,modifier=Modifier.fillMaxWidth()){Text("Check response")}
  if(checked){Text(if(choice==item.best)"Suitable response!" else "Consider a more constructive option.");Text(item.explanation)}
  OutlinedButton(onClick={index=(index+1)%items.size;choice=-1;checked=false},modifier=Modifier.fillMaxWidth()){Text("Next scenario →")}
  Text("No paid AI, workplace messaging, scores or Mint Bloom rewards.")
 }
}
