package com.speakup.app

/** Unofficial, original and offline career/travel practice; never an accredited test. */
internal data class MintCareerQuestion(
    val id: String, val track: String, val difficulty: String, val situation: String,
    val choices: List<String>, val correctIndex: Int, val explanation: String,
    val speakingPrompt: String
)
internal object MintCareerPacks {
    val questions = listOf(
        MintCareerQuestion("interview-intro","Interview","Easy",
            "An interviewer says: Tell me about yourself.",
            listOf("I am a student and I enjoy solving problems.","You must hire me today.","Nothing to say."),
            0,"Introduce yourself briefly and mention a relevant interest or strength.",
            "Introduce yourself and mention one strength."),
        MintCareerQuestion("interview-example","Interview","Medium",
            "An interviewer asks for an example of teamwork.",
            listOf("I always work alone.","I helped organize tasks and checked progress with my teammates.","Teamwork is not important."),
            1,"Give a specific example of what you did and how you collaborated.",
            "Describe a real or fictional teamwork example."),
        MintCareerQuestion("travel-change","Travel","Easy",
            "Your train departure time has changed. What is a helpful question?",
            listOf("I will not listen.","Could you tell me the new departure time, please?","You must stop all trains."),
            1,"Politely ask for the specific information you need.",
            "Ask a station staff member to confirm the new time."),
        MintCareerQuestion("travel-disruption","Travel","Medium",
            "Your connection is delayed. Ask staff for alternative options.",
            listOf("This is all your fault.","I demand a free ticket.","Could you explain which alternatives are available?"),
            2,"Calm, clear questions invite useful information without assuming an entitlement.",
            "Ask about a possible connection."),
        MintCareerQuestion("exam-reading","Unofficial Exam Practice","Medium",
            "Read: 'The library closes at six, except on Fridays when it closes at eight.' When does it close on Friday?",
            listOf("At six","At eight","At nine"),
            1,"Except on Fridays introduces the exception; Friday closing is eight.",
            "Explain the exception in one sentence."),
        MintCareerQuestion("exam-inference","Unofficial Exam Practice","Hard",
            "Read: 'Although sales increased, the team postponed expansion because operating costs remained uncertain.' Why was expansion postponed?",
            listOf("Costs were still uncertain.","Sales had fallen.","The team had no customers."),
            0,"Although contrasts better sales with continuing cost uncertainty.",
            "Summarise the reason for the delay.")
    )
    fun valid(q: MintCareerQuestion): Boolean = q.id.isNotBlank() && q.track.isNotBlank() &&
        q.choices.size == 3 && q.choices.all(String::isNotBlank) &&
        q.choices.map { it.lowercase().trim() }.distinct().size == 3 &&
        q.correctIndex in 0..2 && q.explanation.isNotBlank() && q.speakingPrompt.isNotBlank()
    fun key(q: MintCareerQuestion): String = "career_${q.id}_v1"
    fun result(q: MintCareerQuestion, choice: Int): Boolean = choice == q.correctIndex
}
