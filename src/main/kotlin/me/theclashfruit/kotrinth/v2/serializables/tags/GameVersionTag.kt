package me.theclashfruit.kotrinth.v2.serializables.tags

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import me.theclashfruit.kotrinth.enums.ProjectType

@Serializable
data class GameVersionTag(
    val version: String,

    // TODO: Enum (release, snapshot, alpha, beta)
    @SerialName("version_type") val versionType: String,

    val date: String,
    val major: Boolean
)
