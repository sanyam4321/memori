package com.example.memori.viewmodel

import android.app.Application
import android.net.Uri
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.memori.data.AppDatabase
import com.example.memori.data.Flashcard
import com.example.memori.data.ReviewLog
import com.opencsv.CSVReader
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import java.io.InputStreamReader
import java.util.concurrent.TimeUnit


class MemoriViewModel(application: Application) : AndroidViewModel(application) {
    private val dao = AppDatabase.getDatabase(application).memoriDao()

    val dueCards = dao.getDueCards().stateIn(viewModelScope, SharingStarted.Lazily, emptyList())
    val allCards = dao.getAllCards().stateIn(viewModelScope, SharingStarted.Lazily, emptyList())
    val recentLogs = dao.getRecentLogs().stateIn(viewModelScope, SharingStarted.Lazily, emptyList())

    fun addNewCard(front: String, back: String) {
        viewModelScope.launch {
            dao.insertCard(Flashcard(deckName = "Default", frontText = front, backText = back))
        }
    }

    fun deleteCard(card: Flashcard) {
        viewModelScope.launch {
            dao.deleteCard(card)
        }
    }

    fun processReview(card: Flashcard, quality: Int) {

        var newInterval = card.interval
        var newEase = card.easeFactor
        when (quality) {
            0 -> { newInterval = 1; newEase = maxOf(1.3f, newEase - 0.2f) }
            1 -> { newInterval = maxOf(1, (newInterval * 1.2).toInt()); newEase = maxOf(1.3f, newEase - 0.15f) }
            2 -> { newInterval = if (newInterval == 0) 1 else (newInterval * 2.5).toInt() }
            3 -> { newInterval = if (newInterval == 0) 4 else (newInterval * newEase * 1.3).toInt(); newEase += 0.15f }
        }
        val nextDate = System.currentTimeMillis() + TimeUnit.DAYS.toMillis(newInterval.toLong())

        viewModelScope.launch {
            dao.updateCard(card.copy(interval = newInterval, easeFactor = newEase, nextReviewDate = nextDate))
            logDailyReview()
        }
    }

    private suspend fun logDailyReview() {
        val todayEpoch = System.currentTimeMillis() / 86400000L
        val currentLog = dao.getLogForDay(todayEpoch)

        if (currentLog != null) {
            dao.insertOrUpdateLog(currentLog.copy(reviewCount = currentLog.reviewCount + 1))
        } else {
            dao.insertOrUpdateLog(ReviewLog(dayEpoch = todayEpoch, reviewCount = 1))
        }
    }

    fun importCsv(uri: Uri) {
        viewModelScope.launch {
            try {

                val contentResolver = getApplication<Application>().contentResolver
                val inputStream = contentResolver.openInputStream(uri)

                inputStream?.let { stream ->

                    val reader = CSVReader(InputStreamReader(stream))
                    val records = reader.readAll()

                    for (row in records) {
                        if (row.size >= 2) {
                            val front = row[0].trim()
                            val back = row[1].trim()


                            dao.insertCard(
                                Flashcard(
                                    deckName = "Imported Deck",
                                    frontText = front,
                                    backText = back
                                )
                            )
                        }
                    }
                    reader.close()
                }
            } catch (e: Exception) {
                e.printStackTrace()

            }
        }
    }

}