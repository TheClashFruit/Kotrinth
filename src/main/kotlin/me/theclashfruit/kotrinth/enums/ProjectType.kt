package me.theclashfruit.kotrinth.enums

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
enum class ProjectType(private val value: String) {
    @SerialName("project")      PROJECT("project"), // couldn't have been like "unknown" or something smh
    @SerialName("mod")          MOD("mod"),
    @SerialName("modpack")      MODPACK("modpack"),
    @SerialName("resourcepack") RESOURCEPACK("resourcepack"),
    @SerialName("shader")       SHADER("shader");

    override fun toString(): String {
        return value
    }
}