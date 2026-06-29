package com.example.listgame.model.api

data class ApiPackage(
    val id: Int,
    val game_id: Int,
    val amount: String,
    val price: Int,
    val bonus: String?
)