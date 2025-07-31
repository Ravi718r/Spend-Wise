package com.example.personalexpensetracker.view

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun MonthlyBarChart(data: Map<String, Pair<Double, Double>>) {
    val maxAmount = data.values.maxOfOrNull { maxOf(it.first, it.second) } ?: 1.0
    val barHeight = 120.dp

    Column(modifier = Modifier.fillMaxWidth()) {
        data.forEach { (month, pair) ->
            val incomeRatio = (pair.first / maxAmount).toFloat()
            val expenseRatio = (pair.second / maxAmount).toFloat()

            Column(modifier = Modifier.padding(vertical = 4.dp)) {
                Row(
                    verticalAlignment = Alignment.Bottom,
                    modifier = Modifier.height(barHeight)
                ) {
                    Box(
                        modifier = Modifier
                            .weight(1f)
                            .height(barHeight * incomeRatio)
                            .background(
                                color = Color(0xFF4CAF50),
                                shape = RoundedCornerShape(topStart = 4.dp, topEnd = 4.dp)
                            )
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Box(
                        modifier = Modifier
                            .weight(1f)
                            .height(barHeight * expenseRatio)
                            .background(
                                color = Color(0xFFF44336),
                                shape = RoundedCornerShape(topStart = 4.dp, topEnd = 4.dp)
                            )
                    )
                }
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(
                        text = "${String.format("%,.0f", pair.first)}",
                        fontSize = 10.sp,
                        color = Color.Gray,
                        modifier = Modifier.weight(1f)
                    )
                    Text(
                        text = month,
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Medium,
                        modifier = Modifier.padding(horizontal = 16.dp)
                    )
                    Text(
                        text = "${String.format("%,.0f", pair.second)}",
                        fontSize = 10.sp,
                        color = Color.Gray,
                        modifier = Modifier.weight(1f),
                        textAlign = TextAlign.End
                    )
                }
            }
        }
    }
}

@Composable
fun CategoryPieChart(data: Map<String, Double>) {
    val total = data.values.sum()
    val angleData = data.mapValues { (it.value / total * 360f).toFloat() }

    val colors = listOf(
        Color(0xFF4285F4), // Blue
        Color(0xFF34A853), // Green
        Color(0xFFFBBC05), // Yellow
        Color(0xFFEA4335), // Red
        Color(0xFF673AB7), // Purple
        Color(0xFFFF6D00), // Orange
        Color(0xFF00ACC1)  // Cyan
    )

    Canvas(modifier = Modifier.size(200.dp)) {
        var startAngle = -90f
        var index = 0

        angleData.forEach { (_, angle) ->
            drawArc(
                color = colors[index % colors.size],
                startAngle = startAngle,
                sweepAngle = angle,
                useCenter = true
            )
            startAngle += angle
            index++
        }
    }
}

@Composable
fun CategoryLegend(data: Map<String, Double>) {
    val colors = listOf(
        Color(0xFF4285F4),
        Color(0xFF34A853),
        Color(0xFFFBBC05),
        Color(0xFFEA4335),
        Color(0xFF673AB7),
        Color(0xFFFF6D00),
        Color(0xFF00ACC1)
    )

    val total = data.values.sum()

    Column(
        modifier = Modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        data.entries.forEachIndexed { index, entry ->
            val percentage = (entry.value / total * 100).toFloat()
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.fillMaxWidth()
            ) {
                Box(
                    modifier = Modifier
                        .size(16.dp)
                        .background(colors[index % colors.size], shape = CircleShape)
                )
                Spacer(modifier = Modifier.width(12.dp))
                Text(
                    text = entry.key,
                    fontSize = 14.sp,
                    modifier = Modifier.weight(1f)
                )
                Text(
                    text = "${String.format("%.1f", percentage)}%",
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Medium,
                    color = Color.Gray
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = "${String.format("%,.0f", entry.value)} RUP",
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Medium
                )
            }
        }
    }
}

@Composable
fun LegendItem(color: Color, text: String) {
    Row(verticalAlignment = Alignment.CenterVertically) {
        Box(
            modifier = Modifier
                .size(12.dp)
                .background(color, shape = CircleShape)
        )
        Spacer(modifier = Modifier.width(6.dp))
        Text(text, fontSize = 12.sp)
    }
}
