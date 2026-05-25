package com.example.listgame.data

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.*
import androidx.datastore.preferences.preferencesDataStore
import com.example.listgame.model.User
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import java.io.IOException

val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "nexus_prefs")

object PreferencesKeys {
    val SORT_OPTION        = stringPreferencesKey("sort_option")
    val LAST_USERNAME      = stringPreferencesKey("last_username")
    val IS_DARK_THEME      = booleanPreferencesKey("is_dark_theme")
    val REGISTERED_USERS   = stringPreferencesKey("registered_users")
    val LOGGED_IN_USERNAME = stringPreferencesKey("logged_in_username")
    val IS_LOGGED_IN       = booleanPreferencesKey("is_logged_in")

    fun favoriteGamesKey(username: String) =
        stringPreferencesKey("favorite_games_${username.lowercase()}")
}

class AppDataStore(private val context: Context) {

    fun favoriteGamesFlow(username: String): Flow<List<Int>> =
        context.dataStore.data
            .catch { if (it is IOException) emit(emptyPreferences()) else throw it }
            .map { prefs ->
                val json = prefs[PreferencesKeys.favoriteGamesKey(username)] ?: "[]"
                try { Json.decodeFromString<List<Int>>(json) } catch (e: Exception) { emptyList() }
            }

    suspend fun toggleFavorite(username: String, gameId: Int) {
        context.dataStore.edit { prefs ->
            val key  = PreferencesKeys.favoriteGamesKey(username)
            val list = try {
                Json.decodeFromString<MutableList<Int>>(prefs[key] ?: "[]")
            } catch (e: Exception) { mutableListOf() }
            if (list.contains(gameId)) list.remove(gameId) else list.add(gameId)
            prefs[key] = Json.encodeToString(list)
        }
    }

    suspend fun clearFavorites(username: String) {
        context.dataStore.edit { prefs ->
            prefs[PreferencesKeys.favoriteGamesKey(username)] = "[]"
        }
    }

    val sortOptionFlow: Flow<String> = context.dataStore.data
        .catch { if (it is IOException) emit(emptyPreferences()) else throw it }
        .map { prefs -> prefs[PreferencesKeys.SORT_OPTION] ?: "A-Z" }

    val lastUsernameFlow: Flow<String> = context.dataStore.data
        .catch { if (it is IOException) emit(emptyPreferences()) else throw it }
        .map { prefs -> prefs[PreferencesKeys.LAST_USERNAME] ?: "" }

    val isDarkThemeFlow: Flow<Boolean> = context.dataStore.data
        .catch { if (it is IOException) emit(emptyPreferences()) else throw it }
        .map { prefs -> prefs[PreferencesKeys.IS_DARK_THEME] ?: false }

    suspend fun saveSortOption(option: String) {
        context.dataStore.edit { it[PreferencesKeys.SORT_OPTION] = option }
    }

    suspend fun saveLastUsername(username: String) {
        context.dataStore.edit { it[PreferencesKeys.LAST_USERNAME] = username }
    }

    suspend fun setDarkTheme(isDark: Boolean) {
        context.dataStore.edit { it[PreferencesKeys.IS_DARK_THEME] = isDark }
    }

    val registeredUsersFlow: Flow<List<User>> = context.dataStore.data
        .catch { if (it is IOException) emit(emptyPreferences()) else throw it }
        .map { prefs ->
            val json = prefs[PreferencesKeys.REGISTERED_USERS] ?: "[]"
            try { Json.decodeFromString<List<User>>(json) } catch (e: Exception) { emptyList() }
        }

    val isLoggedInFlow: Flow<Boolean> = context.dataStore.data
        .catch { if (it is IOException) emit(emptyPreferences()) else throw it }
        .map { prefs -> prefs[PreferencesKeys.IS_LOGGED_IN] ?: false }

    val loggedInUsernameFlow: Flow<String> = context.dataStore.data
        .catch { if (it is IOException) emit(emptyPreferences()) else throw it }
        .map { prefs -> prefs[PreferencesKeys.LOGGED_IN_USERNAME] ?: "" }

