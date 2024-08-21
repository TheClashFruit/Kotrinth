package me.theclashfruit.kotrinth.enums

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
enum class Wallet(private val value: String) {
    @SerialName("paypal") PayPal("paypal"),
    @SerialName("venmo")  Venmo("venmo");

    override fun toString(): String {
        return value
    }
}