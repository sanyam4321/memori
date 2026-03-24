package com.example.memori

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.memori.ui.DashboardScreen
import com.example.memori.ui.StudyScreen
import com.example.memori.ui.theme.MemoriTheme
import com.example.memori.viewmodel.MemoriViewModel
import com.example.memori.ui.ManageCardsScreen
import com.example.memori.ui.SearchScreen

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MemoriTheme {
                val navController = rememberNavController()
                val viewModel: MemoriViewModel = viewModel()

                NavHost(navController = navController, startDestination = "dashboard") {
                    composable("dashboard") {
                        DashboardScreen(
                            viewModel = viewModel,
                            onStartStudy = { navController.navigate("study") },
                            onManageCards = { navController.navigate("manage") },
                            onSearchClick = { navController.navigate("search") }
                        )
                    }
                    composable("study") {
                        StudyScreen(
                            viewModel = viewModel,
                            onFinished = { navController.popBackStack() },
                            onBackClick = { navController.popBackStack() }
                        )
                    }
                    composable("manage") {
                        ManageCardsScreen(viewModel = viewModel)
                    }
                    composable("search") {
                        SearchScreen(
                            viewModel = viewModel,
                            onBackClick = { navController.popBackStack() }
                        )
                    }
                }
            }
        }
    }
}