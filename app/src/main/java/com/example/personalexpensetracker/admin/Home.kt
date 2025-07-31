package com.example.personalexpensetracker.admin

import androidx.compose.foundation.clickable
import androidx.compose.ui.graphics.Color
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.People
import androidx.compose.material.icons.filled.BarChart
import androidx.compose.material.icons.filled.Feedback
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.text.font.FontWeight
import androidx.navigation.NavController

@Composable
fun Home(navController: NavController) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Text(
            text = "Hello, Admin 👋",
            fontSize = 24.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(bottom = 24.dp)
        )

        LazyVerticalGrid(
            columns = GridCells.Fixed(2),
            verticalArrangement = Arrangement.spacedBy(16.dp),
            horizontalArrangement = Arrangement.spacedBy(16.dp),
            modifier = Modifier.fillMaxSize()
        ) {
            item {
                AdminOptionCard(
                    icon = Icons.Default.People,
                    title = "Users",
                    backgroundColor = Color(0xFFE3F2FD),
                    iconTint = Color(0xFF1E88E5)
                ) {
                    navController.navigate("UserManagement")
                }
            }
            item {
                AdminOptionCard(
                    icon = Icons.Default.BarChart,
                    title = "Statistics",
                    backgroundColor = Color(0xFFFFF3E0),
                    iconTint = Color(0xFFFB8C00)
                ) {
                    navController.navigate("StatisticsScreen")
                }
            }
            item {
                AdminOptionCard(
                    icon = Icons.Default.Feedback,
                    title = "Feedback",
                    backgroundColor = Color(0xFFE8F5E9),
                    iconTint = Color(0xFF43A047)
                ) {
                    navController.navigate("Feedback")
                }
            }
            item {
                AdminOptionCard(
                    icon = Icons.Default.Settings,
                    title = "Settings",
                    backgroundColor = Color(0xFFF3E5F5),
                    iconTint = Color(0xFF8E24AA)
                ) {
                    navController.navigate("Settings")
                }
            }
        }
    }
}

@Composable
fun AdminOptionCard(
    icon: ImageVector,
    title: String,
    backgroundColor: Color,
    iconTint: Color,
    onClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .height(120.dp)
            .clickable { onClick() },
        colors = CardDefaults.cardColors(containerColor = backgroundColor),
        shape = RoundedCornerShape(16.dp),
        elevation = CardDefaults.cardElevation(4.dp)
    ) {
        Column(
            modifier = Modifier
                .padding(16.dp)
                .fillMaxSize(),
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            Icon(
                imageVector = icon,
                contentDescription = title,
                tint = iconTint,
                modifier = Modifier.size(32.dp)
            )
            Text(
                text = title,
                fontSize = 16.sp,
                fontWeight = FontWeight.SemiBold
            )
        }
    }
}
