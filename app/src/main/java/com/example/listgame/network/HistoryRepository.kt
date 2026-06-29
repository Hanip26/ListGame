package com.example.listgame.network

class HistoryRepository {

    suspend fun getHistory() =
        RetrofitClient.api.getTransactions()

    suspend fun getTransactionByInvoice(invoiceId: String) =
        RetrofitClient.api.getTransactionByInvoice(invoiceId)
}