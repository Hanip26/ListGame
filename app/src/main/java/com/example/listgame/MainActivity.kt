package com.example.listgame

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.BackHandler
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.*
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.ui.Modifier
import com.example.listgame.navigation.LocalBackStack
import com.example.listgame.navigation.Route
import com.example.listgame.ui.screen.GameDetailScreen
import com.example.listgame.ui.screen.GameListScreen
import com.example.listgame.ui.screen.LoginScreen
import com.example.listgame.ui.theme.ListgameTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            ListgameTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    ListGameApp()
                }
            }
        }
    }
}

@Composable
fun <T> NavDisplay(
    backStack: SnapshotStateList<T>,
    content: @Composable (T) -> Unit
) {
    val currentRoute = backStack.lastOrNull()

    if (currentRoute != null) {
        BackHandler(enabled = backStack.size > 1) {
            backStack.removeLastOrNull()
        }
        content(currentRoute)
    }
}

@Composable
fun ListGameApp() {
    val backStack = remember { mutableStateListOf<Route>(Route.Login) }

    CompositionLocalProvider(LocalBackStack provides backStack) {
        NavDisplay(backStack = backStack) { currentRoute ->
            when (currentRoute) {
                is Route.Login -> LoginScreen()
                is Route.Home -> GameListScreen(username = currentRoute.username)
                is Route.Detail -> GameDetailScreen(gameId = currentRoute.gameId)
            }
        }
    }
}