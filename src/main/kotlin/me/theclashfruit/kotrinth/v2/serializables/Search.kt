package me.theclashfruit.kotrinth.v2.serializables

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class Search(
    val hits: List<ProjectResult>,
    val limit: Int,
    val offset: Int,

    @SerialName("total_hits") val totalHits: Int
)