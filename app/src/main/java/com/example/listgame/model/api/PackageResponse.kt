package com.example.listgame.model.api

data class PackageResponse(
    val success: Boolean,
    val packages: List<ApiPackage>
)