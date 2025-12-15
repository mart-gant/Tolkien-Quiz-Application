package com.marcingantkowski.tolkienquizapp.domain.model

import androidx.annotation.Keep

@Keep
data class HighScore(
    val score: Int = 0,
    val totalQuestions: Int = 0,
    val timestamp: Long = 0
)