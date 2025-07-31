package com.example.personalexpensetracker.view

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.expandVertically
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.AddCircle
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.FilterAlt
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.personalexpensetracker.model.Saving
import com.example.personalexpensetracker.model.SavingFilter
import com.example.personalexpensetracker.viewmodel.SavingViewModel
import java.util.Date
import java.util.Locale
import kotlin.math.abs

@Composable
fun SavingsScreen(
    navController: NavController,
    viewModel: SavingViewModel = androidx.lifecycle.viewmodel.compose.viewModel()
) {
    val savings = viewModel.savings.collectAsState().value
    var filter by remember { mutableStateOf(SavingFilter.ALL) }
    var expandedFilter by remember { mutableStateOf(false) }

    val filteredSavings = when (filter) {
        SavingFilter.ALL -> savings
        SavingFilter.COMPLETED -> savings.filter { it.completed }
        SavingFilter.INCOMPLETE -> savings.filter { !it.completed }
    }

    var showDialog by remember { mutableStateOf(false) }

    LaunchedEffect(Unit) {
        viewModel.loadSavings()
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(modifier = Modifier.height(30.dp))

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 8.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                IconButton(onClick = { navController.popBackStack() }) {
                    Icon(
                        imageVector = Icons.Default.ArrowBack,
                        contentDescription = "Back"
                    )
                }
                Text(
                    text = "Saving Goals",
                    fontSize = 22.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.Gray
                )
            }

            Box {
                IconButton(onClick = { expandedFilter = true }) {
                    Icon(
                        imageVector = Icons.Default.FilterAlt,
                        contentDescription = "Filter"
                    )
                }

                DropdownMenu(
                    expanded = expandedFilter,
                    onDismissRequest = { expandedFilter = false },
                    modifier = Modifier
                        .background(Color.White)
                        .clip(RoundedCornerShape(12.dp))
                ) {
                    DropdownMenuItem(
                        text = { Text("All", color = Color.Black) },
                        onClick = {
                            filter = SavingFilter.ALL
                            expandedFilter = false
                        }
                    )
                    DropdownMenuItem(
                        text = { Text("Completed", color = Color.Black) },
                        onClick = {
                            filter = SavingFilter.COMPLETED
                            expandedFilter = false
                        }
                    )
                    DropdownMenuItem(
                        text = { Text("Incomplete", color = Color.Black) },
                        onClick = {
                            filter = SavingFilter.INCOMPLETE
                            expandedFilter = false
                        }
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(20.dp))

        Card(
            modifier = Modifier.fillMaxWidth(),
            elevation = CardDefaults.cardElevation(6.dp),
            shape = RoundedCornerShape(20.dp),
            colors = CardDefaults.cardColors(containerColor = Color.White)
        ) {
            Column(
                modifier = Modifier.padding(24.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                val total = savings.sumOf { it.amount }
                Text(
                    "Total Saved",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.Black
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    "${"%,.0f".format(total)}đ",
                    fontSize = 30.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.Black
                )
            }
        }

        Spacer(modifier = Modifier.height(26.dp))

        LazyColumn(
            modifier = Modifier.fillMaxWidth(),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            items(filteredSavings) { saving ->
                SavingItem(
                    saving = saving,
                    onComplete = {
                        viewModel.updateSaving(it.copy(completed = true))
                    }
                )
            }
        }

        Spacer(modifier = Modifier.weight(1f))
    }

    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.BottomEnd) {
        FloatingActionButton(
            onClick = { showDialog = true },
            containerColor = Color.Black,
            modifier = Modifier.padding(16.dp)
        ) {
            Icon(imageVector = Icons.Default.Add, contentDescription = "Add", tint = Color.White)
        }
    }

    if (showDialog) {
        AddSavingDialog(
            onDismiss = { showDialog = false },
            onAdd = { saving ->
                viewModel.addSaving(saving)
                showDialog = false
            }
        )
    }
}

val vibrantColors = listOf(
    Color(0xFFF44336),
    Color(0xFFE91E63),
    Color(0xFF9C27B0),
    Color(0xFF673AB7),
    Color(0xFF3F51B5),
    Color(0xFF2196F3),
    Color(0xFF03A9F4),
    Color(0xFF009688),
    Color(0xFFFF9800),
    Color(0xFFFF5722)
)

fun formatTimestamp(timestamp: Long): String {
    val date = Date(timestamp)
    val format = java.text.SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
    return format.format(date)
}

@Composable
fun SavingItem(
    saving: Saving,
    onComplete: (Saving) -> Unit = {},
    onAddMoney: (Saving) -> Unit = {}
) {
    var expanded by remember { mutableStateOf(false) }

    val backgroundColor = remember(saving.id) {
        vibrantColors[abs(saving.id.hashCode()) % vibrantColors.size]
    }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { expanded = !expanded },
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = backgroundColor),
        elevation = CardDefaults.cardElevation(4.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(
                    text = saving.title,
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.White,
                    modifier = Modifier.weight(1f)
                )
                Text(
                    text = formatTimestamp(saving.timestamp),
                    fontSize = 14.sp,
                    color = Color.White.copy(alpha = 0.7f),
                    modifier = Modifier.padding(end = 8.dp)
                )
                Text(
                    text = if (expanded) "Hide" else "View",
                    color = Color.White.copy(alpha = 0.7f),
                    fontSize = 14.sp
                )
            }

            AnimatedVisibility(
                visible = expanded,
                enter = expandVertically(),
                exit = shrinkVertically()
            ) {
                Column {
                    Spacer(modifier = Modifier.height(8.dp))

                    val goal = saving.goalAmount.takeIf { it != 0.0 } ?: 1.0
                    val progress = (saving.amount / goal).coerceIn(0.0, 1.0)
                    val percent = (progress * 100).toInt()

                    LinearProgressIndicator(
                        progress = progress.toFloat(),
                        color = Color.White,
                        trackColor = Color.White.copy(alpha = 0.3f),
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(6.dp)
                    )

                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        "Saved: ${"%,.0f".format(saving.amount)}đ / ${"%,.0f".format(goal)}đ ($percent%)",
                        color = Color.White
                    )

                    if (saving.note.isNotBlank()) {
                        Text("Note: ${saving.note}", fontSize = 14.sp, color = Color.White.copy(alpha = 0.8f))
                    }

                    Spacer(modifier = Modifier.height(8.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        if (!saving.completed && saving.amount >= goal) {
                            Button(
                                onClick = { onComplete(saving) },
                                colors = ButtonDefaults.buttonColors(containerColor = Color.White),
                            ) {
                                Text("Mark Completed", color = backgroundColor)
                            }
                        } else if (saving.completed) {
                            Text(
                                "Completed",
                                fontWeight = FontWeight.Bold,
                                color = Color.Yellow,
                                modifier = Modifier.padding(end = 8.dp)
                            )
                        }

                        if (!saving.completed) {
                            TextButton(
                                onClick = { onAddMoney(saving) },
                                modifier = Modifier.padding(start = 8.dp)
                            ) {
                                Icon(
                                    imageVector = Icons.Default.AddCircle,
                                    contentDescription = "Add more",
                                    tint = Color.White
                                )
                                Spacer(modifier = Modifier.width(4.dp))
                                Text("Add more", color = Color.White)
                            }
                        }
                    }
                }
            }
        }
    }
}
