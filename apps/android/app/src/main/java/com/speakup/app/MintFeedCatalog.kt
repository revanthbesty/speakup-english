package com.speakup.app

/**
 * An offline library of original, captioned, animated English micro-stories.
 * These are not licensed third-party videos or user-uploaded content.
 * Full external video streaming requires asset licensing, media infrastructure and review.
 */
internal enum class MintClipLevel { EASY, MEDIUM, HARD }
internal enum class MintClipTheme { EVERYDAY, TRAVEL, SCHOOL, WORK, STORY }
internal data class MintClipFrame(
    val visual: String,
    val caption: String,
    val spokenLine: String
)
internal data class MintClip(
    val id: String,
    val title: String,
    val description: String,
    val creator: String,
    val level: MintClipLevel,
    val minAge: Int,
    val theme: MintClipTheme,
    val frames: List<MintClipFrame>,
    val challengePrompt: String,
    val challengeAnswer: String,
    val choices: List<String>
) {
    fun valid(): Boolean = id.matches(Regex("[a-z0-9-]+")) &&
        title.isNotBlank() && frames.size in 2..6 &&
        frames.all { it.visual.isNotBlank() && it.caption.isNotBlank() && it.spokenLine.isNotBlank() } &&
        creator == "English Mint Studio" && choices.size == 3 &&
        choices.distinct().size == 3 && challengeAnswer in choices &&
        challengePrompt.isNotBlank()
}
internal object MintFeedCatalog {
    /** 12 original short illustrated-caption sequences; no remote video claims. */
    val clips = listOf(
        MintClip("greet-neighbor","A friendly hello","Start a conversation kindly.","English Mint Studio",MintClipLevel.EASY,4,MintClipTheme.EVERYDAY,
            listOf(MintClipFrame("🏡","Mia sees a new neighbor.","I see someone new next door."),
                MintClipFrame("👋","Mia waves and smiles.","Hello! My name is Mia."),
                MintClipFrame("😊","They say hello together.","Nice to meet you!")),
            "What can Mia say when meeting someone?","Nice to meet you!",listOf("Nice to meet you!","Go away now.","Yesterday Tuesday.")),
        MintClip("morning-routine","A fresh morning","Describe a daily routine.","English Mint Studio",MintClipLevel.EASY,4,MintClipTheme.EVERYDAY,
            listOf(MintClipFrame("🌞","The sun rises.","It's a bright morning."),
                MintClipFrame("🪥","A child brushes their teeth.","I brush my teeth every morning."),
                MintClipFrame("🎒","Time to leave for school.","I'm ready for the day!")),
            "Which sentence describes a daily habit?","I brush my teeth every morning.",listOf("I brush my teeth every morning.","Yesterday I brushing.","Tomorrow I brushed.")),
        MintClip("lost-puppy","A little helper","Practise kind requests.","English Mint Studio",MintClipLevel.EASY,4,MintClipTheme.STORY,
            listOf(MintClipFrame("🐕","A puppy looks lost.","This puppy needs help."),
                MintClipFrame("🧑","A helper arrives.","Can I help you find your home?"),
                MintClipFrame("🏠","The puppy gets home safely.","We're glad you're home!")),
            "What is a polite offer to help?","Can I help you?",listOf("Can I help you?","Don't speak.","Never go home.")),
        MintClip("fruit-market","At the fruit market","Practise shopping language.","English Mint Studio",MintClipLevel.EASY,8,MintClipTheme.EVERYDAY,
            listOf(MintClipFrame("🍎","Sam sees fresh apples.","These apples look delicious."),
                MintClipFrame("🛍️","Sam asks the seller.","May I have two apples, please?"),
                MintClipFrame("💚","The seller replies.","Of course. Here you go!")),
            "Which request is polite?","May I have two apples, please?",listOf("May I have two apples, please?","Apples give now.","You must buy me fruit.")),
        MintClip("train-station","Find the station","Ask for help with directions.","English Mint Studio",MintClipLevel.MEDIUM,8,MintClipTheme.TRAVEL,
            listOf(MintClipFrame("🚉","A traveler checks a map.","I'm trying to find the station."),
                MintClipFrame("🗺️","A local explains the route.","Go straight and turn right."),
                MintClipFrame("🙏","The traveler thanks them.","Thank you for your help!")),
            "What should you say after someone helps?","Thank you for your help!",listOf("Thank you for your help!","You are too slow.","Never mind your directions.")),
        MintClip("library-visit","The quiet library","Borrow books politely.","English Mint Studio",MintClipLevel.MEDIUM,8,MintClipTheme.SCHOOL,
            listOf(MintClipFrame("📚","A student finds an interesting book.","I would like to borrow this book."),
                MintClipFrame("🤫","The librarian replies quietly.","You can borrow it for two weeks."),
                MintClipFrame("📖","The student starts reading.","I'll return it on time.")),
            "What does borrow mean?","Use something and return it.",listOf("Use something and return it.","Sell it forever.","Throw it away.")),
        MintClip("rainy-plan","A change of plans","Practise making alternatives.","English Mint Studio",MintClipLevel.MEDIUM,8,MintClipTheme.STORY,
            listOf(MintClipFrame("🌧️","The park is wet.","It looks like rain."),
                MintClipFrame("🏠","Friends consider a new activity.","Shall we play a game indoors instead?"),
                MintClipFrame("🎲","They enjoy their new plan.","That's a great idea!")),
            "What does instead introduce?","An alternative plan",listOf("An alternative plan","A past mistake","An instruction to hurry")),
        MintClip("school-project","A team project","Make clear suggestions.","English Mint Studio",MintClipLevel.MEDIUM,13,MintClipTheme.SCHOOL,
            listOf(MintClipFrame("🧩","A group discusses their assignment.","Let's divide the tasks."),
                MintClipFrame("📝","Each person selects a part.","I can write the introduction."),
                MintClipFrame("🤝","The group agrees.","We'll review our work together.")),
            "Which phrase offers a suggestion?","Let's divide the tasks.",listOf("Let's divide the tasks.","You are all wrong.","Nothing matters.")),
        MintClip("job-interview","The interview","Use confident professional English.","English Mint Studio",MintClipLevel.HARD,18,MintClipTheme.WORK,
            listOf(MintClipFrame("👔","The interviewer welcomes a candidate.","Thank you for joining us."),
                MintClipFrame("💬","The candidate describes their work.","I have experience leading projects."),
                MintClipFrame("🤝","The interview finishes.","I appreciate the opportunity.")),
            "Which phrase sounds professional?","I appreciate the opportunity.",listOf("I appreciate the opportunity.","Give me this job.","Hurry up already.")),
        MintClip("project-update","A precise update","Communicate progress and risks.","English Mint Studio",MintClipLevel.HARD,18,MintClipTheme.WORK,
            listOf(MintClipFrame("📊","The team reviews progress.","We've completed the initial milestones."),
                MintClipFrame("⚠️","One risk remains.","However, deployment still requires testing."),
                MintClipFrame("✅","The lead proposes a solution.","Let's verify the results before launch.")),
            "What does however indicate?","A contrast",listOf("A contrast","A list of examples","The final conclusion")),
        MintClip("evidence-debate","Question the evidence","Disagree without dismissing.","English Mint Studio",MintClipLevel.HARD,16,MintClipTheme.SCHOOL,
            listOf(MintClipFrame("📚","Two students compare their sources.","Our studies reach different conclusions."),
                MintClipFrame("🔎","One student notices a limitation.","The sample may not represent everyone."),
                MintClipFrame("💡","They suggest a careful claim.","We should avoid overstating the evidence.")),
            "Why use may in an argument?","To express uncertainty responsibly",listOf("To express uncertainty responsibly","To promise certainty","To avoid giving reasons")),
        MintClip("community-garden","A thoughtful proposal","Describe benefits and constraints.","English Mint Studio",MintClipLevel.HARD,16,MintClipTheme.STORY,
            listOf(MintClipFrame("🌿","Residents propose a garden.","A shared garden could benefit our neighborhood."),
                MintClipFrame("💧","They consider the resources needed.","We'll need reliable access to water."),
                MintClipFrame("🌱","The group plans a trial.","A small pilot may be a practical start.")),
            "Which wording communicates a cautious proposal?","A small pilot may be a practical start.",listOf("A small pilot may be a practical start.","This will solve everything.","There are no possible challenges."))
    )

    fun visible(age: Int, level: MintClipLevel, blockedIds: Set<String>): List<MintClip> =
        clips.filter { it.minAge <= age && it.level == level && it.id !in blockedIds }

    fun validCatalog(): Boolean = clips.map { it.id }.toSet().size == clips.size && clips.all { it.valid() }
}
