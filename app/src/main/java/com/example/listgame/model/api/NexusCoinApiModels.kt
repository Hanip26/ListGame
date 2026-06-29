package com.example.listgame.model.api

data class NexusCoinBalanceResponse(
    val success: Boolean,
    val balance: Int
)

data class NexusCoinHistoryResponse(
    val success: Boolean,
    val transactions: List<NexusCoinApiTransaction>
)

data class NexusCoinApiTransaction(
    val id: Int,
    val username: String,
    val invoice_id: String,
    val denom_label: String,
    val coin_value: Int,
    val payment_name: String,
    val total_price: Int,
    val status: String,
    val created_at: String
)

data class NexusCoinAddRequest(
    val username: String,
    val invoice_id: String,
    val denom_label: String,
    val coin_value: Int,
    val payment_name: String,
    val total_price: Int
)

data class NexusCoinDeductRequest(
    val username: String,
    val amount: Int
)

data class NexusCoinActionResponse(
    val success: Boolean,
    val balance: Int = 0,
    val message: String? = null
)