package com.marcingantkowski.tolkienquizapp.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters

@Database(
    entities = [QuestionEntity::class, HighScoreEntity::class],
    version = 2
)
@TypeConverters(Converters::class)
abstract class QuizDatabase : RoomDatabase() {
    abstract val questionDao: QuestionDao
    abstract val highScoreDao: HighScoreDao
}