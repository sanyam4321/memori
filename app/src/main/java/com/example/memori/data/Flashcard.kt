package com.example.memori.data

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Fts4
import androidx.room.PrimaryKey

@Entity(tableName = "flashcards")
data class Flashcard(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val deckName: String,
    val frontText: String,
    val backText: String,
    val nextReviewDate: Long = 0L,
    val interval: Int = 0,
    val easeFactor: Float = 2.5f
)

@Fts4(contentEntity = Flashcard::class)
@Entity(tableName = "flashcards_fts")
data class FlashcardFts(
    @ColumnInfo(name = "frontText") val frontText: String
)