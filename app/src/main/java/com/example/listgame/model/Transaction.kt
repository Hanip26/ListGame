package com.example.listgame.model

import kotlinx.serialization.Serializable

@Serializable
data class Transaction(
    val invoiceId  : String,   // "INV-<timestamp>-<gameId>"
    val gameTitle  : String,
    val gameId     : Int,
    val playerId   : String,
    val item       : String,   // "86 Diamond (x1)"
    val payment    : String,
    val totalPrice : Int,
    val status     : TransactionStatus,
    val createdAt  : Long = System.currentTimeMillis()
)

@Serializable
enum class TransactionStatus {
    PENDING,      // Menunggu
    PROCESSING,   // Dalam Proses
    SUCCESS,      // Sukses
    FAILED        // Gagal
}