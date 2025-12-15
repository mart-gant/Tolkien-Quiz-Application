package com.marcingantkowski.tolkienquizapp.domain.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class QuizProgress(
    val questions: List<Question>,
    val currentQuestionIndex: Int,
    val score: Int,
    val totalTimeTakenInMillis: Long
) : Parcelable