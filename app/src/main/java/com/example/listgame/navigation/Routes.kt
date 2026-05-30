package com.example.listgame.navigation

import androidx.compose.runtime.compositionLocalOf
import androidx.compose.runtime.snapshots.SnapshotStateList

sealed class Route {
    object Login           : Route()
    object Register        : Route()
    object Profile         : Route()
    object Dashboard       : Route()
    object CekTransaksi    : Route()   // ✅ BARU
    object KalkulatorWinRate : Route() // ✅ BARU
    object Login     : Route()
    object Register  : Route()
    object Profile   : Route()
    object Dashboard : Route()

    data class Home(val username: String) : Route()
    data class Detail(val gameId: Int)    : Route()
    data class TopUp(val gameId: Int)     : Route()

    data class OrderConfirmation(
        val gameId: Int, val username: String, val playerId: String,
        val amount: String, val quantity: Int, val paymentName: String,
        val totalPrice: Int, val subtotal: Int, val adminFee: Int,
        val discountAmount: Int, val promoDiscount: Int, val whatsappNumber: String
    ) : Route()

    data class PaymentProgress(
        val gameId: Int, val username: String, val playerId: String,
        val amount: String, val quantity: Int, val paymentName: String,
        val totalPrice: Int, val subtotal: Int, val adminFee: Int
    ) : Route()
}

val LocalBackStack = compositionLocalOf<SnapshotStateList<Route>> {
    error("BackStack belum diinisialisasi")
}