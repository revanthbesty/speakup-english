package com.speakup.app

/** Checks structural quality of the offline authored pilot without claiming editorial approval. */
internal object MintGrammarQuality {
    data class Item(val prompt: String, val options: List<String>, val answer: String, val explanation: String)
    fun issues(item: Item): List<String> = buildList {
        if (item.prompt.isBlank()) add("missing prompt")
        if (item.options.size != 3) add("exactly three options required")
        if (item.options.any { it.isBlank() }) add("blank option")
        if (item.options.map { it.trim().lowercase() }.distinct().size != item.options.size) add("duplicate options")
        if (item.options.count { it == item.answer } != 1) add("exactly one matching answer required")
        if (item.explanation.isBlank()) add("missing explanation")
    }
    fun collectionIssues(items: List<Item>): List<String> =
        items.flatMapIndexed { i, item -> issues(item).map { "Q${i + 1}: $it" } } +
            items.groupBy { it.prompt.trim().lowercase() }.filterValues { it.size > 1 }.keys.map { "duplicate prompt: $it" }
}
