package com.marcingantkowski.tolkienquizapp.di

import android.app.Application
import androidx.room.Room
import com.marcingantkowski.tolkienquizapp.data.local.HighScoreDao
import com.marcingantkowski.tolkienquizapp.data.local.QuizDatabase
import com.marcingantkowski.tolkienquizapp.data.remote.TriviaApi
import com.marcingantkowski.tolkienquizapp.data.repository.QuizRepositoryImpl
import com.marcingantkowski.tolkienquizapp.domain.repository.QuizRepository
import com.marcingantkowski.tolkienquizapp.domain.use_case.GetCategoriesUseCase
import com.marcingantkowski.tolkienquizapp.domain.use_case.GetHighScoresUseCase
import com.marcingantkowski.tolkienquizapp.domain.use_case.GetQuestionsUseCase
import com.marcingantkowski.tolkienquizapp.domain.use_case.SaveHighScoreUseCase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun provideTriviaApi(): TriviaApi {
        return Retrofit.Builder()
            .baseUrl(TriviaApi.BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(TriviaApi::class.java)
    }

    @Provides
    @Singleton
    fun provideQuizDatabase(app: Application): QuizDatabase {
        return Room.databaseBuilder(
            app,
            QuizDatabase::class.java,
            "quiz_db"
        ).fallbackToDestructiveMigration().build()
    }

    @Provides
    @Singleton
    fun provideHighScoreDao(db: QuizDatabase): HighScoreDao {
        return db.highScoreDao
    }

    @Provides
    @Singleton
    fun provideQuizRepository(api: TriviaApi, db: QuizDatabase): QuizRepository {
        return QuizRepositoryImpl(api, db.questionDao, db.highScoreDao)
    }

    @Provides
    @Singleton
    fun provideGetQuestionsUseCase(repository: QuizRepository): GetQuestionsUseCase {
        return GetQuestionsUseCase(repository)
    }

    @Provides
    @Singleton
    fun provideSaveHighScoreUseCase(repository: QuizRepository): SaveHighScoreUseCase {
        return SaveHighScoreUseCase(repository)
    }

    @Provides
    @Singleton
    fun provideGetHighScoresUseCase(repository: QuizRepository): GetHighScoresUseCase {
        return GetHighScoresUseCase(repository)
    }

    @Provides
    @Singleton
    fun provideGetCategoriesUseCase(repository: QuizRepository): GetCategoriesUseCase {
        return GetCategoriesUseCase(repository)
    }
}