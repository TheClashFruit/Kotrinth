package me.theclashfruit.kotrinth.enums

import kotlinx.serialization.Serializable
import me.theclashfruit.kotrinth.utils.BadgeSerializable

enum class Badge(val value: Int) {
    Plus(1 shl 0),
    EarlyModPackAdopter(1 shl 1),
    EarlyResourcePackAdopter(1 shl 2),
    EarlyPluginAdopter(1 shl 3),
    AlphaTester(1 shl 4),
    Contributor(1 shl 5),
    Translator(1 shl 6);
}