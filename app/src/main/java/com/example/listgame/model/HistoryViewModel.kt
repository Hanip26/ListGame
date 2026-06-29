package com.example.listgame.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.listgame.model.api.TransactionHistory
import com.example.listgame.network.HistoryRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class HistoryViewModel : ViewModel() {

    private val repository = HistoryRepository()

    private val _history =
        MutableStateFlow<List<TransactionHistory>>(emptyList())

    val history: StateFlow<List<TransactionHistory>>
        get() = _history

    fun loadHistory() {

        viewModelScope.launch {

            val response =
                repository.getHistory()

            if (response.isSuccessful) {

                _history.value =
                    response.body()?.transactions
                        ?: emptyList()
            }
        }
    }
}