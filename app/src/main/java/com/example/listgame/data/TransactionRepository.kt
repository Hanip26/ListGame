package com.example.listgame.data

import com.example.listgame.network.RetrofitClient
import com.example.listgame.network.TransactionRequest

class TransactionRepository {

    suspend fun createTransaction(
        invoiceId: String,
        username: String,
        gameName: String,
        playerId: String,
        amount: String,
        price: Int,
        paymentMethod: String
    ): Boolean {
        return try {
            val response = RetrofitClient.api.createTransaction(
                TransactionRequest(
                    invoice_id = invoiceId,
                    username = username,
                    game_name = gameName,
                    player_id = playerId,
                    amount = amount,
                    price = price,
                    payment_method = paymentMethod
                )
            )
            response.success
        } catch (e: Exception) {
            false
        }
    }
}