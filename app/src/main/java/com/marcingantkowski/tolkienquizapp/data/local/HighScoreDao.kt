package com.marcingantkowski.tolkienquizapp.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface HighScoreDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertHighScore(highScore: HighScoreEntity)

    @Query("SELECT * FROM high_scores ORDER BY score DESC, timestamp DESC")
    suspend fun getHighScores(): List<HighScoreEntity>
}