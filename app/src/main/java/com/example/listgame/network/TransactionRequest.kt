package com.example.listgame.network

data class TransactionRequest(
    val invoice_id: String,
    val username: String,
    val game_name: String,
    val player_id: String,
    val amount: String,
    val price: Int,
    val payment_method: String
)