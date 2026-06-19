package com.example.listgame.network

import com.example.listgame.model.api.LoginRequest
import com.example.listgame.model.api.LoginResponse
import com.example.listgame.model.api.RegisterRequest
import com.example.listgame.model.api.RegisterResponse
import com.example.listgame.model.api.GamesResponse
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST
import retrofit2.http.GET

interface ApiService {

    @POST("login")
    suspend fun login(
        @Body request: LoginRequest
    ): Response<LoginResponse>

    @POST("register")
    suspend fun register(
        @Body request: RegisterRequest
    ): Response<RegisterResponse>

    @GET("games")
    suspend fun getGames(): Response<GamesResponse>
}