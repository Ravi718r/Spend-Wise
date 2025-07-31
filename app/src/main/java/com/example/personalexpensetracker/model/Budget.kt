package com.example.personalexpensetracker.model

data class Budget(
    val id: String = "",
    val userId: String = "",
    val category: String = "",
    val amount: Double = 0.0,
    val month: Int = 0,
    val year: Int = 0
)
