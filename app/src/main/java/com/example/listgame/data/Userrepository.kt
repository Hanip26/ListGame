package com.example.listgame.data

import com.example.listgame.model.User
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.first
import java.security.MessageDigest
import com.example.listgame.data.ResetPasswordResult
import com.example.listgame.model.api.LoginRequest
import com.example.listgame.network.RetrofitClient
import com.example.listgame.model.api.RegisterRequest


class UserRepository(private val dataStore: AppDataStore) {

    val isLoggedIn      : Flow<Boolean> = dataStore.isLoggedInFlow
    val loggedInUsername: Flow<String>  = dataStore.loggedInUsernameFlow

    val currentUser: Flow<User?> = combine(
        dataStore.loggedInUsernameFlow,
        dataStore.registeredUsersFlow
    ) { username, users ->
        if (username.isBlank()) null
        else users.find { it.username.equals(username, ignoreCase = true) }
    }

    suspend fun register(
        username   : String,
        email      : String,
        password   : String,
        displayName: String
    ): RegisterResult = dataStore.registerUser(
        User(
            username     = username.trim(),
            email        = email.trim().lowercase(),
            passwordHash = hashPassword(password),
            displayName  = displayName.trim()
        )
    )

    suspend fun login(usernameOrEmail: String, password: String): LoginResult {
        val users = dataStore.registeredUsersFlow.first()
        val input = usernameOrEmail.trim()
        val user  = users.find {
            it.username.equals(input, ignoreCase = true) ||
                    it.email.equals(input, ignoreCase = true)
        } ?: return LoginResult.UserNotFound

        return if (user.passwordHash == hashPassword(password)) {
            dataStore.saveLoginSession(user.username)
            LoginResult.Success(user.username, user.displayName)
        } else {
            LoginResult.WrongPassword
        }
    }

    suspend fun logout() = dataStore.clearLoginSession()

    // ── Profil ────────────────────────────────────────────────────────────────
    suspend fun updateProfile(
        username   : String,
        displayName: String,
        email      : String,
        phone      : String,
        bio        : String
    ): UpdateProfileResult = dataStore.updateUserProfile(username, displayName, email, phone, bio)

    suspend fun resetPassword(
        usernameOrEmail: String,
        newPassword    : String
    ): ResetPasswordResult = dataStore.resetPasswordByEmail(
        usernameOrEmail = usernameOrEmail,
        newHash         = hashPassword(newPassword)
    )
    // ── Password ──────────────────────────────────────────────────────────────
    suspend fun changePassword(
        username       : String,
        currentPassword: String,
        newPassword    : String
    ): ChangePasswordResult = dataStore.updatePassword(
        username    = username,
        currentHash = hashPassword(currentPassword),
        newHash     = hashPassword(newPassword)
    )

    fun hashPassword(password: String): String {
        val bytes = MessageDigest
            .getInstance("SHA-256")
            .digest(password.toByteArray(Charsets.UTF_8))
        return bytes.joinToString("") { "%02x".format(it) }
    }
    // Tambahkan di dalam class UserRepository
    suspend fun checkUserExists(usernameOrEmail: String): Boolean {
        val users = dataStore.registeredUsersFlow.first()
        val input = usernameOrEmail.trim()
        return users.any {
            it.username.equals(input, ignoreCase = true) ||
                    it.email.equals(input, ignoreCase = true)
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

            if (
                response.isSuccessful &&
                response.body()?.success == true
            ) {
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
                LoginRequest(
                    email = email,
                    password = password
                )
            )

            if (
                response.isSuccessful &&
                response.body()?.success == true
            ) {

                val user = response.body()?.user

                if (user != null) {

                    dataStore.saveLoginSession(
                        user.name
                    )

                    LoginResult.Success(
                        user.name,
                        user.name
                    )
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