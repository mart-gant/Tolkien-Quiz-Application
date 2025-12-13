package com.marcingantkowski.tolkienquizapp.domain.repository

import com.marcingantkowski.tolkienquizapp.domain.model.Category
import com.marcingantkowski.tolkienquizapp.domain.model.HighScore
import com.marcingantkowski.tolkienquizapp.domain.model.Question

interface QuizRepository {
    suspend fun getQuestions(category: Int?, difficulty: String?, type: String?): List<Question>
    suspend fun saveHighScore(highScore: HighScore)
    suspend fun getHighScores(): List<HighScore>
    suspend fun getCategories(): List<Category>
}