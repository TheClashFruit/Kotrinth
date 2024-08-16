package me.theclashfruit.kotrinth.v2.serializables

import kotlinx.serialization.Serializable

@Serializable
data class License(
    val id: String,
    val name: String,
    val url: String? = null
)