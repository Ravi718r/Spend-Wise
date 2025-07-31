//package com.example.personalexpensetracker.view
//
//import androidx.compose.foundation.layout.Column
//import androidx.compose.foundation.layout.fillMaxSize
//import androidx.compose.foundation.layout.fillMaxWidth
//import androidx.compose.foundation.layout.padding
//import androidx.compose.foundation.lazy.LazyColumn
//import androidx.compose.foundation.lazy.items
//import androidx.compose.material.MaterialTheme
//import androidx.compose.material.Tab
//import androidx.compose.material.TabRow
//import androidx.compose.runtime.Composable
//import androidx.compose.runtime.getValue
//import androidx.compose.runtime.mutableStateOf
//import androidx.compose.runtime.remember
//import androidx.compose.runtime.setValue
//import androidx.compose.ui.Modifier
//import androidx.compose.material.Text
//import androidx.compose.ui.unit.dp
//import androidx.navigation.NavController
//import com.example.personalexpensetracker.model.Transaction
//import com.example.personalexpensetracker.model.TransactionType
//import com.example.personalexpensetracker.viewmodel.TransactionViewModel
//
//@Composable
//fun AllTransactionsScreen(
//    navController: NavController,
//    viewModel: TransactionViewModel )
//{
//    var selectedTab by remember { mutableStateOf(0) }
//
//    val incomeTransactions = viewModel.transactions.filter { it.type == TransactionType.INCOME }
//    val expenseTransactions = viewModel.transactions.filter { it.type == TransactionType.EXPENSE }
//
//    Column(modifier = Modifier.fillMaxSize()) {
//        TabRow(selectedTabIndex = selectedTab) {
//            Tab(selected = selectedTab == 0, onClick = { selectedTab = 0 }, text = { Text("Thu nhập") })
//            Tab(selected = selectedTab == 1, onClick = { selectedTab = 1 }, text = { Text("Chi tiêu") })
//        }
//
//        LazyColumn(modifier = Modifier.padding(12.dp)) {
//            val list = if (selectedTab == 0) incomeTransactions else expenseTransactions
//            items(list) { transaction ->
//                TransactionCard(
//                    transaction = transaction,
//                    userId = transaction.userId,
//                    viewModel = viewModel
//                )
//            }
//        }
//    }
//}
