package com.marcingantkowski.tolkienquizapp.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "questions")
data class QuestionEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val text: String,
    val answers: List<String>,
    val correctAnswerIndex: Int
)