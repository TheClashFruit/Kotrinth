package me.theclashfruit.kotrinth.v2.serializables.tags

import kotlinx.serialization.Serializable

@Serializable
data class GenericTag(
    val short: String,
    val name: String
)
