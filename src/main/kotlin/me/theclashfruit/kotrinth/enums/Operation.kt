package me.theclashfruit.kotrinth.enums

enum class Operation(val value: String) {
    Equal("="),
    Smaller("<"),
    Greater(">"),
    SmallerOrEqual("<="),
    GreaterOrEqual(">="),
    NotEqual("!=");
}