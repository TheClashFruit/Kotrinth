package me.theclashfruit.kotrinth.v2.serializables

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

import me.theclashfruit.kotrinth.enums.Side
import me.theclashfruit.kotrinth.enums.Status

@Serializable
data class Project(
    val additional_categories: List<String>,
    val approved: String,
    val body: String,

    @Deprecated("Always null, only kept for legacy compatibility.")
    @SerialName("body_url") val bodyUrl: String? = null,

    val categories: List<String>,

    @SerialName("client_side") val clientSide: Side,
    @SerialName("server_side") val serverSide: Side,

    val status: Status,

    val color: Int? = null,
    val description: String,
    val discord_url: String,
    val donation_urls: List<DonationUrl>,
    val downloads: Int,
    val followers: Int,
    val gallery: List<Gallery>,
    val game_versions: List<String>,
    val icon_url: String,
    val id: String,
    val issues_url: String,
    val license: License,
    val loaders: List<String>,

    @Deprecated("Use `threadId` instead.")
    @SerialName("moderator_message") val moderatorMessage: ModeratorMessage? = null,

    val monetization_status: String,
    val project_type: String,
    val published: String,
    val queued: String,
    val requested_status: String,
    val slug: String,
    val source_url: String,
    val team: String,
    val thread_id: String,
    val title: String,
    val updated: String,
    val versions: List<String>,
    val wiki_url: String
)