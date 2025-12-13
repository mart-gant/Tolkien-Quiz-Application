package com.marcingantkowski.tolkienquizapp.data.remote.dto

import com.google.gson.annotations.SerializedName

data class CategoriesResponse(
    @SerializedName("trivia_categories")
    val triviaCategories: List<CategoryDto>
)