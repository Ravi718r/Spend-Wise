package com.example.personalexpensetracker.model

data class Saving(
    val id: String = "",
    val userId: String = "",
    val title: String = "",
    val amount: Double = 0.0,
    val goalAmount: Double? = null,
    val completed: Boolean = false,
    val category: String = "Khác",
    val note: String = "",
    val timestamp: Long = System.currentTimeMillis()
)
