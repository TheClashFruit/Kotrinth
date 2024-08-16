package me.theclashfruit.kotrinth.enums

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
enum class MonetizationStatus(private val value: String) {
    @SerialName("monetized")         MONETIZED("monetized"),
    @SerialName("demonetized")       DEMONETIZED("demonetized"),
    @SerialName("force-demonetized") FORCE_DEMONETIZED("force-demonetized");

    override fun toString(): String {
        return value
    }
}