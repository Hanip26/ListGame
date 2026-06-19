package com.example.listgame.model

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.listgame.model.api.ApiGame
import com.example.listgame.network.GameRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class GameApiViewModel : ViewModel() {

    private val repository = GameRepository()

    private val _games =
        MutableStateFlow<List<ApiGame>>(emptyList())

    val games: StateFlow<List<ApiGame>>
        get() = _games

    fun loadGames() {

        viewModelScope.launch {

            val response =
                repository.getGames()

            if(response.isSuccessful){

                _games.value =
                    response.body()?.games ?: emptyList()
            }
        }
    }
}