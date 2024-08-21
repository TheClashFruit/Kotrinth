package me.theclashfruit.kotrinth.enums

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
enum class WalletType(private val value: String) {
    @SerialName("email")       Email("email"),
    @SerialName("phone")       Phone("phone"),
    @SerialName("user_handle") UserHandle("user_handle");

    override fun toString(): String {
        return value
    }
}