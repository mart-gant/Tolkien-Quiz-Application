package com.marcingantkowski.tolkienquizapp.presentation

import com.marcingantkowski.tolkienquizapp.domain.model.Question

data class QuizState(
    val questions: List<Question> = emptyList(),
    val currentQuestionIndex: Int = 0,
    val score: Int = 0,
    val selectedAnswerIndex: Int? = null,
    val isQuizFinished: Boolean = false,
    val isLoading: Boolean = false,
    val error: String? = null
)