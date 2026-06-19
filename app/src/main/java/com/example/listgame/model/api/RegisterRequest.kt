package com.example.listgame.model.api

data class RegisterRequest(
    val username: String,
    val email: String,
    val password: String,
    val display_name: String
)