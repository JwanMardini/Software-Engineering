package se.jwan.lab2.ui

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import se.jwan.lab2.data.repository.FirebaseRepository
import se.jwan.lab2.ui.screens.BakingScreen
import se.jwan.lab2.ui.screens.ControlScreen
import se.jwan.lab2.viewmodel.MainViewModel

@Composable
fun MyAppNavHost(
    startDestination: String = "control"
) {
    val navController = rememberNavController()
    
    NavHost(navController = navController, startDestination = startDestination) {
        composable("control") {
            ControlScreen(
                viewModel = MainViewModel(FirebaseRepository()),
                onNavigateToGemini = {
                navController.navigate("gemini")
            })
        }

        composable("gemini") {
            BakingScreen()
        }
    }

}