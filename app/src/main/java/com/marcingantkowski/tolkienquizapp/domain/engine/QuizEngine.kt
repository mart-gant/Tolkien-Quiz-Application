package com.marcingantkowski.tolkienquizapp.domain.engine

import com.marcingantkowski.tolkienquizapp.domain.model.Question
import com.marcingantkowski.tolkienquizapp.domain.model.QuizProgress

/**
 * Manages the core logic of a quiz session.
 *
 * This class is responsible for tracking the current question, checking answers,
 * calculating the score, and managing the overall quiz flow. It is designed to be
 * self-contained and easily testable, with no dependencies on the Android framework
 * or presentation layer.
 *
 * @param questions The list of questions for this quiz session.
 */
class QuizEngine(val questions: List<Question>) {

    var currentQuestionIndex: Int = 0
        private set

    var score: Int = 0
        private set

    var selectedAnswerIndex: Int? = null
        private set

    var totalTimeTakenInMillis: Long = 0
        private set

    private var currentQuestionStartTime: Long = 0

    /**
     * Secondary constructor to restore the engine's state from a progress object.
     */
    constructor(progress: QuizProgress) : this(progress.questions) {
        this.currentQuestionIndex = progress.currentQuestionIndex
        this.score = progress.score
        this.totalTimeTakenInMillis = progress.totalTimeTakenInMillis
    }

    /**
     * Creates a snapshot of the current quiz progress.
     */
    fun getQuizProgress(): QuizProgress {
        return QuizProgress(
            questions = this.questions,
            currentQuestionIndex = this.currentQuestionIndex,
            score = this.score,
            totalTimeTakenInMillis = this.totalTimeTakenInMillis
        )
    }

    val isQuizFinished: Boolean
        get() = currentQuestionIndex >= questions.size

    val currentQuestion: Question?
        get() = questions.getOrNull(currentQuestionIndex)

    val averageAnswerTimeInMillis: Long
        get() = if (currentQuestionIndex > 0) totalTimeTakenInMillis / currentQuestionIndex else 0

    init {
        startQuestion()
    }

    enum class MoveToNextStatus {
        SUCCESS,
        QUIZ_FINISHED,
        ANSWER_NOT_SELECTED
    }

    fun selectAnswer(answerIndex: Int): Boolean {
        if (selectedAnswerIndex != null || isQuizFinished) {
            return false
        }

        recordTime()
        selectedAnswerIndex = answerIndex
        if (currentQuestion?.answers?.getOrNull(answerIndex)?.isCorrect == true) {
            score++
        }
        return true
    }

    fun moveToNextQuestion(): MoveToNextStatus {
        if (selectedAnswerIndex == null) {
            return MoveToNextStatus.ANSWER_NOT_SELECTED
        }
        return advanceToNextQuestion()
    }

    fun skipQuestion(): MoveToNextStatus {
        recordTime()
        return advanceToNextQuestion()
    }

    private fun startQuestion() {
        if (!isQuizFinished) {
            currentQuestionStartTime = System.currentTimeMillis()
        }
    }

    private fun recordTime() {
        totalTimeTakenInMillis += System.currentTimeMillis() - currentQuestionStartTime
    }

    private fun advanceToNextQuestion(): MoveToNextStatus {
        val nextIndex = currentQuestionIndex + 1
        currentQuestionIndex = nextIndex
        selectedAnswerIndex = null

        return if (isQuizFinished) {
            MoveToNextStatus.QUIZ_FINISHED
        } else {
            startQuestion()
            MoveToNextStatus.SUCCESS
        }
    }
}