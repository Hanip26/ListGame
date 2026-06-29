package com.example.listgame.model.api

data class TransactionDetailResponse(
    val success: Boolean,
    val transaction: TransactionHistory?,
    val message: String? = null
)