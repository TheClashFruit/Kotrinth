package me.theclashfruit.kotrinth.v2.serializables

import kotlinx.serialization.Serializable

@Serializable
data class Gallery(
    val created: String,
    val description: String,
    val featured: Boolean,
    val ordering: Int,
    val title: String,
    val url: String
)