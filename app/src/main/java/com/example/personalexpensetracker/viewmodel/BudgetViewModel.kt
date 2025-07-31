package com.example.personalexpensetracker.viewmodel

import android.util.Log
import androidx.compose.runtime.mutableStateListOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.personalexpensetracker.model.Budget
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import java.util.*

class BudgetViewModel : ViewModel() {
    private val db = FirebaseFirestore.getInstance()
    val budgets = mutableStateListOf<Budget>()

    private val _totalBudgetCount = MutableStateFlow(0)
    val totalBudgetCount = _totalBudgetCount.asStateFlow()

    // Fetch budgets for a specific user
    fun getBudgets(userId: String) {
        viewModelScope.launch {
            try {
                val snapshot = db.collection("budgets")
                    .whereEqualTo("userId", userId)
                    .get()
                    .await()

                val list = snapshot.documents.mapNotNull { it.toObject(Budget::class.java) }
                budgets.clear()
                budgets.addAll(list)
                _totalBudgetCount.value = list.size
            } catch (e: Exception) {
                Log.e("BudgetViewModel", "Error fetching budget data", e)
            }
        }
    }

    // Add a new budget entry
    fun addBudget(budget: Budget, onSuccess: () -> Unit, onFailure: (Exception) -> Unit) {
        viewModelScope.launch {
            try {
                db.collection("budgets")
                    .document(budget.id)
                    .set(budget)
                    .await()
                budgets.add(budget)
                _totalBudgetCount.value = budgets.size
                onSuccess()
            } catch (e: Exception) {
                Log.e("BudgetViewModel", "Error adding budget", e)
                onFailure(e)
            }
        }
    }

    // Get budget for a specific category in the current month and year
    fun getBudgetForCategoryThisMonth(category: String): Budget? {
        val now = Calendar.getInstance()
        val month = now.get(Calendar.MONTH) + 1
        val year = now.get(Calendar.YEAR)

        return budgets.find {
            it.category.equals(category, ignoreCase = true) &&
                    it.month == month && it.year == year
        }
    }

    // Load budgets for a given user, month, and year
    fun loadBudgets(userId: String, month: Int, year: Int) {
        viewModelScope.launch {
            try {
                val snapshot = db.collection("budgets")
                    .whereEqualTo("userId", userId)
                    .whereEqualTo("month", month + 1) // Firebase stores month as 1-12
                    .whereEqualTo("year", year)
                    .get()
                    .await()

                val list = snapshot.documents.mapNotNull { it.toObject(Budget::class.java) }
                budgets.clear()
                budgets.addAll(list)
                _totalBudgetCount.value = list.size
            } catch (e: Exception) {
                Log.e("BudgetViewModel", "Error loading budgets by month/year", e)
            }
        }
    }

    // Load all budgets irrespective of user or date
    fun loadAllBudgets() {
        viewModelScope.launch {
            try {
                val snapshot = db.collection("budgets").get().await()
                val list = snapshot.documents.mapNotNull { it.toObject(Budget::class.java) }
                budgets.clear()
                budgets.addAll(list)
                _totalBudgetCount.value = list.size
            } catch (e: Exception) {
                Log.e("BudgetViewModel", "Error loading all budgets", e)
            }
        }
    }
}
