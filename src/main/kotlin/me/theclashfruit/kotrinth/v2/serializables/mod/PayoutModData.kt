package me.theclashfruit.kotrinth.v2.serializables.mod

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import me.theclashfruit.kotrinth.enums.Wallet
import me.theclashfruit.kotrinth.enums.WalletType

@Serializable
data class PayoutModData(
    @SerialName("payout_address")     val payoutAddress: Float? = null,
    @SerialName("payout_wallet")      val payoutWallet: Wallet? = null,
    @SerialName("payout_wallet_type") val payoutWalletType: WalletType? = null,
)