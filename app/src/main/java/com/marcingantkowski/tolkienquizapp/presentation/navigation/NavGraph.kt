package com.marcingantkowski.tolkienquizapp.presentation.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.marcingantkowski.tolkienquizapp.presentation.HighScoresScreen
import com.marcingantkowski.tolkienquizapp.presentation.QuizScreen
import com.marcingantkowski.tolkienquizapp.presentation.StartScreen

@Composable
fun NavGraph() {
    val navController = rememberNavController()
    NavHost(
        navController = navController,
        startDestination = Screen.StartScreen.route
    ) {
        composable(route = Screen.StartScreen.route) {
            StartScreen(navController = navController)
        }
        composable(
            route = Screen.QuizScreen.route,
            arguments = listOf(
                navArgument("categoryId") { type = NavType.IntType; defaultValue = -1 },
                navArgument("difficulty") { type = NavType.StringType; defaultValue = "any" },
                navArgument("type") { type = NavType.StringType; defaultValue = "any" },
            )
        ) {
            QuizScreen()
        }
        composable(route = Screen.HighScoresScreen.route) {
            HighScoresScreen()
        }
    }
}