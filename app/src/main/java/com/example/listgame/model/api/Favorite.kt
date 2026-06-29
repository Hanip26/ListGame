package com.example.listgame.model.api

data class Favorite(
    val id: Int,
    val username: String,
    val game_id: Int
)

data class FavoriteListResponse(
    val success: Boolean,
    val favorites: List<Favorite>
)

data class FavoriteToggleRequest(
    val username: String,
    val game_id: Int
)

data class FavoriteToggleResponse(
    val success: Boolean,
    val is_favorite: Boolean,
    val message: String
)