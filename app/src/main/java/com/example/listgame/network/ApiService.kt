package com.example.listgame.network

import com.example.listgame.model.api.LoginRequest
import com.example.listgame.model.api.LoginResponse
import com.example.listgame.model.api.RegisterRequest
import com.example.listgame.model.api.RegisterResponse
import com.example.listgame.model.api.GamesResponse
import com.example.listgame.model.api.PackageResponse
import com.example.listgame.model.api.HistoryResponse
import com.example.listgame.model.api.TransactionDetailResponse
import com.example.listgame.model.api.FavoriteListResponse
import com.example.listgame.model.api.FavoriteToggleRequest
import com.example.listgame.model.api.FavoriteToggleResponse
import com.example.listgame.model.api.ProfileResponse
import com.example.listgame.model.api.UpdateProfileRequest
import com.example.listgame.model.api.ChangePasswordRequest
import com.example.listgame.model.api.ChangePasswordApiResponse
import com.example.listgame.model.api.CheckUserRequest
import com.example.listgame.model.api.CheckUserResponse
import com.example.listgame.model.api.ResetPasswordApiRequest
import com.example.listgame.model.api.NexusCoinBalanceResponse
import com.example.listgame.model.api.NexusCoinHistoryResponse
import com.example.listgame.model.api.NexusCoinAddRequest
import com.example.listgame.model.api.NexusCoinDeductRequest
import com.example.listgame.model.api.NexusCoinActionResponse
import retrofit2.http.PUT
import retrofit2.http.DELETE
import retrofit2.http.Path
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

    @POST("transactions")
    suspend fun createTransaction(
        @Body request: TransactionRequest
    ): TransactionResponse

    @GET("games")
    suspend fun getGames(): Response<GamesResponse>

    @GET("games/{id}/packages")
    suspend fun getPackages(
        @Path("id") gameId: Int
    ): Response<PackageResponse>
    @GET("transactions")
    suspend fun getTransactions():
            Response<HistoryResponse>

    @GET("transactions/{invoice_id}")
    suspend fun getTransactionByInvoice(
        @Path("invoice_id") invoiceId: String
    ): Response<TransactionDetailResponse>

    @GET("favorites/{username}")
    suspend fun getFavorites(
        @Path("username") username: String
    ): Response<FavoriteListResponse>

    @POST("favorites")
    suspend fun toggleFavorite(
        @Body request: FavoriteToggleRequest
    ): Response<FavoriteToggleResponse>

    @DELETE("favorites/{username}")
    suspend fun clearFavorites(
        @Path("username") username: String
    ): Response<FavoriteToggleResponse>

    @GET("profile/{username}")
    suspend fun getProfile(
        @Path("username") username: String
    ): Response<ProfileResponse>

    @PUT("profile")
    suspend fun updateProfile(
        @Body request: UpdateProfileRequest
    ): Response<ProfileResponse>

    @POST("change-password")
    suspend fun changePasswordApi(
        @Body request: ChangePasswordRequest
    ): Response<ChangePasswordApiResponse>

    @POST("check-user")
    suspend fun checkUser(
        @Body request: CheckUserRequest
    ): Response<CheckUserResponse>

    @POST("reset-password")
    suspend fun resetPasswordApi(
        @Body request: ResetPasswordApiRequest
    ): Response<ChangePasswordApiResponse>

    @GET("nexus-coin/balance/{username}")
    suspend fun getNexusCoinBalance(
        @Path("username") username: String
    ): Response<NexusCoinBalanceResponse>

    @GET("nexus-coin/history/{username}")
    suspend fun getNexusCoinHistory(
        @Path("username") username: String
    ): Response<NexusCoinHistoryResponse>

    @POST("nexus-coin/add")
    suspend fun addNexusCoin(
        @Body request: NexusCoinAddRequest
    ): Response<NexusCoinActionResponse>

    @POST("nexus-coin/deduct")
    suspend fun deductNexusCoin(
        @Body request: NexusCoinDeductRequest
    ): Response<NexusCoinActionResponse>
}