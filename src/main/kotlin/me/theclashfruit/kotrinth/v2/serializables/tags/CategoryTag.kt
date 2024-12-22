package me.theclashfruit.kotrinth.v2.serializables.tags

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import me.theclashfruit.kotrinth.enums.ProjectType

@Serializable
data class CategoryTag(
    val icon: String,
    val name: String,
    val header: String,

    @SerialName("project_type") val projectType: ProjectType
)
