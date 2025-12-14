package com.marcingantkowski.tolkienquizapp.domain.engine

import com.marcingantkowski.tolkienquizapp.domain.model.Question
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertNull
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test

class QuizEngineTest {

    private lateinit var questions: List<Question>
    private lateinit var quizEngine: QuizEngine

    @Before
    fun setUp() {
        questions = listOf(
            Question("Question 1", listOf("A", "B", "C"), 0),
            Question("Question 2", listOf("A", "B", "C"), 1),
            Question("Question 3", listOf("A", "B", "C"), 2)
        )
        quizEngine = QuizEngine(questions)
    }

    @Test
    fun `initial state is correct`() {
        assertEquals(0, quizEngine.currentQuestionIndex)
        assertEquals(0, quizEngine.score)
        assertNull(quizEngine.selectedAnswerIndex)
        assertFalse(quizEngine.isQuizFinished)
        assertEquals(questions[0], quizEngine.currentQuestion)
    }

    @Test
    fun `selectAnswer should update selectedAnswerIndex`() {
        val result = quizEngine.selectAnswer(1)
        assertTrue(result)
        assertEquals(1, quizEngine.selectedAnswerIndex)
    }

    @Test
    fun `selecting correct answer should increment score`() {
        quizEngine.selectAnswer(0)
        assertEquals(1, quizEngine.score)
    }

    @Test
    fun `selecting incorrect answer should not increment score`() {
        quizEngine.selectAnswer(1)
        assertEquals(0, quizEngine.score)
    }

    @Test
    fun `selecting answer twice should not be allowed`() {
        quizEngine.selectAnswer(0)
        val result = quizEngine.selectAnswer(1)
        assertFalse(result)
        assertEquals(0, quizEngine.selectedAnswerIndex)
    }

    @Test
    fun `moveToNextQuestion should advance to next question`() {
        quizEngine.selectAnswer(0)
        val result = quizEngine.moveToNextQuestion()
        assertEquals(QuizEngine.MoveToNextStatus.SUCCESS, result)
        assertEquals(1, quizEngine.currentQuestionIndex)
        assertNull(quizEngine.selectedAnswerIndex)
    }

    @Test
    fun `finishing quiz should set isQuizFinished`() {
        quizEngine.selectAnswer(0)
        quizEngine.moveToNextQuestion() // Q2
        quizEngine.selectAnswer(1)
        quizEngine.moveToNextQuestion() // Q3
        quizEngine.selectAnswer(2)
        val result = quizEngine.moveToNextQuestion() // Finish

        assertEquals(QuizEngine.MoveToNextStatus.QUIZ_FINISHED, result)
        assertTrue(quizEngine.isQuizFinished)
    }
}