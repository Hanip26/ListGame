package com.example.listgame.navigation

import androidx.compose.runtime.compositionLocalOf
import androidx.compose.runtime.snapshots.SnapshotStateList

sealed class Route {
    object Login : Route()
    data class Home(val username: String) : Route()
    data class Detail(val gameId: Int) : Route()
}

val LocalBackStack = compositionLocalOf<SnapshotStateList<Route>> {
    error("BackStack belum diinisialisasi")
}