package com.example.listgame

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.listgame.ui.screen.GameDetailScreen
import com.example.listgame.ui.screen.GameListScreen
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
fun ListGameApp() {
    val navController = rememberNavController()

    NavHost(
        navController = navController,
        startDestination = "home"
    ) {
        // Rute Home
        composable("home") {
            GameListScreen(
                navigateToDetail = { gameId ->
                    navController.navigate("detail/$gameId")
                }
            )
        }

        composable("detail/{gameId}") { backStackEntry ->
            val idString = backStackEntry.arguments?.getString("gameId")
            val id = idString?.toIntOrNull() ?: -1

            GameDetailScreen(
                gameId = id,
                navigateBack = {
                    navController.navigateUp()
                }
            )
        }
    }
}