    suspend fun registerUser(user: User): RegisterResult {
        var result: RegisterResult = RegisterResult.Success
        context.dataStore.edit { prefs ->
            val list = try {
                Json.decodeFromString<MutableList<User>>(
                    prefs[PreferencesKeys.REGISTERED_USERS] ?: "[]"
                )
            } catch (e: Exception) { mutableListOf() }

            when {
                list.any { it.username.equals(user.username, ignoreCase = true) } ->
                    result = RegisterResult.UsernameTaken
                list.any { it.email.equals(user.email, ignoreCase = true) } ->
                    result = RegisterResult.EmailTaken
                else -> {
                    list.add(user)
                    prefs[PreferencesKeys.REGISTERED_USERS] = Json.encodeToString(list)
                }
            }
        }
        return result
    }

    suspend fun updateUserProfile(
        username   : String,
        displayName: String,
        email      : String,
        phone      : String,
        bio        : String
    ): UpdateProfileResult {
        var result: UpdateProfileResult = UpdateProfileResult.Success
        context.dataStore.edit { prefs ->
            val list = try {
                Json.decodeFromString<MutableList<User>>(
                    prefs[PreferencesKeys.REGISTERED_USERS] ?: "[]"
                )
            } catch (e: Exception) { mutableListOf() }

            val idx = list.indexOfFirst { it.username.equals(username, ignoreCase = true) }
            if (idx == -1) { result = UpdateProfileResult.UserNotFound; return@edit }

            val emailTaken = list.any {
                !it.username.equals(username, ignoreCase = true) &&
                        it.email.equals(email, ignoreCase = true)
            }
            if (emailTaken) { result = UpdateProfileResult.EmailTaken; return@edit }

            list[idx] = list[idx].copy(
                displayName = displayName.trim(),
                email       = email.trim().lowercase(),
                phone       = phone.trim(),
                bio         = bio.trim()
            )
            prefs[PreferencesKeys.REGISTERED_USERS] = Json.encodeToString(list)
        }
        return result
    }

    suspend fun updatePassword(
        username       : String,
        currentHash    : String,
        newHash        : String
    ): ChangePasswordResult {
        var result: ChangePasswordResult = ChangePasswordResult.Success
        context.dataStore.edit { prefs ->
            val list = try {
                Json.decodeFromString<MutableList<User>>(
                    prefs[PreferencesKeys.REGISTERED_USERS] ?: "[]"
                )
            } catch (e: Exception) { mutableListOf() }

            val idx = list.indexOfFirst { it.username.equals(username, ignoreCase = true) }
            if (idx == -1) { result = ChangePasswordResult.UserNotFound; return@edit }

            if (list[idx].passwordHash != currentHash) {
                result = ChangePasswordResult.WrongCurrentPassword
                return@edit
            }
            list[idx] = list[idx].copy(passwordHash = newHash)
            prefs[PreferencesKeys.REGISTERED_USERS] = Json.encodeToString(list)
        }
        return result
    }

    suspend fun saveLoginSession(username: String) {
        context.dataStore.edit { prefs ->
            prefs[PreferencesKeys.IS_LOGGED_IN]       = true
            prefs[PreferencesKeys.LOGGED_IN_USERNAME] = username
            prefs[PreferencesKeys.LAST_USERNAME]      = username
        }
    }

    suspend fun clearLoginSession() {
        context.dataStore.edit { prefs ->
            prefs[PreferencesKeys.IS_LOGGED_IN]       = false
            prefs[PreferencesKeys.LOGGED_IN_USERNAME] = ""
        }
    }
}

sealed class RegisterResult {
    object Success       : RegisterResult()
    object UsernameTaken : RegisterResult()
    object EmailTaken    : RegisterResult()
}

sealed class UpdateProfileResult {
    object Success      : UpdateProfileResult()
    object UserNotFound : UpdateProfileResult()
    object EmailTaken   : UpdateProfileResult()
}

sealed class ChangePasswordResult {
    object Success             : ChangePasswordResult()
    object UserNotFound        : ChangePasswordResult()
    object WrongCurrentPassword: ChangePasswordResult()
}