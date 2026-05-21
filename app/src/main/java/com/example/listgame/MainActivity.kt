package com.example.listgame

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.ExperimentalSharedTransitionApi
import androidx.compose.animation.SharedTransitionLayout
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import com.example.listgame.navigation.LocalBackStack
import com.example.listgame.navigation.Route
import com.example.listgame.ui.screen.GameDetailScreen
import com.example.listgame.ui.screen.GameListScreen
import com.example.listgame.ui.screen.LoginScreen
import com.example.listgame.ui.screen.OrderConfirmationScreen
import com.example.listgame.ui.screen.PaymentProgressScreen
import com.example.listgame.ui.screen.TopUpScreen
import com.example.listgame.ui.theme.ListgameTheme

@OptIn(ExperimentalSharedTransitionApi::class)
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            ListgameTheme {
                val backStack = remember { mutableStateListOf<Route>(Route.Login) }

                val favoriteGames = remember { mutableStateListOf<Int>() }
                var sortOption by remember { mutableStateOf("A-Z") }

                CompositionLocalProvider(LocalBackStack provides backStack) {
                    Surface(
                        modifier = Modifier.fillMaxSize(),
                        color = MaterialTheme.colorScheme.background
                    ) {
                        SharedTransitionLayout {
                            AnimatedContent(
                                targetState = backStack.lastOrNull(),
                                label = "NexusNavigation"
                            ) { currentRoute ->
                                when (currentRoute) {

                                    is Route.Login -> {
                                        LoginScreen()
                                    }

                                    is Route.Home -> {
                                        GameListScreen(
                                            username = currentRoute.username,
                                            favoriteGames = favoriteGames,
                                            sortOption = sortOption,
                                            onSortChange = { sortOption = it },
                                            onFavoriteToggle = { gameId ->
                                                if (favoriteGames.contains(gameId))
                                                    favoriteGames.remove(gameId)
                                                else
                                                    favoriteGames.add(gameId)
                                            },
                                            onClearFavorites = { favoriteGames.clear() },
                                            sharedTransitionScope = this@SharedTransitionLayout,
                                            animatedVisibilityScope = this@AnimatedContent
                                        )
                                    }

                                    is Route.Detail -> {
                                        GameDetailScreen(
                                            gameId = currentRoute.gameId,
                                            sharedTransitionScope = this@SharedTransitionLayout,
                                            animatedVisibilityScope = this@AnimatedContent
                                        )
                                    }

                                    is Route.TopUp -> {
                                        TopUpScreen(gameId = currentRoute.gameId)
                                    }

                                    // ✅ Halaman Konfirmasi Pesanan
                                    is Route.OrderConfirmation -> {
                                        OrderConfirmationScreen(route = currentRoute)
                                    }

                                    // ✅ Halaman Progress Pembayaran
                                    is Route.PaymentProgress -> {
                                        PaymentProgressScreen(route = currentRoute)
                                    }

                                    null -> {}
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}