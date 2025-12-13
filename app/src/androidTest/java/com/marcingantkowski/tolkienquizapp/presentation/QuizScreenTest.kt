package com.marcingantkowski.tolkienquizapp.presentation

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithText
import com.marcingantkowski.tolkienquizapp.domain.model.Question
import io.mockk.every
import io.mockk.mockk
import org.junit.Rule
import org.junit.Test

class QuizScreenTest {

    @get:Rule
    val composeTestRule = createComposeRule()

    @Test
    fun quizFinished_shouldShowFinalScore() {
        // Given
        val questions = listOf(Question("Test Question?", listOf("A", "B"), 0))
        val state = QuizState(
            questions = questions,
            score = 1,
            isQuizFinished = true
        )
        val viewModel: QuizViewModel = mockk(relaxed = true) {
            every { this@mockk.state.value } returns state
        }

        // When
        composeTestRule.setContent {
            QuizScreen(quizViewModel = viewModel)
        }

        // Then
        composeTestRule.onNodeWithText("Koniec quizu!").assertIsDisplayed()
        composeTestRule.onNodeWithText("Twój wynik: 1/1").assertIsDisplayed()
    }
}