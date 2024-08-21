package me.theclashfruit.kotrinth.v2.serializables

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import me.theclashfruit.kotrinth.enums.Wallet
import me.theclashfruit.kotrinth.enums.WalletType

@Serializable
data class PayoutData(
    val balance: Double,

    @SerialName("payout_address")     val payoutAddress: Float? = null,
    @SerialName("payout_wallet")      val payoutWallet: Wallet? = null,
    @SerialName("payout_wallet_type") val payoutWalletType: WalletType? = null,

    @SerialName("paypal_address") val paypalAddress: String? = null,
    @SerialName("paypal_country") val paypalCountry: String? = null,

    @SerialName("venmo_handle") val venmoHandle: String? = null
)