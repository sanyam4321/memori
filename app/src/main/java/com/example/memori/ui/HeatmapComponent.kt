package com.example.memori.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.example.memori.data.ReviewLog

@Composable
fun HeatmapComponent(logs: List<ReviewLog>) {

    val todayEpoch = System.currentTimeMillis() / 86400000L
    val last7Days = (todayEpoch - 6..todayEpoch).toList()

    Column(modifier = Modifier.fillMaxWidth().padding(vertical = 16.dp)) {
        Text("Activity (Last 7 Days)", style = MaterialTheme.typography.titleMedium)
        Spacer(modifier = Modifier.height(8.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            last7Days.forEach { day ->

                val logForDay = logs.find { it.dayEpoch == day }
                val count = logForDay?.reviewCount ?: 0

                val boxColor = when {
                    count == 0 -> Color.LightGray
                    count in 1..5 -> Color(0xFFC8E6C9)
                    count in 6..15 -> Color(0xFF81C784)
                    else -> Color(0xFF388E3C)
                }

                Box(
                    modifier = Modifier
                        .size(40.dp)
                        .clip(RoundedCornerShape(4.dp))
                        .background(boxColor),
                    contentAlignment = Alignment.Center
                ) {

                    Text(text = count.toString(), color = if (count > 5) Color.White else Color.Black)
                }
            }
        }
    }
}