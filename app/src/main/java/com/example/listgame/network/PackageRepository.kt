package com.example.listgame.network

import retrofit2.Response
import com.example.listgame.model.api.PackageResponse

class PackageRepository {

    suspend fun getPackages(
        gameId: Int
    ): Response<PackageResponse> {

        return RetrofitClient
            .api
            .getPackages(gameId)
    }
}