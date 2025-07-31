package com.example.personalexpensetracker.view

import android.content.Intent
import android.provider.MediaStore
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowBackIosNew
import androidx.compose.material.icons.filled.Mic
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.personalexpensetracker.model.Transaction
import com.example.personalexpensetracker.model.TransactionType
import com.example.personalexpensetracker.viewmodel.NotificationViewModel
import com.example.personalexpensetracker.viewmodel.TransactionViewModel
import java.util.*

@Composable
fun AddTransactionDialog(
    userId: String,
    onDismiss: () -> Unit,
    navController: NavController,
    viewModel: TransactionViewModel,
    notificationViewModel: NotificationViewModel = viewModel()
) {
    val context = LocalContext.current

    var amount by remember { mutableStateOf("") }
    var category by remember { mutableStateOf("") }
    var note by remember { mutableStateOf("") }
    var type by remember { mutableStateOf(TransactionType.EXPENSE) }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = {
            Text("Add Transaction", fontWeight = FontWeight.Bold)
        },

        text = {
            Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                // Category + mic icon
                TextField(
                    value = category,
                    onValueChange = { category = it },
                    label = { Text("Category") },
                    shape = RoundedCornerShape(12.dp),
                    modifier = Modifier.fillMaxWidth(),
                    trailingIcon = {
                        IconButton(onClick = {
                            // Open default sound recording app
                            val intent = Intent(MediaStore.Audio.Media.RECORD_SOUND_ACTION)
                            if (intent.resolveActivity(context.packageManager) != null) {
                                context.startActivity(intent)
                            } else {
                                Toast.makeText(context, "Device does not support recording", Toast.LENGTH_SHORT).show()
                            }
                        }) {
                            Icon(
                                imageVector = Icons.Default.Mic,
                                contentDescription = "Record category"
                            )
                        }
                    },
                    colors = TextFieldDefaults.colors(
                        focusedContainerColor = Color.Transparent,
                        unfocusedContainerColor = Color.Transparent
                    )
                )

                // Amount
                TextField(
                    value = amount,
                    onValueChange = { amount = it },
                    label = { Text("Amount") },
                    shape = RoundedCornerShape(12.dp),
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    modifier = Modifier.fillMaxWidth(),
                    colors = TextFieldDefaults.colors(
                        focusedContainerColor = Color.Transparent,
                        unfocusedContainerColor = Color.Transparent
                    )
                )

                // Note + mic icon
                TextField(
                    value = note,
                    onValueChange = { note = it },
                    label = { Text("Note") },
                    shape = RoundedCornerShape(12.dp),
                    modifier = Modifier.fillMaxWidth(),
                    trailingIcon = {
                        IconButton(onClick = {
                            val intent = Intent(MediaStore.Audio.Media.RECORD_SOUND_ACTION)
                            if (intent.resolveActivity(context.packageManager) != null) {
                                context.startActivity(intent)
                            } else {
                                Toast.makeText(context, "Device does not support recording", Toast.LENGTH_SHORT).show()
                            }
                        }) {
                            Icon(
                                imageVector = Icons.Default.Mic,
                                contentDescription = "Record note"
                            )
                        }
                    },
                    colors = TextFieldDefaults.colors(
                        focusedContainerColor = Color.Transparent,
                        unfocusedContainerColor = Color.Transparent
                    )
                )

                // Transaction type dropdown
                DropdownMenuBox(
                    selectedType = type,
                    onTypeSelected = { type = it }
                )
            }
        },
        confirmButton = {
            Button(
                onClick = {
                    val amountDouble = amount.toDoubleOrNull()
                    if (amountDouble == null || category.isBlank()) return@Button

                    val calendar = Calendar.getInstance(TimeZone.getTimeZone("Asia/Kolkata"))
                    val currentDate = calendar.timeInMillis

                    val transaction = Transaction(
                        id = UUID.randomUUID().toString(),
                        userId = userId,
                        type = type,
                        amount = amountDouble,
                        category = category,
                        note = note.takeIf { it.isNotBlank() },
                        date = currentDate
                    )

                    viewModel.addTransaction(
                        transaction,
                        onSuccess = {
                            viewModel.getTransactionsByUser(userId)
                            notificationViewModel.addNotification(
                                userId = userId,
                                title = "New Transaction",
                                message = "You just added a ${if (type == TransactionType.EXPENSE) "expense"
                                else "income"}: ${String.format("%,.0f", amountDouble)} RUP for $category."
                            )
                            onDismiss()
                        },
                        onFailure = { onDismiss() }
                    )
                },
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color.Black,
                    contentColor = Color.White
                )
            ) {
                Text("Save")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Cancel", color = Color.Gray)
            }
        },
        shape = RoundedCornerShape(16.dp)
    )
}

@Composable
fun DropdownMenuBox(
    selectedType: TransactionType,
    onTypeSelected: (TransactionType) -> Unit
) {
    var expanded by remember { mutableStateOf(false) }

    Box {
        Button(
            onClick = { expanded = true },
            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFEEEEEE))
        ) {
            Text(
                when (selectedType) {
                    TransactionType.EXPENSE -> "Expense"
                    TransactionType.INCOME -> "Income"
                },
                color = Color.Black
            )
        }

        DropdownMenu(expanded = expanded, onDismissRequest = { expanded = false }) {
            DropdownMenuItem(
                text = { Text("Expense") },
                onClick = {
                    onTypeSelected(TransactionType.EXPENSE)
                    expanded = false
                }
            )
            DropdownMenuItem(
                text = { Text("Income") },
                onClick = {
                    onTypeSelected(TransactionType.INCOME)
                    expanded = false
                }
            )
        }
    }
}

@Composable
fun SummaryItem(title: String, amount: Double, color: Color) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Text(text = title, fontWeight = FontWeight.Bold, fontSize = 13.sp)
        Spacer(modifier = Modifier.height(4.dp))
        Text(
            text = "${String.format("%,.0f", amount)} RUP",
            color = color,
            fontSize = 10.sp,
            fontWeight = FontWeight.Bold
        )
    }
}
