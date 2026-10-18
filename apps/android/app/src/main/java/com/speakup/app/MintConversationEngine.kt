package com.speakup.app

/**
 * Offline, deterministic conversational speaking practice.
 * It is deliberately NOT described as a live AI model or a clinical/CEFR scoring tool.
 * Future paid providers must be integrated server-side behind a separate approved gateway.
 */
internal enum class MintVoiceMode { SPEAK_TO_COMPLETE, LISTEN_THEN_SPEAK, PLAY_THEN_SPEAK }
internal enum class MintResponseSource { SPEECH, TEXT }
internal enum class MintConversationLevel { BEGINNER, INTERMEDIATE, ADVANCED }
internal enum class MintFeedbackStatus { PRACTISED, NEEDS_RETRY, UNRECOGNIZED }

internal data class MintDialogueTurn(
    val id: String,
    val speaker: String,
    val prompt: String,
    val listeningText: String,
    val suggestedResponse: String,
    val expectedIdeas: Set<String>,
    val hint: String,
    val mode: MintVoiceMode
)
internal data class MintDialogueScenario(
    val id: String,
    val title: String,
    val description: String,
    val level: MintConversationLevel,
    val minAge: Int,
    val turns: List<MintDialogueTurn>
)
internal data class MintVoiceFeedback(
    val status: MintFeedbackStatus,
    val message: String,
    val ideaSuggestion: String,
    val speakingCounted: Boolean,
    val confidenceKnown: Boolean = false
)

internal object MintConversationEngine {
    private val scenes = listOf(
        MintDialogueScenario("meet","Meet a new friend","Say hello and make a friendly introduction.",
            MintConversationLevel.BEGINNER,4,listOf(
                MintDialogueTurn("meet-1","Mia","Hi! What's your name?","Hi! What's your name?","Hello, my name is Alex.",setOf("name","hello"),"Introduce yourself with My name is…",MintVoiceMode.LISTEN_THEN_SPEAK),
                MintDialogueTurn("meet-2","Mia","It's nice to meet you. How are you?","It's nice to meet you. How are you?","Nice to meet you too! I'm doing well.",setOf("nice","well","fine"),"Thank Mia and say how you feel.",MintVoiceMode.SPEAK_TO_COMPLETE),
                MintDialogueTurn("meet-3","Mia","What do you enjoy doing?","What do you enjoy doing?","I enjoy reading and playing games.",setOf("enjoy","like","love"),"Say I enjoy… or I like…",MintVoiceMode.PLAY_THEN_SPEAK)
            )),
        MintDialogueScenario("travel","Find your way","Ask for directions during a journey.",
            MintConversationLevel.BEGINNER,8,listOf(
                MintDialogueTurn("travel-1","Guide","Hello, can I help you?","Hello, can I help you?","Could you tell me where the station is?",setOf("station","where","directions"),"Ask politely where the station is.",MintVoiceMode.LISTEN_THEN_SPEAK),
                MintDialogueTurn("travel-2","Guide","Go straight, then turn left.","Go straight, then turn left.","Thank you for the directions!",setOf("thank"),"Thank the person for their help.",MintVoiceMode.SPEAK_TO_COMPLETE),
                MintDialogueTurn("travel-3","Guide","Do you need anything else?","Do you need anything else?","No, thank you. That's all.",setOf("no","thank"),"End the conversation politely.",MintVoiceMode.PLAY_THEN_SPEAK)
            )),
        MintDialogueScenario("colleagues","Team project","Make suggestions and ask follow-up questions.",
            MintConversationLevel.INTERMEDIATE,13,listOf(
                MintDialogueTurn("team-1","Jordan","We need to finish the project soon. Any ideas?","We need to finish the project soon. Any ideas?","We could divide the tasks and review progress tomorrow.",setOf("could","tasks","suggest"),"Use We could… to offer an idea.",MintVoiceMode.PLAY_THEN_SPEAK),
                MintDialogueTurn("team-2","Jordan","Can you explain your suggestion?","Can you explain your suggestion?","Dividing the work will help us finish more efficiently.",setOf("because","help","efficiently"),"Give a reason with because or will help.",MintVoiceMode.LISTEN_THEN_SPEAK),
                MintDialogueTurn("team-3","Jordan","What if someone needs more time?","What if someone needs more time?","We can adjust the deadline and support one another.",setOf("adjust","support","time"),"Suggest a constructive solution.",MintVoiceMode.SPEAK_TO_COMPLETE)
            )),
        MintDialogueScenario("interview","Job interview","Practise explaining your abilities and experience.",
            MintConversationLevel.INTERMEDIATE,18,listOf(
                MintDialogueTurn("job-1","Interviewer","Tell me about yourself.","Tell me about yourself.","I have experience solving problems and working in teams.",setOf("experience","skills","team"),"Mention a skill and relevant experience.",MintVoiceMode.SPEAK_TO_COMPLETE),
                MintDialogueTurn("job-2","Interviewer","What is one of your strengths?","What is one of your strengths?","I'm a good listener and I communicate clearly.",setOf("strength","good","communication"),"Describe one strength with an example.",MintVoiceMode.LISTEN_THEN_SPEAK),
                MintDialogueTurn("job-3","Interviewer","Do you have any questions for us?","Do you have any questions for us?","Could you tell me about the team's priorities?",setOf("could","team","priorities"),"Ask a relevant and respectful question.",MintVoiceMode.PLAY_THEN_SPEAK)
            )),
        MintDialogueScenario("debate","Evidence and opinions","Disagree respectfully using nuance.",
            MintConversationLevel.ADVANCED,16,listOf(
                MintDialogueTurn("debate-1","Avery","This study proves the approach always works.","This study proves the approach always works.","The findings are promising; however, they may not apply in every context.",setOf("however","evidence","context"),"Recognize possible limitations of the evidence.",MintVoiceMode.LISTEN_THEN_SPEAK),
                MintDialogueTurn("debate-2","Avery","What additional evidence would you want?","What additional evidence would you want?","I'd examine larger samples and compare outcomes over time.",setOf("samples","compare","data"),"Name the evidence you would seek.",MintVoiceMode.SPEAK_TO_COMPLETE),
                MintDialogueTurn("debate-3","Avery","Can you summarize your position?","Can you summarize your position?","The evidence suggests potential benefits but does not establish causation.",setOf("suggests","causation","benefits"),"State a balanced conclusion.",MintVoiceMode.PLAY_THEN_SPEAK)
            )),
        MintDialogueScenario("presentation","Explain a proposal","Communicate a professional recommendation.",
            MintConversationLevel.ADVANCED,18,listOf(
                MintDialogueTurn("presentation-1","Director","What is your recommendation?","What is your recommendation?","I recommend a phased rollout to reduce operational risk.",setOf("recommend","phased","risk"),"Explain your proposed approach.",MintVoiceMode.SPEAK_TO_COMPLETE),
                MintDialogueTurn("presentation-2","Director","What is the main concern?","What is the main concern?","The primary concern is reliability during peak demand.",setOf("concern","reliability","demand"),"Identify a concrete risk.",MintVoiceMode.LISTEN_THEN_SPEAK),
                MintDialogueTurn("presentation-3","Director","How would you address that concern?","How would you address that concern?","We could measure performance and adjust the rollout based on results.",setOf("measure","adjust","results"),"Propose a way to verify progress.",MintVoiceMode.PLAY_THEN_SPEAK)
            ))
    )

