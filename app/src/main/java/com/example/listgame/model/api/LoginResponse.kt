package com.example.listgame.model.api

data class LoginResponse(
    val success: Boolean,
    val message: String,
    val user: ApiUser?
)