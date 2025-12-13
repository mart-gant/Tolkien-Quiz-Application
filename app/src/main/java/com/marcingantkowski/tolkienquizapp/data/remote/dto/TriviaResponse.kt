package com.marcingantkowski.tolkienquizapp.data.remote.dto

import com.google.gson.annotations.SerializedName

data class TriviaResponse(
    @SerializedName("response_code")
    val responseCode: Int,
    val results: List<QuestionDto>
)