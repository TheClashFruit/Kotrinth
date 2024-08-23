package me.theclashfruit.kotrinth.v2.serializables

import kotlinx.serialization.Serializable

@Serializable
data class Gallery(
    val created: String,
    val description: String? = null,
    val featured: Boolean,
    val ordering: Long, // wtf modrinth why is this a 64-bit integer???
    val title: String? = null,
    val url: String
)