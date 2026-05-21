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
    val imageRes: Int,
    val topUpOptions: List<TopUpOption> = emptyList() // ✅ Tambah ini
)

data class TopUpOption(
    val amount: String,       // "86 Diamond"
    val price: String,        // "Rp 19.000"
    val bonus: String = ""    // "Bonus 10%" (opsional)
)