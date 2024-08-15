package me.theclashfruit.kotrinth.v2.serializables

import kotlinx.serialization.Serializable

@Serializable
data class ModeratorMessage(
    val message: String,
    val body: String? = null,
)
