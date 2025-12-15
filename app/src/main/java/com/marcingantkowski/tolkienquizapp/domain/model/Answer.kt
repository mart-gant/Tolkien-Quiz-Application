package com.marcingantkowski.tolkienquizapp.domain.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Answer(
    val text: String,
    val isCorrect: Boolean
) : Parcelable