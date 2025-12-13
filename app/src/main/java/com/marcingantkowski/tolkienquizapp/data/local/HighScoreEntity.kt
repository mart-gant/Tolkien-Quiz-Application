package com.marcingantkowski.tolkienquizapp.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "high_scores")
data class HighScoreEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val score: Int,
    val totalQuestions: Int,
    val timestamp: Long
)