package com.marcingantkowski.tolkienquizapp.data.remote.dto

import com.google.gson.annotations.SerializedName

data class QuestionDto(
    val category: String,
    val type: String,
    val difficulty: String,
    val question: String,
    @SerializedName("correct_answer")
    val correctAnswer: String,
    @SerializedName("incorrect_answers")
    val incorrectAnswers: List<String>
)