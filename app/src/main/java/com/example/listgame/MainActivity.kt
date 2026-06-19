package com.example.listgame

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.ExperimentalSharedTransitionApi
import androidx.compose.animation.SharedTransitionLayout
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import com.example.listgame.navigation.LocalBackStack
import com.example.listgame.navigation.Route
import com.example.listgame.ui.screen.*
import com.example.listgame.ui.theme.ListgameTheme
import com.example.listgame.viewmodel.AppViewModel
import com.example.listgame.viewmodel.AuthViewModel
import com.example.listgame.ui.screen.CekTransaksiScreen
import com.example.listgame.ui.screen.KalkulatorWinRateScreen
import dagger.hilt.android.AndroidEntryPoint
import com.example.listgame.ui.screen.ForgotPasswordScreen
import com.example.listgame.ui.screen.NexusCoinTopUpScreen
import com.example.listgame.ui.screen.NexusCoinHistoryScreen
import com.example.listgame.ui.screen.NexusCoinRedeemScreen

@AndroidEntryPoint
@OptIn(ExperimentalSharedTransitionApi::class)
class MainActivity : ComponentActivity() {

    private val appViewModel  : AppViewModel  by viewModels()
    private val authViewModel : AuthViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            val isDarkTheme   by appViewModel.isDarkTheme.collectAsState()
            val isLoggedIn    by authViewModel.isLoggedIn.collectAsState()
            val savedUsername by authViewModel.loggedInUsername.collectAsState()

            ListgameTheme(darkTheme = isDarkTheme) {
                val backStack = remember {
                    mutableStateListOf<Route>(
                        if (isLoggedIn && savedUsername.isNotBlank()) Route.Home(savedUsername)
                        else Route.Login
                    )
                }

                val favoriteGames by appViewModel.favoriteGames.collectAsState()
                val sortOption    by appViewModel.sortOption.collectAsState()


                val doLogout: () -> Unit = {
                    appViewModel.setActiveUser("")
                    authViewModel.logout()
                    backStack.clear()
                    backStack.add(Route.Login)
                }
                CompositionLocalProvider(LocalBackStack provides backStack) {
                    Surface(
                        modifier = Modifier.fillMaxSize(),
                        color    = MaterialTheme.colorScheme.background
                    ) {
                        SharedTransitionLayout {
                            AnimatedContent(
                                targetState = backStack.lastOrNull(),
                                label       = "NexusNavigation"
                            ) { currentRoute ->
                                when (currentRoute) {
                                    is Route.CekTransaksi -> {
                                        CekTransaksiScreen(appViewModel = appViewModel)
                                    }

                                    is Route.KalkulatorWinRate -> {
                                        KalkulatorWinRateScreen()
                                    }
                                    // ── Auth ──────────────────────────────
                                    is Route.Login -> {
                                        LoginScreen(
                                            viewModel            = authViewModel,
                                            onLoginSuccess       = { username ->
                                                appViewModel.setActiveUser(username)
                                                backStack.clear()
                                                backStack.add(Route.Home(username))
                                            },
                                            onNavigateToRegister = { backStack.add(Route.Register) },
                                            onNavigateToForgotPassword = { backStack.add(Route.ForgotPassword) }
                                        )
                                    }

                                    is Route.Register -> {
                                        RegisterScreen(
                                            viewModel         = authViewModel,
                                            onRegisterSuccess = { displayName ->
                                                authViewModel.setRegisterSuccessMessage(displayName)
                                                backStack.clear()
                                                backStack.add(Route.Login)
                                            },
                                            onNavigateBack = { backStack.removeLastOrNull() }
                                        )
                                    }

<<<<<<< HEAD
                                    is Route.ForgotPassword -> {
                                        ForgotPasswordScreen(
                                            viewModel      = authViewModel,
                                            onResetSuccess = {
                                                // Tampilkan snackbar sukses di LoginScreen
                                                authViewModel.setRegisterSuccessMessage("Password berhasil direset! Silakan masuk.")
                                                backStack.removeLastOrNull()
                                            }
                                        )
                                    }

                                    // ── Home ──────────────────────────────
=======
>>>>>>> origin/main
                                    is Route.Home -> {
                                        LaunchedEffect(currentRoute.username) {
                                            appViewModel.setActiveUser(currentRoute.username)
                                        }
                                        GameListScreen(
                                            username              = currentRoute.username,
                                            favoriteGames         = favoriteGames,
                                            sortOption            = sortOption,
                                            onSortChange          = { appViewModel.saveSortOption(it) },
                                            onFavoriteToggle      = { appViewModel.toggleFavorite(it) },
                                            onClearFavorites      = { appViewModel.clearFavorites() },
                                            onLogout              = doLogout,
                                            onNavigateToDashboard = { backStack.add(Route.Dashboard) },
                                            onNavigateToProfile   = { backStack.add(Route.Profile) },
                                            sharedTransitionScope   = this@SharedTransitionLayout,
                                            animatedVisibilityScope = this@AnimatedContent
                                        )
                                    }

                                    is Route.Dashboard -> {
                                        DashboardScreen(
                                            authViewModel       = authViewModel,
                                            appViewModel        = appViewModel,
                                            onNavigateToProfile = { backStack.add(Route.Profile) },
                                            onLogout            = doLogout
                                        )
                                    }

<<<<<<< HEAD
                                    // ── Profil (SUDAH DIPERBAIKI SINKRONISASINYA) ──
=======
>>>>>>> origin/main
                                    is Route.Profile -> {
                                        ProfileScreen(
                                            authViewModel = authViewModel,
                                            appViewModel  = appViewModel,
                                            onLogout      = doLogout
                                        )
                                    }

                                    is Route.Detail -> {
                                        GameDetailScreen(
                                            gameId                  = currentRoute.gameId,
                                            sharedTransitionScope   = this@SharedTransitionLayout,
                                            animatedVisibilityScope = this@AnimatedContent
                                        )
                                    }

                                    is Route.TopUp -> {
                                        TopUpScreen(
                                            gameId       = currentRoute.gameId,
                                            appViewModel = appViewModel
                                        )
                                    }

                                    is Route.OrderConfirmation -> {
                                        OrderConfirmationScreen(route = currentRoute)
                                    }

                                    is Route.PaymentProgress -> {
                                        PaymentProgressScreen(
                                            route        = currentRoute,
                                            appViewModel = appViewModel
                                        )
                                    }

                                    is Route.NexusCoinTopUp -> {
                                        NexusCoinTopUpScreen(appViewModel = appViewModel)
                                    }

                                    is Route.NexusCoinRedeem -> {
                                        NexusCoinRedeemScreen(appViewModel = appViewModel)
                                    }

                                    is Route.NexusCoinHistory -> {
                                        NexusCoinHistoryScreen(appViewModel = appViewModel)
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