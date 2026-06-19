package com.example.listgame.model

import kotlinx.serialization.Serializable

@Serializable
data class NexusCoinTransaction(
    val invoiceId  : String,
    val denomLabel : String,    // "Rp. 50.000"
    val coinValue  : Int,       // jumlah koin yang ditambahkan
    val paymentName: String,    // nama metode pembayaran
    val totalPrice : Int,       // total dibayar (termasuk admin fee)
    val status     : NexusCoinTransactionStatus,
    val createdAt  : Long = System.currentTimeMillis()
)

@Serializable
enum class NexusCoinTransactionStatus {
    SUCCESS,
    FAILED,
    PENDING
}