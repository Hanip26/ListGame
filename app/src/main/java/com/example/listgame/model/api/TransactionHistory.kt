package com.example.listgame.model.api

data class TransactionHistory(
    val invoice_id: String,
    val username: String,
    val game_name: String,
    val player_id: String?,
    val amount: String,
    val price: Int,
    val payment_method: String?,
    val status: String
)