package me.theclashfruit.kotrinth.enums

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
enum class ProjectType(private val value: String) {
    @SerialName("project")      Project("project"), // couldn't have been like "unknown" or something smh
    @SerialName("mod")          Mod("mod"),
    @SerialName("modpack")      ModPack("modpack"),
    @SerialName("resourcepack") ResourcePack("resourcepack"),
    @SerialName("shader")       Shader("shader");

    override fun toString(): String {
        return value
    }
}