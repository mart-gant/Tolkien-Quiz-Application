package com.marcingantkowski.tolkienquizapp.presentation

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.SavedStateHandle
import com.marcingantkowski.tolkienquizapp.domain.model.HighScore
import com.marcingantkowski.tolkienquizapp.domain.model.Question
import com.marcingantkowski.tolkienquizapp.domain.use_case.GetQuestionsUseCase
import com.marcingantkowski.tolkienquizapp.domain.use_case.SaveHighScoreUseCase
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Rule
import org.junit.Test

@ExperimentalCoroutinesApi
class QuizViewModelTest {

    @get:Rule
    val instantExecutorRule = InstantTaskExecutorRule()

    private val testDispatcher = StandardTestDispatcher()

    private lateinit var getQuestionsUseCase: GetQuestionsUseCase
    private lateinit var saveHighScoreUseCase: SaveHighScoreUseCase
    private lateinit var savedStateHandle: SavedStateHandle
    private lateinit var viewModel: QuizViewModel

    @Before
    fun setUp() {
        Dispatchers.setMain(testDispatcher)
        getQuestionsUseCase = mockk()
        saveHighScoreUseCase = mockk(relaxed = true)
        savedStateHandle = SavedStateHandle()
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `loadQuestions success should update state with questions`() = runTest {
        // Given
        val questions = listOf(Question("Test Question?", listOf("A", "B"), 0))
        coEvery { getQuestionsUseCase(any(), any(), any()) } returns questions

        // When
        viewModel = QuizViewModel(getQuestionsUseCase, saveHighScoreUseCase, savedStateHandle)
        testDispatcher.scheduler.advanceUntilIdle()

        // Then
        val state = viewModel.state.value
        assertEquals(false, state.isLoading)
        assertEquals(null, state.error)
        assertEquals(questions, state.questions)
    }

    @Test
    fun `loadQuestions error should update state with error`() = runTest {
        // Given
        val errorMessage = "Test error"
        coEvery { getQuestionsUseCase(any(), any(), any()) } throws RuntimeException(errorMessage)

        // When
        viewModel = QuizViewModel(getQuestionsUseCase, saveHighScoreUseCase, savedStateHandle)
        testDispatcher.scheduler.advanceUntilIdle()

        // Then
        val state = viewModel.state.value
        assertEquals(false, state.isLoading)
        assertEquals(errorMessage, state.error)
        assertEquals(emptyList<Question>(), state.questions)
    }

    @Test
    fun `finishing quiz should save high score`() = runTest {
        // Given
        val questions = listOf(Question("Test Question?", listOf("A", "B"), 0))
        coEvery { getQuestionsUseCase(any(), any(), any()) } returns questions
        viewModel = QuizViewModel(getQuestionsUseCase, saveHighScoreUseCase, savedStateHandle)
        testDispatcher.scheduler.advanceUntilIdle()

        // When
        viewModel.onAnswerSelected(0)
        viewModel.onNextQuestionClicked()
        testDispatcher.scheduler.advanceUntilIdle()

        // Then
        coVerify(exactly = 1) { saveHighScoreUseCase(any()) }
        assertEquals(true, viewModel.state.value.isQuizFinished)
    }
}