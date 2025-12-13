package com.marcingantkowski.tolkienquizapp.domain.use_case

import com.marcingantkowski.tolkienquizapp.domain.model.Question
import com.marcingantkowski.tolkienquizapp.domain.repository.QuizRepository

class GetQuestionsUseCase(
    private val repository: QuizRepository
) {
    suspend operator fun invoke(category: Int?, difficulty: String?, type: String?): List<Question> {
        return repository.getQuestions(category, difficulty, type)
    }
}