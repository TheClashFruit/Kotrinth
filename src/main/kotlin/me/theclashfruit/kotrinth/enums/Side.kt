package me.theclashfruit.kotrinth.enums

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
enum class Side(private val value: String) {
    @SerialName("required")    REQUIRED("required"),
    @SerialName("optional")    OPTIONAL("optional"),
    @SerialName("unsupported") UNSUPPORTED("unsupported"),
    @SerialName("unknown")     UNKNOWN("unknown");

    override fun toString(): String {
        return value
    }

    fun getName(): String {
        return value
    }
}