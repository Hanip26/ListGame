package com.example.listgame.model

import kotlinx.serialization.Serializable

@Serializable
data class User(
    val username    : String,
    val email       : String,
    val passwordHash: String,
    val displayName : String,
    val phone       : String = "",
    val bio         : String = "",
    val createdAt   : Long   = System.currentTimeMillis()
)