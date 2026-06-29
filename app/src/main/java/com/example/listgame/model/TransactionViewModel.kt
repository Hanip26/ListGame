package com.example.listgame.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.listgame.data.TransactionRepository
import kotlinx.coroutines.launch

class TransactionViewModel : ViewModel() {

    private val repository = TransactionRepository()

    fun createTransaction(
        invoiceId: String,
        username: String,
        gameName: String,
        playerId: String,
        amount: String,
        price: Int,
        paymentMethod: String
    ) {
        viewModelScope.launch {
            repository.createTransaction(
                invoiceId, username, gameName, playerId, amount, price, paymentMethod
            )
        }
    }
}