    fun scenarios(level: MintConversationLevel, learnerAge: Int): List<MintDialogueScenario> =
        scenes.filter { it.level == level && learnerAge >= it.minAge }

    fun all(): List<MintDialogueScenario> = scenes

    fun recommendedLevel(difficulty: String): MintConversationLevel = when(difficulty) {
        "Hard" -> MintConversationLevel.ADVANCED
        "Medium" -> MintConversationLevel.INTERMEDIATE
        else -> MintConversationLevel.BEGINNER
    }

    fun evaluate(turn: MintDialogueTurn, response: String, source: MintResponseSource): MintVoiceFeedback {
        val words = Regex("[a-z']+").findAll(response.lowercase()).map { it.value }.toSet()
        val isSpeech = source == MintResponseSource.SPEECH
        if (words.isEmpty()) return MintVoiceFeedback(MintFeedbackStatus.UNRECOGNIZED,
            "No clear words were available. Try speaking again or use quiet text practice.",
            turn.hint, false)
        val containsIdea = turn.expectedIdeas.any { idea -> idea in words }
        val substantive = words.size >= 3
        val status = if (substantive) MintFeedbackStatus.PRACTISED else MintFeedbackStatus.NEEDS_RETRY
        val message = when {
            !substantive -> "Try a complete short sentence. You can rehearse at your own pace."
            containsIdea -> "You expressed a relevant idea. Try another natural way to say it."
            else -> "Thanks for practising. Your wording may also work; review the example if it helps."
        }
        return MintVoiceFeedback(status, message, if (containsIdea) turn.suggestedResponse else turn.hint,
            isSpeech && substantive, confidenceKnown = false)
    }
}
