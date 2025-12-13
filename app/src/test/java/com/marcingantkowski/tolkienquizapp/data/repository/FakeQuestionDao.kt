package com.marcingantkowski.tolkienquizapp.data.repository

import com.marcingantkowski.tolkienquizapp.data.local.QuestionDao
import com.marcingantkowski.tolkienquizapp.data.local.QuestionEntity

class FakeQuestionDao : QuestionDao {

    private val questions = mutableListOf<QuestionEntity>()

    override suspend fun insertQuestions(questions: List<QuestionEntity>) {
        this.questions.addAll(questions)
    }

    override suspend fun clearQuestions() {
        questions.clear()
    }

    override suspend fun getQuestions(): List<QuestionEntity> {
        return questions
    }
}