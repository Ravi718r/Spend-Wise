package com.example.personalexpensetracker.admin

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.personalexpensetracker.viewmodel.BudgetViewModel
import com.example.personalexpensetracker.viewmodel.TransactionViewModel
import com.example.personalexpensetracker.viewmodel.UserViewModel

@Composable
fun StatisticsScreen(
    navController: NavController,
    userViewModel: UserViewModel = viewModel(),
    transactionViewModel: TransactionViewModel = viewModel(),
    budgetViewModel: BudgetViewModel = viewModel()
) {
    LaunchedEffect(Unit) {
        transactionViewModel.getTotalTransactionCount()
        budgetViewModel.loadAllBudgets()
    }

    val users by userViewModel.userList.collectAsState()
    val totalUsers = users.size

    val totalTransactions by transactionViewModel.totalTransactionCount.collectAsState()
    val budgets = budgetViewModel.budgets
    val totalBudgets = budgets.size

    Column(modifier = Modifier.fillMaxSize().padding(16.dp)) {
        Spacer(modifier = Modifier.height(40.dp))
        Text("General Statistics", fontSize = 24.sp, fontWeight = FontWeight.Bold)

        StatisticCard("Users", "$totalUsers users", Color(0xFFE1F5FE))
        StatisticCard("Transactions", "$totalTransactions transactions", Color(0xFFFFF3E0))
        StatisticCard("Budgets", "$totalBudgets budgets created", Color(0xFFE8F5E9))
    }
}


@Composable
fun StatisticCard(title: String, value: String, backgroundColor: Color) {
    Card(
        colors = CardDefaults.cardColors(containerColor = backgroundColor),
        shape = RoundedCornerShape(16.dp),
        elevation = CardDefaults.cardElevation(6.dp),
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(text = title, fontSize = 18.sp, fontWeight = FontWeight.Medium)
            Text(text = value, fontSize = 16.sp)
        }
    }
}

fun formatCurrency(amount: Double): String {
    return "%,.0f đ".format(amount)
}
