package com.speakup.app
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
class MainActivity: ComponentActivity(){override fun onCreate(savedInstanceState:Bundle?){super.onCreate(savedInstanceState);setContent{SpeakUpApp()}}}
@Composable fun SpeakUpApp(){var screen by remember{mutableStateOf("home")};MaterialTheme{Column(Modifier.fillMaxSize().padding(24.dp)){Text("SpeakUp English",style=MaterialTheme.typography.headlineMedium);Spacer(Modifier.height(20.dp));when(screen){"home"->Home({screen="lesson"});"lesson"->Lesson({screen="speak"});else->Speak({screen="home"})}}}}
@Composable fun Home(next:()->Unit){Text("Good to see you 👋");Text("Breakfast conversations",style=MaterialTheme.typography.headlineSmall);Text("Build a sentence, speak it, get feedback, retry.");Button(onClick=next){Text("Continue lesson")}}
@Composable fun Lesson(next:()->Unit){Text("Use the verb eat",style=MaterialTheme.typography.headlineSmall);Text("WHO → ACTION → WHAT → WHEN");Button(onClick=next){Text("Build & speak")}}
@Composable fun Speak(next:()->Unit){Text("Say: I eat rice for breakfast.",style=MaterialTheme.typography.headlineSmall);Button(onClick=next){Text("Start speaking")};Text("Speech is processed through the shared backend; raw audio is not retained by default.")}
