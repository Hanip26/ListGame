package com.example.listgame.data

import com.example.listgame.model.User
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.first
import java.security.MessageDigest
import com.example.listgame.data.ResetPasswordResult
import com.example.listgame.model.api.LoginRequest
import com.example.listgame.model.api.ChangePasswordRequest
import com.example.listgame.model.api.UpdateProfileRequest
import com.example.listgame.network.RetrofitClient
import com.example.listgame.model.api.RegisterRequest
import com.example.listgame.model.api.CheckUserRequest
import com.example.listgame.model.api.ResetPasswordApiRequest

class UserRepository(private val dataStore: AppDataStore) {

    val isLoggedIn      : Flow<Boolean> = dataStore.isLoggedInFlow
    val loggedInUsername: Flow<String>  = dataStore.loggedInUsernameFlow

    // ── Profil sekarang dari API, bukan dataStore lokal ────────────────────
    private val _currentUser = MutableStateFlow<User?>(null)
    val currentUser: Flow<User?> = _currentUser

    suspend fun refreshProfile(username: String) {
        if (username.isBlank()) {
            _currentUser.value = null
            return
        }
        try {
            val response = RetrofitClient.api.getProfile(username)
            if (response.isSuccessful && response.body()?.success == true) {
                val apiUser = response.body()?.user
                if (apiUser != null) {
                    _currentUser.value = User(
                        username     = apiUser.name,
                        email        = apiUser.email,
                        passwordHash = "",
                        displayName  = apiUser.display_name ?: apiUser.name,
                        phone        = apiUser.phone ?: "",
                        bio          = apiUser.bio ?: ""
                    )
                }
            }
        } catch (e: Exception) {
            // biarkan currentUser apa adanya kalau request gagal
        }
    }

    suspend fun logout() = dataStore.clearLoginSession()

    suspend fun updateProfile(
        username   : String,
        displayName: String,
        email      : String,
        phone      : String,
        bio        : String
    ): UpdateProfileResult {
        return try {
            val response = RetrofitClient.api.updateProfile(
                UpdateProfileRequest(
                    username = username,
                    display_name = displayName,
                    email = email,
                    phone = phone,
                    bio = bio
                )
            )
            if (response.isSuccessful && response.body()?.success == true) {
                refreshProfile(username)
                UpdateProfileResult.Success
            } else {
                UpdateProfileResult.EmailTaken
            }
        } catch (e: Exception) {
            UpdateProfileResult.UserNotFound
        }
    }

    suspend fun resetPassword(
        usernameOrEmail: String,
        newPassword: String
    ): ResetPasswordResult {
        return try {
            val response = RetrofitClient.api.resetPasswordApi(
                ResetPasswordApiRequest(
                    usernameOrEmail = usernameOrEmail.trim(),
                    newPassword = newPassword
                )
            )
            if (response.isSuccessful && response.body()?.success == true) {
                ResetPasswordResult.Success
            } else {
                ResetPasswordResult.UserNotFound
            }
        } catch (e: Exception) {
            ResetPasswordResult.UserNotFound
        }
    }

    suspend fun changePassword(
        username       : String,
        currentPassword: String,
        newPassword    : String
    ): ChangePasswordResult {
        return try {
            val response = RetrofitClient.api.changePasswordApi(
                ChangePasswordRequest(
                    username = username,
                    current_password = currentPassword,
                    new_password = newPassword
                )
            )
            if (response.isSuccessful && response.body()?.success == true) {
                ChangePasswordResult.Success
            } else {
                ChangePasswordResult.WrongCurrentPassword
            }
        } catch (e: Exception) {
            ChangePasswordResult.UserNotFound
        }
    }

    fun hashPassword(password: String): String {
        val bytes = MessageDigest
            .getInstance("SHA-256")
            .digest(password.toByteArray(Charsets.UTF_8))
        return bytes.joinToString("") { "%02x".format(it) }
    }

    suspend fun checkUserExists(usernameOrEmail: String): Boolean {
        return try {
            val response = RetrofitClient.api.checkUser(
                CheckUserRequest(usernameOrEmail = usernameOrEmail.trim())
            )
            response.isSuccessful && response.body()?.exists == true
        } catch (e: Exception) {
            false
        }
    }

    suspend fun registerApi(
        username: String,
        email: String,
        password: String,
        displayName: String
    ): RegisterResult {
        return try {
            val response =
                RetrofitClient.api.register(
                    RegisterRequest(
                        username = username,
                        email = email,
                        password = password,
                        display_name = displayName
                    )
                )
            if (response.isSuccessful && response.body()?.success == true) {
                RegisterResult.Success
            } else {
                RegisterResult.EmailTaken
            }
        } catch (e: Exception) {
            RegisterResult.EmailTaken
        }
    }

    suspend fun loginApi(
        email: String,
        password: String
    ): LoginResult {
        return try {
            val response = RetrofitClient.api.login(
                LoginRequest(email = email, password = password)
            )
            if (response.isSuccessful && response.body()?.success == true) {
                val user = response.body()?.user
                if (user != null) {
                    dataStore.saveLoginSession(user.name)
                    refreshProfile(user.name)
                    LoginResult.Success(user.name, user.name)
                } else {
                    LoginResult.UserNotFound
                }
            } else {
                LoginResult.WrongPassword
            }
        } catch (e: Exception) {
            LoginResult.UserNotFound
        }
    }
}

sealed class LoginResult {
    data class Success(val username: String, val displayName: String) : LoginResult()
    object WrongPassword : LoginResult()
    object UserNotFound  : LoginResult()
}