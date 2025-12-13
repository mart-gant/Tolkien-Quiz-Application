package com.marcingantkowski.tolkienquizapp.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface QuestionDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertQuestions(questions: List<QuestionEntity>)

    @Query("DELETE FROM questions")
    suspend fun clearQuestions()

    @Query("SELECT * FROM questions")
    suspend fun getQuestions(): List<QuestionEntity>
}