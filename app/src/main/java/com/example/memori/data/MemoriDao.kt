package com.example.memori.data

import androidx.room.*
import kotlinx.coroutines.flow.Flow

@Dao
interface MemoriDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertCard(card: Flashcard)

    @Update
    suspend fun updateCard(card: Flashcard)

    @Delete
    suspend fun deleteCard(card: Flashcard)

    @Query("SELECT * FROM flashcards")
    fun getAllCards(): Flow<List<Flashcard>>

    @Query("SELECT * FROM flashcards WHERE nextReviewDate <= :currentDate")
    fun getDueCards(currentDate: Long = System.currentTimeMillis()): Flow<List<Flashcard>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertOrUpdateLog(log: ReviewLog)

    @Query("SELECT * FROM review_log WHERE dayEpoch = :day")
    suspend fun getLogForDay(day: Long): ReviewLog?

    @Query("SELECT * FROM review_log ORDER BY dayEpoch DESC LIMIT 7")
    fun getRecentLogs(): Flow<List<ReviewLog>>
}