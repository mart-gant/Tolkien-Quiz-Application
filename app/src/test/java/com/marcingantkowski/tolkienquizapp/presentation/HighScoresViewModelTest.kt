package com.marcingantkowski.tolkienquizapp.presentation

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.marcingantkowski.tolkienquizapp.domain.model.HighScore
import com.marcingantkowski.tolkienquizapp.domain.use_case.GetHighScoresUseCase
import io.mockk.coEvery
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
class HighScoresViewModelTest {

    @get:Rule
    val instantExecutorRule = InstantTaskExecutorRule()

    private val testDispatcher = StandardTestDispatcher()

    private lateinit var getHighScoresUseCase: GetHighScoresUseCase
    private lateinit var viewModel: HighScoresViewModel

    @Before
    fun setUp() {
        Dispatchers.setMain(testDispatcher)
        getHighScoresUseCase = mockk()
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `loadHighScores success should update state with high scores`() = runTest {
        // Given
        val highScores = listOf(HighScore(10, 10, System.currentTimeMillis()))
        coEvery { getHighScoresUseCase() } returns highScores

        // When
        viewModel = HighScoresViewModel(getHighScoresUseCase)
        testDispatcher.scheduler.advanceUntilIdle()

        // Then
        val state = viewModel.state.value
        assertEquals(false, state.isLoading)
        assertEquals(null, state.error)
        assertEquals(highScores, state.highScores)
    }

    @Test
    fun `loadHighScores error should update state with error`() = runTest {
        // Given
        val errorMessage = "Test error"
        coEvery { getHighScoresUseCase() } throws RuntimeException(errorMessage)

        // When
        viewModel = HighScoresViewModel(getHighScoresUseCase)
        testDispatcher.scheduler.advanceUntilIdle()

        // Then
        val state = viewModel.state.value
        assertEquals(false, state.isLoading)
        assertEquals(errorMessage, state.error)
        assertEquals(emptyList<HighScore>(), state.highScores)
    }
}