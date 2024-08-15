package me.theclashfruit.kotrinth.utils

import kotlinx.serialization.Serializable

@Serializable
data class ApiError(
    val error: String,
    val description: String
)
