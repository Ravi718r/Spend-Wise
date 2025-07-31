package com.example.personalexpensetracker.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.personalexpensetracker.model.Transaction
import com.example.personalexpensetracker.model.TransactionType
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import android.util.Log
import androidx.compose.runtime.mutableStateListOf
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale

class TransactionViewModel : ViewModel() {

    private val db = FirebaseFirestore.getInstance()

    private val _totalTransactionCount = MutableStateFlow(0)
    val totalTransactionCount: StateFlow<Int> = _totalTransactionCount.asStateFlow()

    fun addTransaction(transaction: Transaction, onSuccess: () -> Unit, onFailure: (Exception) -> Unit) {
        viewModelScope.launch {
            try {
                db.collection("transactions")
                    .document(transaction.id)
                    .set(transaction)
                    .await()

                transactions.add(transaction)

                _totalTransactionCount.value = transactions.size
                onSuccess()
            } catch (e: Exception) {
                Log.e("TransactionViewModel", "Error adding transaction", e)
                onFailure(e)
            }
        }
    }

    val transactions = mutableStateListOf<Transaction>()

    fun getTransactionsByUser(userId: String) {
        viewModelScope.launch {
            try {
                val snapshot = db.collection("transactions")
                    .whereEqualTo("userId", userId)
                    .get()
                    .await()

                val list = snapshot.documents.mapNotNull { it.toObject(Transaction::class.java) }
                transactions.clear()
                transactions.addAll(list)

                _totalTransactionCount.value = transactions.size
            } catch (e: Exception) {
                Log.e("TransactionViewModel", "Lỗi khi lấy giao dịch", e)
            }
        }
    }

    fun deleteTransaction(transactionId: String, onSuccess: () -> Unit, onFailure: (Exception) -> Unit) {
        viewModelScope.launch {
            try {
                db.collection("transactions")
                    .document(transactionId)
                    .delete()
                    .await()

                transactions.removeAll { it.id == transactionId }

                _totalTransactionCount.value = transactions.size
                onSuccess()
            } catch (e: Exception) {
                Log.e("TransactionViewModel", "Lỗi khi xoá giao dịch", e)
                onFailure(e)
            }
        }
    }

    fun filterByType(type: TransactionType): List<Transaction> {
        return transactions.filter { it.type == type }
    }

    fun filterByMonthYear(month: Int, year: Int): List<Transaction> {
        return transactions.filter {
            val cal = Calendar.getInstance().apply {
                timeInMillis = it.date
            }
            cal.get(Calendar.MONTH) == month && cal.get(Calendar.YEAR) == year
        }
    }

    fun filterByCategory(category: String): List<Transaction> {
        return transactions.filter { it.category == category }
    }

    val totalIncome get() = transactions.filter { it.type == TransactionType.INCOME }.sumOf { it.amount }
    val totalExpense get() = transactions.filter { it.type == TransactionType.EXPENSE }.sumOf { it.amount }
    val balance get() = totalIncome - totalExpense

    fun getSortedTransactions(): List<Transaction> {
        return transactions.sortedByDescending { it.date }
    }

    fun getCurrentMonthTotal(type: TransactionType): Double {
        val now = Calendar.getInstance()
        return transactions.filter {
            it.type == type && isSameMonth(it.date, now)
        }.sumOf { it.amount }
    }

    private fun isSameMonth(timestamp: Long, cal: Calendar): Boolean {
        val transCal = Calendar.getInstance().apply { timeInMillis = timestamp }
        return transCal.get(Calendar.MONTH) == cal.get(Calendar.MONTH) &&
                transCal.get(Calendar.YEAR) == cal.get(Calendar.YEAR)
    }

    fun getMonthlySummary(): Map<String, Pair<Double, Double>> {
        val formatter = SimpleDateFormat("MM/yyyy", Locale.getDefault())
        return transactions.groupBy {
            formatter.format(Date(it.date))
        }.mapValues { entry ->
            val income = entry.value.filter { it.type == TransactionType.INCOME }.sumOf { it.amount }
            val expense = entry.value.filter { it.type == TransactionType.EXPENSE }.sumOf { it.amount }
            income to expense
        }.toSortedMap()
    }

    fun getExpenseByCategory(): Map<String, Double> {
        return transactions.filter { it.type == TransactionType.EXPENSE }
            .groupBy { it.category }
            .mapValues { it.value.sumOf { tx -> tx.amount } }
            .toSortedMap()
    }

    fun getTransactionsByCategoryGroupedByType(): Map<TransactionType, Map<String, Double>> {
        return transactions.groupBy { it.type }.mapValues { typeEntry ->
            typeEntry.value.groupBy { it.category }
                .mapValues { it.value.sumOf { tx -> tx.amount } }
        }
    }

    fun getTotalTransactionCount(): Int {
        return transactions.size
    }


}
