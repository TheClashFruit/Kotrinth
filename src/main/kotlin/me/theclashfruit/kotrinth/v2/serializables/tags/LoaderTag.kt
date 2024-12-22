package me.theclashfruit.kotrinth.v2.serializables.tags

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import me.theclashfruit.kotrinth.enums.ProjectType

@Serializable
data class LoaderTag(
    val icon: String,
    val name: String,

    @SerialName("supported_project_types") val supportedProjectTypes: List<ProjectType>
)
