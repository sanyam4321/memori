package com.example.memori.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "review_log")
data class ReviewLog(
    @PrimaryKey val dayEpoch: Long,
    val reviewCount: Int
)