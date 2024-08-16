package me.theclashfruit.kotrinth.enums

enum class Sort(private val value: String) {
    RELEVANCE("relevance"),
    DOWNLOADS("downloads"),
    FOLLOWS("follows"),
    NEWEST("newest"),
    UPDATED("updated");

    override fun toString(): String {
        return value
    }
}