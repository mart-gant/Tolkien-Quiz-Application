package com.marcingantkowski.tolkienquizapp.data.repository

import com.marcingantkowski.tolkienquizapp.data.remote.TriviaApi
import com.marcingantkowski.tolkienquizapp.data.remote.dto.QuestionDto
import com.marcingantkowski.tolkienquizapp.data.remote.dto.TriviaResponse
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test

@ExperimentalCoroutinesApi
class QuizRepositoryImplTest {

    private lateinit var repository: QuizRepositoryImpl
    private lateinit var api: TriviaApi
    private lateinit var questionDao: FakeQuestionDao
    private lateinit var highScoreDao: FakeHighScoreDao

    @Before
    fun setUp() {
        api = mockk()
        questionDao = FakeQuestionDao()
        highScoreDao = FakeHighScoreDao()
        repository = QuizRepositoryImpl(api, questionDao, highScoreDao)
    }

    @Test
    fun `getQuestions empty local, success remote should fetch and save`() = runTest {
        // Given
        val remoteQuestions = TriviaResponse(0, listOf(QuestionDto("Cat", "type", "diff", "Q1", "A1", listOf("A2"))))
        coEvery { api.getQuestions(any(), any(), any()) } returns remoteQuestions

        // When
        val result = repository.getQuestions(null, null, null)

        // Then
        assertEquals(1, result.size)
        assertEquals("Q1", result.first().text)
        assertEquals(1, questionDao.getQuestions().size)
    }

    @Test
    fun `getQuestions error remote, has local should return local`() = runTest {
        // Given
        questionDao.insertQuestions(listOf(com.marcingantkowski.tolkienquizapp.data.local.QuestionEntity(text="Local Q", answers= emptyList(), correctAnswerIndex = 0)))
        coEvery { api.getQuestions(any(), any(), any()) } throws RuntimeException()

        // When
        val result = repository.getQuestions(null, null, null)

        // Then
        assertEquals(1, result.size)
        assertEquals("Local Q", result.first().text)
    }
}