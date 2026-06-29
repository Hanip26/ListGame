package com.example.listgame.model.api

data class ApiUserProfile(
    val id: Int,
    val name: String,
    val display_name: String?,
    val email: String,
    val phone: String?,
    val bio: String?
)

data class ProfileResponse(
    val success: Boolean,
    val message: String? = null,
    val user: ApiUserProfile?
)

data class UpdateProfileRequest(
    val username: String,
    val display_name: String,
    val email: String,
    val phone: String,
    val bio: String
)

data class ChangePasswordRequest(
    val username: String,
    val current_password: String,
    val new_password: String
)

data class ChangePasswordApiResponse(
    val success: Boolean,
    val message: String
)
data class CheckUserRequest(
    val usernameOrEmail: String
)

data class CheckUserResponse(
    val success: Boolean,
    val exists: Boolean
)

data class ResetPasswordApiRequest(
    val usernameOrEmail: String,
    val newPassword: String
)