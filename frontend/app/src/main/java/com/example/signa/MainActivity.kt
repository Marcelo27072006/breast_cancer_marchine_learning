package com.example.signa

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.signa.ui.screens.DashboardScreen
import com.example.signa.ui.screens.HistoryScreen
import com.example.signa.ui.screens.HomeScreen
import com.example.signa.ui.screens.PacientesScreen
import com.example.signa.ui.screens.SymptomsScreen
import com.example.signa.ui.theme.SignaTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            SignaTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    val navController = rememberNavController()

                    NavHost(
                        navController    = navController,
                        startDestination = "home"
                    ) {
                        composable("home") {
                            HomeScreen(navController = navController)
                        }
                        composable("pacientes") {
                            PacientesScreen(navController = navController)
                        }
                        composable("history") {
                            HistoryScreen(navController = navController)
                        }
                        composable("dashboard") {
                            DashboardScreen(navController = navController)
                        }
                    }
                }
            }
        }
    }
}