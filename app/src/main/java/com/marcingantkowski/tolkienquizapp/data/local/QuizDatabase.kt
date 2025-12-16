package com.marcingantkowski.tolkienquizapp.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters

@Database(
    entities = [QuestionEntity::class], // Usunięto HighScoreEntity
    version = 1, // Zmieniono wersję na 1
    exportSchema = false // Dodano, aby uniknąć ostrzeżeń o schemacie
)
@TypeConverters(Converters::class)
abstract class QuizDatabase : RoomDatabase() {
    abstract val questionDao: QuestionDao
    // Usunięto highScoreDao
}