package com.speakup.app

/** Original, local professional communication practice. No accredited certifications. */
internal data class MintProfessionalPractice(
    val id: String, val industry: String, val situation: String, val prompt: String,
    val answers: List<String>, val correctIndex: Int, val explanation: String
)
internal object MintProfessionalPilot {
    val items = listOf(
        MintProfessionalPractice("business-status","Business","Team update","Your manager asks about progress. Which response is clear?",
            listOf("The report is almost ready; I will send it by 3 PM.","No update.","You should already know."),
            0,"Give a concise status update and a realistic next step."),
        MintProfessionalPractice("tech-bug","IT & Technology","Bug report","How do you report a repeatable application error?",
            listOf("The app is bad.","I can reproduce the crash after tapping Save on Android 13.","Someone must fix everything."),
            1,"Describe the observed failure, steps and environment without guessing the root cause."),
        MintProfessionalPractice("service-empathy","Customer Service","Customer complaint","A customer says their order arrived late. What should you say?",
            listOf("It is not my concern.","You are wrong to complain.","I'm sorry for the delay. Let me check the order status."),
            2,"Acknowledge the concern and offer a specific helpful action."),
        MintProfessionalPractice("hospitality-directions","Hospitality","Guest request","A guest asks where breakfast is served. Choose a helpful reply.",
            listOf("Breakfast is served on the ground floor from 7 AM.","Find it yourself.","I don't give information."),
            0,"Give clear, relevant directions and times without unnecessary details.")
    )
    fun valid(item: MintProfessionalPractice) = item.id.isNotBlank() && item.industry.isNotBlank() &&
        item.prompt.isNotBlank() && item.answers.size == 3 && item.answers.all { it.isNotBlank() } &&
        item.answers.distinct().size == 3 && item.correctIndex in 0..2 && item.explanation.isNotBlank()
}
