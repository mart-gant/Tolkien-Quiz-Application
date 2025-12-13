package com.marcingantkowski.tolkienquizapp.domain.model

data class Question(
    val text: String,
    val answers: List<String>,
    val correctAnswerIndex: Int
)