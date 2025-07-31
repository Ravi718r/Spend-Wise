package com.example.personalexpensetracker.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.personalexpensetracker.model.Users
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class UserViewModel : ViewModel() {

    private val auth = FirebaseAuth.getInstance()
    private val firestore = FirebaseFirestore.getInstance()

    private val _userList = MutableStateFlow<List<Users>>(emptyList())
    val userList: StateFlow<List<Users>> = _userList

    init {
        fetchUsers()
    }

    fun createUser(
        name: String,
        email: String,
        role: String = "user",
        onSuccess: () -> Unit,
        onError: (String) -> Unit
    ) {
        val userId = auth.currentUser?.uid

        if (userId == null) {
            onError("Cannot get user UID.")
            return
        }

        val user = Users(
            userId = userId,
            ten = name,
            email = email,
            role = role
        )

        viewModelScope.launch {
            firestore.collection("Users")
                .document(userId)
                .set(user)
                .addOnSuccessListener {
                    Log.d("UserViewModel", "Account creation successful")
                    onSuccess()
                }
                .addOnFailureListener { e ->
                    Log.e("UserViewModel", "Error: ${e.message}")
                    onError("Error saving user information: ${e.message}")
                }
        }
    }

    fun fetchUsers() {
        firestore.collection("Users")
            .addSnapshotListener { snapshot, error ->
                if (error != null) {
                    Log.e("UserViewModel", "Error reading list: ${error.message}")
                    return@addSnapshotListener
                }

                if (snapshot != null) {
                    val users = snapshot.documents.mapNotNull { it.toObject(Users::class.java) }
                    _userList.value = users
                }
            }
    }

    fun deleteUser(userId: String, onSuccess: () -> Unit = {}, onError: (String) -> Unit = {}) {
        firestore.collection("Users")
            .document(userId)
            .delete()
            .addOnSuccessListener {
                Log.d("UserViewModel", "User deletion successful")
                onSuccess()
            }
            .addOnFailureListener { e ->
                Log.e("UserViewModel", "Error deleting: ${e.message}")
                onError("Deletion failed: ${e.message}")
            }
    }

    fun updateUser(user: Users, onSuccess: () -> Unit = {}, onError: (String) -> Unit = {}) {
        firestore.collection("Users")
            .document(user.userId)
            .set(user)
            .addOnSuccessListener {
                Log.d("UserViewModel", "User update successful")
                onSuccess()
            }
            .addOnFailureListener { e ->
                Log.e("UserViewModel", "Error updating: ${e.message}")
                onError("Update failed: ${e.message}")
            }
    }

    fun updateUserInfo(
        user: Users,
        newPassword: String? = null,
        onSuccess: () -> Unit = {},
        onError: (String) -> Unit = {}
    ) {
        val currentUser = auth.currentUser

        if (currentUser == null) {
            onError("Current user not found.")
            return
        }

        if (!newPassword.isNullOrEmpty()) {
            currentUser.updatePassword(newPassword)
                .addOnFailureListener { e ->
                    Log.e("UserViewModel", "Password change error: ${e.message}")
                    onError("Cannot change password: ${e.message}")
                }
                .addOnSuccessListener {
                    Log.d("UserViewModel", "Password change successful")
                    updateUser(user, onSuccess, onError)
                }
        } else {
            updateUser(user, onSuccess, onError)
        }
    }

    fun getUserById(userId: String): Users? {
        return _userList.value.find { it.userId == userId }
    }

}
