package com.example.listgame.data

import com.example.listgame.model.User
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.first
import java.security.MessageDigest

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


    suspend fun updateProfile(
        username   : String,
        displayName: String,
        email      : String,
        phone      : String,
        bio        : String
    ): UpdateProfileResult = dataStore.updateUserProfile(username, displayName, email, phone, bio)

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
}

sealed class LoginResult {
    data class Success(val username: String, val displayName: String) : LoginResult()
    object WrongPassword : LoginResult()
    object UserNotFound  : LoginResult()
}