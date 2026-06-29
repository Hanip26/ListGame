package com.example.listgame.model.api

data class HistoryResponse(
    val success: Boolean,
    val transactions: List<TransactionHistory>
)