package com.marcingantkowski.tolkienquizapp.domain.use_case

import com.marcingantkowski.tolkienquizapp.domain.model.HighScore
import com.marcingantkowski.tolkienquizapp.domain.repository.QuizRepository

class SaveHighScoreUseCase(private val repository: QuizRepository) {
    suspend operator fun invoke(highScore: HighScore) {
        repository.saveHighScore(highScore)
    }
}