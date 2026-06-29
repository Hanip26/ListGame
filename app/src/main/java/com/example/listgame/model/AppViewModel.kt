package com.example.listgame.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.listgame.data.AppDataStore
import com.example.listgame.model.NexusCoinTransaction
import com.example.listgame.model.NexusCoinTransactionStatus
import com.example.listgame.model.Transaction
import com.example.listgame.network.FavoriteRepository
import com.example.listgame.network.NexusCoinRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.*
import javax.inject.Inject

@HiltViewModel
class AppViewModel @Inject constructor(
    private val dataStore: AppDataStore
) : ViewModel() {

    private val favoriteRepository  = FavoriteRepository()
    private val nexusCoinRepository = NexusCoinRepository()

    // ── Preferensi UI (tetap lokal) ───────────────────────────────────────────
    val sortOption: StateFlow<String> = dataStore.sortOptionFlow
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), "A-Z")

    val lastUsername: StateFlow<String> = dataStore.lastUsernameFlow
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), "")

    val isDarkTheme: StateFlow<Boolean> = dataStore.isDarkThemeFlow
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), false)

    // ── Active user ───────────────────────────────────────────────────────────
    private val _activeUsername = MutableStateFlow("")

    fun setActiveUser(username: String) {
        _activeUsername.value = username
        if (username.isNotBlank()) {
            refreshFavorites()
            refreshNexusCoin()
        } else {
            _favoriteGames.value    = emptyList()
            _nexusCoinBalance.value = 0
            _nexusCoinHistory.value = emptyList()
        }
    }

    // ── Favorit — dari API ────────────────────────────────────────────────────
    private val _favoriteGames = MutableStateFlow<List<Int>>(emptyList())
    val favoriteGames: StateFlow<List<Int>> = _favoriteGames

    private fun refreshFavorites() {
        val username = _activeUsername.value
        if (username.isBlank()) return
        viewModelScope.launch {
            try {
                val response = favoriteRepository.getFavorites(username)
                if (response.isSuccessful) {
                    _favoriteGames.value =
                        response.body()?.favorites?.map { it.game_id } ?: emptyList()
                }
            } catch (e: Exception) { }
        }
    }

    fun toggleFavorite(gameId: Int) {
        val username = _activeUsername.value
        if (username.isBlank()) return
        viewModelScope.launch {
            try { favoriteRepository.toggleFavorite(username, gameId) }
            catch (e: Exception) { }
            finally { refreshFavorites() }
        }
    }

    fun clearFavorites() {
        val username = _activeUsername.value
        if (username.isBlank()) return
        viewModelScope.launch {
            try { favoriteRepository.clearFavorites(username) }
            catch (e: Exception) { }
            finally { refreshFavorites() }
        }
    }

    // ── Transaksi game — tetap di DataStore (sudah tersimpan di MySQL via API) ─
    @OptIn(ExperimentalCoroutinesApi::class)
    val transactions: StateFlow<List<Transaction>> = _activeUsername
        .flatMapLatest { username ->
            if (username.isBlank()) flowOf(emptyList())
            else dataStore.transactionsFlow(username)
        }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), emptyList())

    fun addTransaction(transaction: Transaction) {
        val username = _activeUsername.value
        if (username.isBlank()) return
        viewModelScope.launch { dataStore.addTransaction(username, transaction) }
    }

    // ── Nexus Coin — dari API ─────────────────────────────────────────────────
    private val _nexusCoinBalance = MutableStateFlow(0)
    val nexusCoinBalance: StateFlow<Int> = _nexusCoinBalance

    private val _nexusCoinHistory = MutableStateFlow<List<NexusCoinTransaction>>(emptyList())
    val nexusCoinHistory: StateFlow<List<NexusCoinTransaction>> = _nexusCoinHistory

    private fun refreshNexusCoin() {
        val username = _activeUsername.value
        if (username.isBlank()) return
        viewModelScope.launch {
            try {
                val balRes = nexusCoinRepository.getBalance(username)
                if (balRes.isSuccessful) {
                    _nexusCoinBalance.value = balRes.body()?.balance ?: 0
                }
                val hisRes = nexusCoinRepository.getHistory(username)
                if (hisRes.isSuccessful) {
                    _nexusCoinHistory.value = hisRes.body()?.transactions?.map { api ->
                        val epochMs = try {
                            SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault())
                                .parse(api.created_at)?.time ?: System.currentTimeMillis()
                        } catch (e: Exception) { System.currentTimeMillis() }
                        NexusCoinTransaction(
                            invoiceId   = api.invoice_id,
                            denomLabel  = api.denom_label,
                            coinValue   = api.coin_value,
                            paymentName = api.payment_name,
                            totalPrice  = api.total_price,
                            status      = when (api.status.uppercase()) {
                                "SUCCESS" -> NexusCoinTransactionStatus.SUCCESS
                                "FAILED"  -> NexusCoinTransactionStatus.FAILED
                                else      -> NexusCoinTransactionStatus.PENDING
                            },
                            createdAt   = epochMs
                        )
                    } ?: emptyList()
                }
            } catch (e: Exception) { }
        }
    }

    fun addNexusCoins(amount: Int) {
        // dipanggil dari NexusCoinTopUpScreen/RedeemScreen bersamaan dengan addNexusCoinTransaction
        // penambahan saldo ditangani di addNexusCoinTransaction, fungsi ini sekarang no-op
        // agar tidak double-add
    }

    fun addNexusCoinTransaction(trx: NexusCoinTransaction) {
        val username = _activeUsername.value
        if (username.isBlank()) return
        viewModelScope.launch {
            try {
                nexusCoinRepository.addCoins(
                    username    = username,
                    invoiceId   = trx.invoiceId,
                    denomLabel  = trx.denomLabel,
                    coinValue   = trx.coinValue,
                    paymentName = trx.paymentName,
                    totalPrice  = trx.totalPrice
                )
            } catch (e: Exception) { }
            finally { refreshNexusCoin() }
        }
    }

    fun deductNexusCoins(amount: Int, onResult: (Boolean) -> Unit = {}) {
        val username = _activeUsername.value
        if (username.isBlank()) { onResult(false); return }
        viewModelScope.launch {
            try {
                val response = nexusCoinRepository.deductCoins(username, amount)
                val success  = response.isSuccessful && response.body()?.success == true
                onResult(success)
            } catch (e: Exception) {
                onResult(false)
            } finally {
                refreshNexusCoin()
            }
        }
    }

    // ── Preferensi actions ────────────────────────────────────────────────────
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