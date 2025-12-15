package com.marcingantkowski.tolkienquizapp.presentation

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.marcingantkowski.tolkienquizapp.domain.engine.QuizEngine
import com.marcingantkowski.tolkienquizapp.domain.model.HighScore
import com.marcingantkowski.tolkienquizapp.domain.model.QuizProgress
import com.marcingantkowski.tolkienquizapp.domain.use_case.GetQuestionsUseCase
import com.marcingantkowski.tolkienquizapp.domain.use_case.SaveHighScoreUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.launch
import javax.inject.Inject

private const val QUESTION_TIME_LIMIT_MILLIS = 15000L
private const val STATE_KEY_QUIZ_PROGRESS = "quizProgress"

@HiltViewModel
class QuizViewModel @Inject constructor(
    private val getQuestionsUseCase: GetQuestionsUseCase,
    private val saveHighScoreUseCase: SaveHighScoreUseCase,
    private val savedStateHandle: SavedStateHandle
) : ViewModel() {

    private var quizEngine: QuizEngine? = null
    private var timerJob: Job? = null

    private val _state = mutableStateOf(QuizState())
    val state: State<QuizState> = _state

    init {
        restoreOrLoadNewQuiz()
    }

    fun onAnswerSelected(selectedAnswerIndex: Int) {
        timerJob?.cancel()
        quizEngine?.selectAnswer(selectedAnswerIndex)
        updateStateAndSave()
    }

    fun onNextQuestionClicked() {
        quizEngine?.moveToNextQuestion()?.also { status ->
            if (status == QuizEngine.MoveToNextStatus.QUIZ_FINISHED) {
                finishQuiz()
            } else {
                updateStateAndSave()
                startTimer()
            }
        }
    }

    fun onRestartClicked() {
        timerJob?.cancel()
        savedStateHandle.remove<QuizProgress>(STATE_KEY_QUIZ_PROGRESS)
        loadNewQuestions()
    }

    private fun restoreOrLoadNewQuiz() {
        val savedProgress: QuizProgress? = savedStateHandle[STATE_KEY_QUIZ_PROGRESS]
        if (savedProgress != null) {
            quizEngine = QuizEngine(savedProgress)
            updateState()
            startTimer()
        } else {
            loadNewQuestions()
        }
    }

    private fun loadNewQuestions() {
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
                quizEngine = QuizEngine(questions)
                updateStateAndSave()
                startTimer()
            } catch (e: Exception) {
                _state.value = QuizState(error = e.localizedMessage ?: "An unexpected error occurred")
            }
        }
    }

    private fun startTimer() {
        timerJob?.cancel()
        timerJob = viewModelScope.launch {
            tickerFlow(QUESTION_TIME_LIMIT_MILLIS).collect { timeLeft ->
                _state.value = _state.value.copy(
                    timeLeftInMillis = timeLeft,
                    timeProgress = timeLeft.toFloat() / QUESTION_TIME_LIMIT_MILLIS
                )
                if (timeLeft == 0L) {
                    onTimeUp()
                }
            }
        }
    }

    private fun onTimeUp() {
        quizEngine?.skipQuestion()?.also { status ->
            if (status == QuizEngine.MoveToNextStatus.QUIZ_FINISHED) {
                finishQuiz()
            } else {
                updateStateAndSave()
                startTimer()
            }
        }
    }

    private fun updateStateAndSave() {
        updateState()
        saveState()
    }

    private fun updateState() {
        val engine = quizEngine ?: return
        _state.value = _state.value.copy(
            questions = engine.questions,
            currentQuestionIndex = engine.currentQuestionIndex,
            score = engine.score,
            selectedAnswerIndex = engine.selectedAnswerIndex,
            isQuizFinished = engine.isQuizFinished,
            isLoading = false // Ensure loading is reset
        )
    }

    private fun saveState() {
        quizEngine?.getQuizProgress()?.let {
            savedStateHandle[STATE_KEY_QUIZ_PROGRESS] = it
        }
    }

    private fun finishQuiz() {
        timerJob?.cancel()
        savedStateHandle.remove<QuizProgress>(STATE_KEY_QUIZ_PROGRESS)
        viewModelScope.launch {
            val engine = quizEngine ?: return@launch
            saveHighScoreUseCase(
                HighScore(
                    score = engine.score,
                    totalQuestions = engine.questions.size,
                    timestamp = System.currentTimeMillis()
                )
            )
            _state.value = _state.value.copy(
                isQuizFinished = true,
                totalTimeTakenInMillis = engine.totalTimeTakenInMillis,
                averageAnswerTimeInMillis = engine.averageAnswerTimeInMillis
            )
        }
    }

    private fun tickerFlow(period: Long, initialDelay: Long = 0L) = flow {
        delay(initialDelay)
        var timeLeft = period
        while (timeLeft >= 0) {
            emit(timeLeft)
            timeLeft -= 100
            delay(100)
        }
    }
}