package me.theclashfruit.kotrinth.enums

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
enum class Role(private val value: String) {
    @SerialName("admin") Admin("admin"),
    @SerialName("moderator") Moderator("moderator"),
    @SerialName("developer") Developer("developer");

    override fun toString(): String {
        return value
    }
}