package com.marcingantkowski.tolkienquizapp.data.remote

import com.marcingantkowski.tolkienquizapp.data.remote.dto.CategoriesResponse
import com.marcingantkowski.tolkienquizapp.data.remote.dto.TriviaResponse
import retrofit2.http.GET
import retrofit2.http.Query

interface TriviaApi {

    @GET("api.php")
    suspend fun getQuestions(
        @Query("amount") amount: Int = 10,
        @Query("category") category: Int?,
        @Query("difficulty") difficulty: String?,
        @Query("type") type: String?
    ): TriviaResponse

    @GET("api_category.php")
    suspend fun getCategories(): CategoriesResponse

    companion object {
        const val BASE_URL = "https://opentdb.com/"
    }
}