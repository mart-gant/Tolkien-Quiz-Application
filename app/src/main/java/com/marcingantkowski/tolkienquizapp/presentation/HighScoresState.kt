package com.marcingantkowski.tolkienquizapp.presentation

import com.marcingantkowski.tolkienquizapp.domain.model.HighScore

data class HighScoresState(
    val highScores: List<HighScore> = emptyList(),
    val isLoading: Boolean = false,
    val error: String? = null
)