package com.marcingantkowski.tolkienquizapp.presentation.navigation

sealed class Screen(val route: String) {
    object StartScreen : Screen("start_screen")
    object QuizScreen : Screen("quiz_screen?categoryId={categoryId}&difficulty={difficulty}&type={type}") {
        fun createRoute(categoryId: Int?, difficulty: String?, type: String?): String {
            return "quiz_screen?categoryId=${categoryId ?: -1}&difficulty=${difficulty ?: "any"}&type=${type ?: "any"}"
        }
    }
    object HighScoresScreen : Screen("high_scores_screen")
}