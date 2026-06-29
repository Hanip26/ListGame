package com.example.listgame.ui.components

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Dashboard
import androidx.compose.material.icons.rounded.MonetizationOn
import androidx.compose.material.icons.rounded.Receipt
import androidx.compose.material.icons.rounded.SportsEsports
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.vector.ImageVector

enum class BottomNavDestination(
    val label: String,
    val icon: ImageVector
) {
    GAME("Game",           Icons.Rounded.SportsEsports),
    CEK_TRANSAKSI("Cek Transaksi", Icons.Rounded.Receipt),
    NEXUS_COIN("Nexus Coin",   Icons.Rounded.MonetizationOn),
    DASHBOARD("Dashboard", Icons.Rounded.Dashboard)
}

@Composable
fun BottomNavBar(
    current : BottomNavDestination,
    onSelect: (BottomNavDestination) -> Unit
) {
    NavigationBar {
        BottomNavDestination.entries.forEach { dest ->
            NavigationBarItem(
                selected = current == dest,
                onClick  = { if (current != dest) onSelect(dest) },
                icon     = { Icon(dest.icon, contentDescription = dest.label) },
                label    = { Text(dest.label) }
            )
        }
    }
}