package me.theclashfruit.kotrinth.utils

import kotlinx.serialization.KSerializer
import kotlinx.serialization.descriptors.PrimitiveKind
import kotlinx.serialization.descriptors.PrimitiveSerialDescriptor
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder
import me.theclashfruit.kotrinth.enums.Badge

class BadgeSerializable : KSerializer<List<Badge>> {
    override val descriptor: SerialDescriptor = PrimitiveSerialDescriptor("Badge", PrimitiveKind.INT)

    override fun serialize(encoder: Encoder, value: List<Badge>) {
        var bitField = 0;

        value.forEach {
            bitField = bitField or (1 shl it.ordinal)
        }

        encoder.encodeInt(bitField)
    }

    override fun deserialize(decoder: Decoder): List<Badge> {
        val badges   = mutableListOf<Badge>()
        val bitField = decoder.decodeInt()

        if (hasBadge(bitField, Badge.Plus)) {
            badges.add(Badge.Plus)
        }

        if (hasBadge(bitField, Badge.EarlyModPackAdopter)) {
            badges.add(Badge.EarlyModPackAdopter)
        }

        if (hasBadge(bitField, Badge.EarlyResourcePackAdopter)) {
            badges.add(Badge.EarlyResourcePackAdopter)
        }

        if (hasBadge(bitField, Badge.EarlyPluginAdopter)) {
            badges.add(Badge.EarlyPluginAdopter)
        }

        if (hasBadge(bitField, Badge.AlphaTester)) {
            badges.add(Badge.AlphaTester)
        }

        if (hasBadge(bitField, Badge.Contributor)) {
            badges.add(Badge.Contributor)
        }

        if (hasBadge(bitField, Badge.Translator)) {
            badges.add(Badge.Translator)
        }

        return badges
    }

    private fun hasBadge(badges: Int, badge: Badge): Boolean {
        return (badges and badge.value) != 0
    }
}