package com.example.personalexpensetracker.model

data class Transaction(
    val id: String = "",
    val userId: String = "",
    val type: TransactionType = TransactionType.EXPENSE,
    val amount: Double = 0.0,
    val category: String = "",
    val note: String? = null,
    val date: Long = 0L,
    val createdAt: Long = System.currentTimeMillis(),
    val updatedAt: Long? = null
)

enum class TransactionType {
    INCOME,
    EXPENSE
}
