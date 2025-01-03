package me.theclashfruit.kotrinth.v2.serializables

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import me.theclashfruit.kotrinth.enums.Badge
import me.theclashfruit.kotrinth.utils.BadgeSerializable

@Serializable
data class UserData(
    @Serializable(with = BadgeSerializable::class)
    val badges: List<Badge>,

    val bio: String,
    val created: String,
    val id: String,
    val role: String,
    val username: String,

    val email: String? = null,

    @Deprecated("This is no longer public for security reasons and is always null.")
    @SerialName("github_id") val githubId: String? = null,

    val name: String? = null, // why does this still exists lol?

    @SerialName("has_password") val hasPassword: Boolean? = null,
    @SerialName("has_totp") val hasTotp: Boolean? = null,
    @SerialName("payout_data") val payoutData: PayoutData? = null,
    @SerialName("email_verified") val emailVerified: Boolean? = null,
    @SerialName("auth_providers") val authProviders: List<String>? = null,

    @SerialName("avatar_url") val avatarUrl: String
)