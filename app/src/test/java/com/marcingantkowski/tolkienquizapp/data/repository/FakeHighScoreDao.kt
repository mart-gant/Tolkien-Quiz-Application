package com.marcingantkowski.tolkienquizapp.data.repository

import com.marcingantkowski.tolkienquizapp.data.local.HighScoreDao
import com.marcingantkowski.tolkienquizapp.data.local.HighScoreEntity

class FakeHighScoreDao : HighScoreDao {

    private val highScores = mutableListOf<HighScoreEntity>()

    override suspend fun insertHighScore(highScore: HighScoreEntity) {
        highScores.add(highScore)
    }

    override suspend fun getHighScores(): List<HighScoreEntity> {
        return highScores.sortedByDescending { it.score }
    }
}