package me.theclashfruit.kotrinth.v2.serializables.mod

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class UserModData(
    val username: String? = null,
    val name: String? = null,
    val email: String? = null,
    val bio: String? = null,

    @SerialName("payout_data") val payoutData: PayoutModData? = null
)
