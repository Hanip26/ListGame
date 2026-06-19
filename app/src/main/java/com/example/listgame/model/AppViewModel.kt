package com.example.listgame.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.listgame.data.AppDataStore
import com.example.listgame.model.NexusCoinTransaction
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

    // ── Favorit per-akun ──────────────────────────────────────────────────────
    @OptIn(ExperimentalCoroutinesApi::class)
    val favoriteGames: StateFlow<List<Int>> = _activeUsername
        .flatMapLatest { username ->
            if (username.isBlank()) flowOf(emptyList())
            else dataStore.favoriteGamesFlow(username)
        }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), emptyList())

    // ── Transaksi game per-akun ───────────────────────────────────────────────
    @OptIn(ExperimentalCoroutinesApi::class)
    val transactions: StateFlow<List<Transaction>> = _activeUsername
        .flatMapLatest { username ->
            if (username.isBlank()) flowOf(emptyList())
            else dataStore.transactionsFlow(username)
        }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), emptyList())

    // ── NEXUS Coin: saldo per-akun ────────────────────────────────────────────
    @OptIn(ExperimentalCoroutinesApi::class)
    val nexusCoinBalance: StateFlow<Int> = _activeUsername
        .flatMapLatest { username ->
            if (username.isBlank()) flowOf(0)
            else dataStore.nexusCoinBalanceFlow(username)
        }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), 0)

    // ── NEXUS Coin: riwayat per-akun ──────────────────────────────────────────
    @OptIn(ExperimentalCoroutinesApi::class)
    val nexusCoinHistory: StateFlow<List<NexusCoinTransaction>> = _activeUsername
        .flatMapLatest { username ->
            if (username.isBlank()) flowOf(emptyList())
            else dataStore.nexusCoinHistoryFlow(username)
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

    // ── NEXUS Coin actions ────────────────────────────────────────────────────

    /** Tambah koin ke saldo akun yang sedang aktif */
    fun addNexusCoins(amount: Int) {
        val username = _activeUsername.value
        if (username.isBlank()) return
        viewModelScope.launch { dataStore.addNexusCoins(username, amount) }
    }

    /** Potong koin dari saldo (mis. untuk redeem) */
    fun deductNexusCoins(amount: Int, onResult: (Boolean) -> Unit = {}) {
        val username = _activeUsername.value
        if (username.isBlank()) { onResult(false); return }
        viewModelScope.launch {
            val success = dataStore.deductNexusCoins(username, amount)
            onResult(success)
        }
    }

    /** Simpan satu entri riwayat top-up NEXUS Coin */
    fun addNexusCoinTransaction(trx: NexusCoinTransaction) {
        val username = _activeUsername.value
        if (username.isBlank()) return
        viewModelScope.launch { dataStore.addNexusCoinTransaction(username, trx) }
    }
}