package com.speakup.app

/** Original, offline story data. No remote AI, image license or download is required. */
internal data class MintFiveWordScene(val illustration: String, val caption: String, val narration: String)
internal data class MintFiveWordPack(
    val id: String,
    val title: String,
    val words: List<String>,
    val scenes: List<MintFiveWordScene>,
    val question: String,
    val choices: List<String>,
    val correctAnswer: String,
    val minAge: Int = 4
) {
    fun valid() = id.matches(Regex("[a-z0-9-]+")) &&
        title.isNotBlank() && words.size == 5 &&
        words.all { it.matches(Regex("[a-zA-Z]+")) } &&
        words.map { it.lowercase() }.distinct().size == 5 &&
        scenes.size in 4..8 && scenes.all {
            it.illustration.isNotBlank() && it.caption.isNotBlank() && it.narration.isNotBlank()
        } && choices.size == 3 && choices.distinct().size == 3 &&
        correctAnswer in choices && question.isNotBlank() && minAge >= 4
}

internal object MintFiveWordCatalog {
    val packs = listOf(
        MintFiveWordPack(
            "lost-basket", "The Lost Basket",
            listOf("market", "basket", "fresh", "buy", "happy"),
            listOf(
                MintFiveWordScene("🛒", "Mia walks to the market.", "Mia visits the market in the morning."),
                MintFiveWordScene("🧺", "Her basket is missing!", "Oh no! Where is my basket?"),
                MintFiveWordScene("🍎", "She sees fresh apples.", "These apples look fresh."),
                MintFiveWordScene("💬", "Mia asks to buy some fruit.", "May I buy two apples, please?"),
                MintFiveWordScene("😊", "The seller finds her basket.", "Here's your basket! I'm so happy.")
            ),
            "Where did Mia go?", listOf("The market", "The airport", "The cinema"), "The market"
        ),
        MintFiveWordPack(
            "rainy-day", "A Rainy Day",
            listOf("rain", "umbrella", "street", "friend", "home"),
            listOf(
                MintFiveWordScene("🌧️", "Rain begins to fall.", "Look, it's starting to rain."),
                MintFiveWordScene("☂️", "Sam opens an umbrella.", "Let's use this umbrella."),
                MintFiveWordScene("🚶", "Sam crosses the street safely.", "We can cross at the crossing."),
                MintFiveWordScene("🤝", "Sam helps a friend.", "Come under my umbrella, friend."),
                MintFiveWordScene("🏠", "They arrive home.", "We made it home and stayed dry.")
            ),
            "What helped them stay dry?", listOf("An umbrella", "A book", "A bicycle"), "An umbrella"
        )
    )
    fun visible(age: Int) = packs.filter { it.minAge <= age && it.valid() }
}
