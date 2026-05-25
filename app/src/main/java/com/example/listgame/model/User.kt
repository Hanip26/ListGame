package com.example.listgame.model

import kotlinx.serialization.Serializable

@Serializable
data class User(
    val username    : String,           // unik, primary key
    val email       : String,           // unik
    val passwordHash: String,           // SHA-256, tidak pernah plaintext
    val displayName : String,           // nama tampilan bebas
    val phone       : String = "",      // nomor HP (opsional)
    val bio         : String = "",      // bio singkat (opsional)
    val createdAt   : Long   = System.currentTimeMillis()
)