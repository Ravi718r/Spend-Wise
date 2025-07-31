package com.example.personalexpensetracker.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.personalexpensetracker.model.Saving
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import java.util.*

class SavingViewModel : ViewModel() {

    private val db = FirebaseFirestore.getInstance()
    private val auth = FirebaseAuth.getInstance()

    private val _savings = MutableStateFlow<List<Saving>>(emptyList())
    val savings: StateFlow<List<Saving>> = _savings

    fun loadSavings() {
        val userId = auth.currentUser?.uid ?: return

        viewModelScope.launch {
            try {
                val snapshot = db.collection("savings")
                    .whereEqualTo("userId", userId)
                    .get()
                    .await()

                val list = snapshot.documents.mapNotNull { doc ->
                    doc.toObject(Saving::class.java)?.copy(id = doc.id)
                }

                _savings.value = list
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    fun addSaving(saving: Saving) {
        val userId = auth.currentUser?.uid ?: return

        val newSaving = saving.copy(
            userId = userId,
            timestamp = System.currentTimeMillis()
        )

        viewModelScope.launch {
            try {
                db.collection("savings")
                    .add(newSaving)
                    .await()
                loadSavings()
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    fun deleteSaving(id: String) {
        viewModelScope.launch {
            try {
                db.collection("savings").document(id).delete().await()
                loadSavings()
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    fun updateSaving(saving: Saving) {
        viewModelScope.launch {
            try {
                db.collection("savings").document(saving.id)
                    .set(saving)
                    .await()
                loadSavings()
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    fun markAsCompleted(id: String) {
        val saving = _savings.value.find { it.id == id } ?: return
        val updated = saving.copy(completed = true)
        updateSaving(updated)
    }

    fun addAmountToSaving(saving: Saving, amountToAdd: Double) {
        val updated = saving.copy(
            amount = saving.amount + amountToAdd,
            completed = saving.goalAmount?.let { saving.amount + amountToAdd >= it } == true
        )
        updateSaving(updated)
    }

}
