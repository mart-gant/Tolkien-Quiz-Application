package com.marcingantkowski.tolkienquizapp.domain.use_case

import com.marcingantkowski.tolkienquizapp.domain.model.HighScore
import com.marcingantkowski.tolkienquizapp.domain.repository.QuizRepository

class GetHighScoresUseCase(private val repository: QuizRepository) {
    suspend operator fun invoke(): List<HighScore> {
        return repository.getHighScores()
    }
}