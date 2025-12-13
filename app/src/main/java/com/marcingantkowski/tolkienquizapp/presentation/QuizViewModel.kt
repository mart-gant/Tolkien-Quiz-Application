package com.marcingantkowski.tolkienquizapp.presentation

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.marcingantkowski.tolkienquizapp.domain.model.HighScore
import com.marcingantkowski.tolkienquizapp.domain.use_case.GetQuestionsUseCase
import com.marcingantkowski.tolkienquizapp.domain.use_case.SaveHighScoreUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class QuizViewModel @Inject constructor(
    private val getQuestionsUseCase: GetQuestionsUseCase,
    private val saveHighScoreUseCase: SaveHighScoreUseCase,
    private val savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val _state = mutableStateOf(QuizState())
    val state: State<QuizState> = _state

    init {
        loadQuestions()
    }

    fun onAnswerSelected(selectedAnswerIndex: Int) {
        if (state.value.selectedAnswerIndex != null) {
            return
        }

        val currentQuestion = state.value.questions[state.value.currentQuestionIndex]
        val newScore = if (selectedAnswerIndex == currentQuestion.correctAnswerIndex) {
            state.value.score + 1
        } else {
            state.value.score
        }

        _state.value = state.value.copy(
            selectedAnswerIndex = selectedAnswerIndex,
            score = newScore
        )
    }

    fun onNextQuestionClicked() {
        val nextQuestionIndex = state.value.currentQuestionIndex + 1
        if (nextQuestionIndex < state.value.questions.size) {
            _state.value = state.value.copy(
                currentQuestionIndex = nextQuestionIndex,
                selectedAnswerIndex = null
            )
        } else {
            viewModelScope.launch {
                saveHighScoreUseCase(HighScore(
                    score = state.value.score,
                    totalQuestions = state.value.questions.size,
                    timestamp = System.currentTimeMillis()
                ))
                _state.value = state.value.copy(isQuizFinished = true)
            }
        }
    }

    fun onRestartClicked() {
        loadQuestions()
    }

    private fun loadQuestions() {
        viewModelScope.launch {
            _state.value = QuizState(isLoading = true)
            try {
                val categoryId = savedStateHandle.get<Int>("categoryId")
                val difficulty = savedStateHandle.get<String>("difficulty")
                val type = savedStateHandle.get<String>("type")

                val questions = getQuestionsUseCase(
                    category = if (categoryId == -1) null else categoryId,
                    difficulty = if (difficulty == "any") null else difficulty,
                    type = if (type == "any") null else type
                )
                _state.value = QuizState(questions = questions)
            } catch (e: Exception) {
                _state.value = QuizState(error = e.localizedMessage ?: "An unexpected error occurred")
            }
        }
    }
}