package me.theclashfruit.kotrinth.v2.serializables

import kotlinx.serialization.Serializable

@Serializable
data class DonationUrl(
    val id: String,
    val platform: String,
    val url: String
)