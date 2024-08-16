package me.theclashfruit.kotrinth.enums

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
enum class Status(private val value: String) {
    @SerialName("approved")   APPROVED("approved"),
    @SerialName("archived")   ARCHIVED("archived"),
    @SerialName("rejected")   REJECTED("rejected"),
    @SerialName("draft")      DRAFT("draft"),
    @SerialName("unlisted")   UNLISTED("unlisted"),
    @SerialName("processing") PROCESSING("processing"),
    @SerialName("withheld")   WITHHELD("withheld"),
    @SerialName("scheduled")  SCHEDULED("scheduled"),
    @SerialName("private")    PRIVATE("private"),
    @SerialName("unknown")    UNKNOWN("unknown");

    override fun toString(): String {
        return value
    }
}