package com.speakup.app

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
internal fun MintInterviewPrepScreen(onVerified: (String) -> Unit = {}) {
    var index by remember { mutableIntStateOf(0) }
    var choice by remember { mutableIntStateOf(-1) }
    var checked by remember { mutableStateOf(false) }
    val item=MintInterviewPrep.items[index]
    Column(verticalArrangement=Arrangement.spacedBy(10.dp)) {
        MintScreenHeading("Interview Preparation", "Practise confident answers with private, offline activities.", "PROFESSIONAL ENGLISH")
        Text("Four original practice questions, not a certified interview or hiring score.")
        Text("Question ${index+1} of ${MintInterviewPrep.items.size}")
        Text(item.prompt,style=MaterialTheme.typography.titleMedium)
        item.choices.forEachIndexed { i,answer ->
            OutlinedButton(onClick={choice=i;checked=false},modifier=Modifier.fillMaxWidth().heightIn(min=48.dp)) {
                Text("${if(choice==i) "●" else "○"} ${i+1}. $answer",modifier=Modifier.fillMaxWidth())
            }
        }
        Button(onClick={checked=true;if(choice==item.best)onVerified(item.id)},enabled=choice>=0,modifier=Modifier.fillMaxWidth()) {Text("Check reply")}
        if(checked) {
            Text(if(choice==item.best) "Suitable response!" else "This could be improved. Try again.")
            Text(item.explanation)
        }
        Text("Optional personal practice: ${item.followUp}",color=MaterialTheme.colorScheme.onSurfaceVariant)
        OutlinedButton(onClick={
            index=(index+1)%MintInterviewPrep.items.size;choice=-1;checked=false
        },modifier=Modifier.fillMaxWidth()) {Text("Next interview question →")}
        Text("Private practice only; no raw audio, external AI or additional Mint Bloom.")
    }
}
