package com.example.listgame.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.listgame.data.AppDataStore
import com.example.listgame.model.Transaction
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AppViewModel @Inject constructor(
    private val dataStore: AppDataStore
) : ViewModel() {

    // ── State global ──────────────────────────────────────────────────────────
    val sortOption: StateFlow<String> = dataStore.sortOptionFlow
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), "A-Z")

    val lastUsername: StateFlow<String> = dataStore.lastUsernameFlow
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), "")

    val isDarkTheme: StateFlow<Boolean> = dataStore.isDarkThemeFlow
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), false)

    // ── Active user (set saat login/logout) ───────────────────────────────────
    private val _activeUsername = MutableStateFlow("")

    fun setActiveUser(username: String) {
        _activeUsername.value = username
    }

    // ── Favorit per-akun (flatMapLatest → berganti otomatis saat user ganti) ──
    @OptIn(ExperimentalCoroutinesApi::class)
    val favoriteGames: StateFlow<List<Int>> = _activeUsername
        .flatMapLatest { username ->
            if (username.isBlank()) flowOf(emptyList())
            else dataStore.favoriteGamesFlow(username)
        }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), emptyList())

    // ── Transaksi per-akun ────────────────────────────────────────────────────
    @OptIn(ExperimentalCoroutinesApi::class)
    val transactions: StateFlow<List<Transaction>> = _activeUsername
        .flatMapLatest { username ->
            if (username.isBlank()) flowOf(emptyList())
            else dataStore.transactionsFlow(username)
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

    fun addTransaction(transaction: Transaction) {
        val username = _activeUsername.value
        if (username.isBlank()) return
        viewModelScope.launch { dataStore.addTransaction(username, transaction) }
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
