package com.marcingantkowski.tolkienquizapp.di

import com.marcingantkowski.tolkienquizapp.data.repository.QuizRepositoryImpl
import com.marcingantkowski.tolkienquizapp.domain.model.HighScore
import com.marcingantkowski.tolkienquizapp.domain.model.Question
import com.marcingantkowski.tolkienquizapp.domain.repository.QuizRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.components.SingletonComponent
import dagger.hilt.testing.TestInstallIn
import javax.inject.Singleton

@Module
@TestInstallIn(
    components = [SingletonComponent::class],
    replaces = [AppModule::class]
)
object TestAppModule {

    @Provides
    @Singleton
    fun provideTestQuizRepository(): QuizRepository {
        return object : QuizRepository {
            private val highScores = mutableListOf(HighScore(8, 10, System.currentTimeMillis()))

            override suspend fun getQuestions(category: Int?, difficulty: String?, type: String?): List<Question> {
                return listOf(Question("Test Q", listOf("A", "B"), 0))
            }

            override suspend fun saveHighScore(highScore: HighScore) {
                highScores.add(highScore)
            }

            override suspend fun getHighScores(): List<HighScore> {
                return highScores
            }
        }
    }
}