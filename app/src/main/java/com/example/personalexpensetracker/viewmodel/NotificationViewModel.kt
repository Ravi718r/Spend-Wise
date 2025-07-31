package com.example.personalexpensetracker.viewmodel

import androidx.lifecycle.ViewModel
import com.example.personalexpensetracker.model.Notification
import com.google.firebase.Firebase
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.firestore
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import java.util.UUID

class NotificationViewModel : ViewModel() {
    private val db = FirebaseFirestore.getInstance()
    private val _notifications = MutableStateFlow<List<Notification>>(emptyList())
    val notifications = _notifications.asStateFlow()

    fun getNotificationsForUser(userId: String) {
        db.collection("notifications")
            .whereEqualTo("userId", userId)
            .orderBy("timestamp", com.google.firebase.firestore.Query.Direction.DESCENDING)
            .addSnapshotListener { snapshot, _ ->
                if (snapshot != null) {
                    val list = snapshot.documents.mapNotNull { it.toObject(Notification::class.java) }
                    _notifications.value = list
                }
            }
    }

    fun markAsRead(notificationId: String) {
        db.collection("notifications").document(notificationId)
            .update("isRead", true)
    }

    fun addNotification(userId: String, title: String, message: String) {
        val notification = Notification(
            id = UUID.randomUUID().toString(),
            userId = userId,
            title = title,
            message = message,
            timestamp = System.currentTimeMillis(),
            isRead = false
        )

        Firebase.firestore.collection("notifications")
            .document(notification.id)
            .set(notification)
    }

}
