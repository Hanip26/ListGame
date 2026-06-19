package com.example.listgame.network

class GameRepository {

    suspend fun getGames() =
        RetrofitClient.api.getGames()
}