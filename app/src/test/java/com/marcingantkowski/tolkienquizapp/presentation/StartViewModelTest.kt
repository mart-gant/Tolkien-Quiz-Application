package com.marcingantkowski.tolkienquizapp.presentation

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.marcingantkowski.tolkienquizapp.domain.model.Category
import com.marcingantkowski.tolkienquizapp.domain.use_case.GetCategoriesUseCase
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
class StartViewModelTest {

    @get:Rule
    val instantExecutorRule = InstantTaskExecutorRule()

    private val testDispatcher = StandardTestDispatcher()

    private lateinit var getCategoriesUseCase: GetCategoriesUseCase
    private lateinit var viewModel: StartViewModel

    @Before
    fun setUp() {
        Dispatchers.setMain(testDispatcher)
        getCategoriesUseCase = mockk()
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `loadCategories success should update state with categories`() = runTest {
        // Given
        val categories = listOf(Category(9, "General Knowledge"))
        coEvery { getCategoriesUseCase() } returns categories

        // When
        viewModel = StartViewModel(getCategoriesUseCase)
        testDispatcher.scheduler.advanceUntilIdle()

        // Then
        val state = viewModel.state.value
        assertEquals(false, state.isLoading)
        assertEquals(null, state.error)
        assertEquals(categories, state.categories)
    }

    @Test
    fun `loadCategories error should update state with error`() = runTest {
        // Given
        val errorMessage = "Test error"
        coEvery { getCategoriesUseCase() } throws RuntimeException(errorMessage)

        // When
        viewModel = StartViewModel(getCategoriesUseCase)
        testDispatcher.scheduler.advanceUntilIdle()

        // Then
        val state = viewModel.state.value
        assertEquals(false, state.isLoading)
        assertEquals(errorMessage, state.error)
        assertEquals(emptyList<Category>(), state.categories)
    }
}