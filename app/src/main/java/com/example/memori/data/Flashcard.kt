package com.example.memori.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "flashcards")
data class Flashcard(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val deckName: String,
    val frontText: String,
    val backText: String,
    val nextReviewDate: Long = 0L, // SRS: When to show next
    val interval: Int = 0, // SRS: Days between reviews
    val easeFactor: Float = 2.5f // SRS: Multiplier for difficulty
)