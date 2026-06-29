package com.example.listgame.network

import com.example.listgame.model.api.NexusCoinAddRequest
import com.example.listgame.model.api.NexusCoinDeductRequest

class NexusCoinRepository {

    suspend fun getBalance(username: String) =
        RetrofitClient.api.getNexusCoinBalance(username)

    suspend fun getHistory(username: String) =
        RetrofitClient.api.getNexusCoinHistory(username)

    suspend fun addCoins(
        username: String,
        invoiceId: String,
        denomLabel: String,
        coinValue: Int,
        paymentName: String,
        totalPrice: Int
    ) = RetrofitClient.api.addNexusCoin(
        NexusCoinAddRequest(
            username = username,
            invoice_id = invoiceId,
            denom_label = denomLabel,
            coin_value = coinValue,
            payment_name = paymentName,
            total_price = totalPrice
        )
    )

    suspend fun deductCoins(username: String, amount: Int) =
        RetrofitClient.api.deductNexusCoin(
            NexusCoinDeductRequest(username = username, amount = amount)
        )
}