package com.example.listgame.model.api

data class GamesResponse(
    val success: Boolean,
    val games: List<ApiGame>
)