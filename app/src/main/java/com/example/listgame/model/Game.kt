package com.example.listgame.model

data class Game(
    val id: Int,
    val title: String,
    val developer: String,
    val description: String,
    val rating: Double,
    val size: String,
    val genres: List<String>,
    val latestUpdate: String,
    val imageRes: Int
)