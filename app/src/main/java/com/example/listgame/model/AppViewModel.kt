package com.example.listgame.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.listgame.data.AppDataStore
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

class AppViewModel(application: Application) : AndroidViewModel(application) {

    private val dataStore = AppDataStore(application)

    // ── State global ──────────────────────────────────────────────────────────
    val sortOption: StateFlow<String> = dataStore.sortOptionFlow
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), "A-Z")

    val lastUsername: StateFlow<String> = dataStore.lastUsernameFlow
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), "")

    val isDarkTheme: StateFlow<Boolean> = dataStore.isDarkThemeFlow
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), false)

    // ── Favorit per-akun ──────────────────────────────────────────────────────
    // activeUsername di-set oleh MainActivity setiap kali ada login/logout.
    // FlatMapLatest memastikan Flow favorit langsung berganti saat username ganti
    // — tidak perlu restart app.
    private val _activeUsername = MutableStateFlow("")

    /** Dipanggil MainActivity setiap kali user login atau logout. */
    fun setActiveUser(username: String) {
        _activeUsername.value = username
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    val favoriteGames: StateFlow<List<Int>> = _activeUsername
        .flatMapLatest { username ->
            if (username.isBlank()) flowOf(emptyList())
            else dataStore.favoriteGamesFlow(username)
        }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), emptyList())

    // ── Actions ───────────────────────────────────────────────────────────────

    fun toggleFavorite(gameId: Int) {
        val username = _activeUsername.value
        if (username.isBlank()) return
        viewModelScope.launch { dataStore.toggleFavorite(username, gameId) }
    }

    fun clearFavorites() {
        val username = _activeUsername.value
        if (username.isBlank()) return
        viewModelScope.launch { dataStore.clearFavorites(username) }
    }

    fun saveSortOption(option: String) {
        viewModelScope.launch { dataStore.saveSortOption(option) }
    }

    fun saveUsername(username: String) {
        viewModelScope.launch { dataStore.saveLastUsername(username) }
    }

    fun setDarkTheme(isDark: Boolean) {
        viewModelScope.launch { dataStore.setDarkTheme(isDark) }
    }
}