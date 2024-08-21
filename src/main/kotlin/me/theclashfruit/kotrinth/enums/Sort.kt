package me.theclashfruit.kotrinth.enums

enum class Sort(private val value: String) {
    Relevance("relevance"),
    Downloads("downloads"),
    Follows("follows"),
    Newest("newest"),
    Updated("updated");

    override fun toString(): String {
        return value
    }
}