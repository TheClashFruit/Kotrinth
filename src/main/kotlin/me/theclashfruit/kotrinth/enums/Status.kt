package me.theclashfruit.kotrinth.enums

enum class Status(val value: String) {
    APPROVED("approved"),
    ARCHIVED("archived"),
    REJECTED("rejected"),
    DRAFT("draft"),
    UNLISTED("unlisted"),
    PROCESSING("processing"),
    WITHHELD("withheld"),
    SCHEDULED("scheduled"),
    PRIVATE("private"),
    UNKNOWN("unknown")
}