package com.marcingantkowski.tolkienquizapp.data.repository

import android.text.Html
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import com.marcingantkowski.tolkienquizapp.data.local.QuestionDao
import com.marcingantkowski.tolkienquizapp.data.local.QuestionEntity
import com.marcingantkowski.tolkienquizapp.data.remote.TriviaApi
import com.marcingantkowski.tolkienquizapp.data.remote.dto.CategoryDto
import com.marcingantkowski.tolkienquizapp.data.remote.dto.QuestionDto
import com.marcingantkowski.tolkienquizapp.domain.model.Answer
import com.marcingantkowski.tolkienquizapp.domain.model.Category
import com.marcingantkowski.tolkienquizapp.domain.model.HighScore
import com.marcingantkowski.tolkienquizapp.domain.model.Question
import com.marcingantkowski.tolkienquizapp.domain.repository.QuizRepository
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

private const val HIGH_SCORES_COLLECTION = "highscores"

class QuizRepositoryImpl @Inject constructor(
    private val api: TriviaApi,
    private val questionDao: QuestionDao,
    private val firestore: FirebaseFirestore
) : QuizRepository {
    override suspend fun getQuestions(category: Int?, difficulty: String?, type: String?): List<Question> {
        return try {
            val remoteQuestions = api.getQuestions(
                category = category,
                difficulty = difficulty,
                type = type
            ).results
            questionDao.clearQuestions()
            questionDao.insertQuestions(remoteQuestions.map { it.toQuestionEntity() })
            questionDao.getQuestions().map { it.toQuestion() }
        } catch (e: Exception) {
            val localQuestions = questionDao.getQuestions().map { it.toQuestion() }
            if (localQuestions.isNotEmpty()) {
                localQuestions
            } else {
                throw e
            }
        }
    }

    override suspend fun saveHighScore(highScore: HighScore) {
        firestore.collection(HIGH_SCORES_COLLECTION)
            .add(highScore)
            .await()
    }

    override suspend fun getHighScores(): List<HighScore> {
        return firestore.collection(HIGH_SCORES_COLLECTION)
            .orderBy("score", Query.Direction.DESCENDING)
            .limit(100)
            .get()
            .await()
            .toObjects(HighScore::class.java)
    }

    override suspend fun getCategories(): List<Category> {
        return api.getCategories().triviaCategories.map { it.toCategory() }
    }
}

private fun QuestionDto.toQuestionEntity(): QuestionEntity {
    val answers = (incorrectAnswers + correctAnswer).shuffled()
    val correctAnswerIndex = answers.indexOf(correctAnswer)
    return QuestionEntity(
        text = Html.fromHtml(question, Html.FROM_HTML_MODE_LEGACY).toString(),
        answers = answers.map { Html.fromHtml(it, Html.FROM_HTML_MODE_LEGACY).toString() },
        correctAnswerIndex = correctAnswerIndex
    )
}

private fun QuestionEntity.toQuestion(): Question {
    val domainAnswers = answers.mapIndexed { index, answerText ->
        Answer(
            text = answerText,
            isCorrect = (index == correctAnswerIndex)
        )
    }
    return Question(
        text = text,
        answers = domainAnswers
    )
}

private fun CategoryDto.toCategory(): Category {
    return Category(
        id = id,
        name = name
    )
}