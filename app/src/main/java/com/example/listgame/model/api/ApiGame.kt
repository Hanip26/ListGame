package com.example.listgame.model.api

data class ApiGame(
    val id: Int,
    val title: String,
    val developer: String,
    val description: String,
    val image_url: String,
    val category: String
)