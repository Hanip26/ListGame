package com.example.listgame.network

import com.example.listgame.model.api.FavoriteToggleRequest

class FavoriteRepository {

    suspend fun getFavorites(username: String) =
        RetrofitClient.api.getFavorites(username)

    suspend fun toggleFavorite(username: String, gameId: Int) =
        RetrofitClient.api.toggleFavorite(
            FavoriteToggleRequest(username = username, game_id = gameId)
        )

    suspend fun clearFavorites(username: String) =
        RetrofitClient.api.clearFavorites(username)
}