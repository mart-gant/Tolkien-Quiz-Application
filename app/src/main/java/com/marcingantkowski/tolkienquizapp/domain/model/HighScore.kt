package com.marcingantkowski.tolkienquizapp.domain.model

data class HighScore(
    val score: Int,
    val totalQuestions: Int,
    val timestamp: Long
)