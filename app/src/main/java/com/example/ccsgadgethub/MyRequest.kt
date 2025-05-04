package com.example.ccsgadgethub.ui

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.ccsgadgethub.R
import com.example.ccsgadgethub.viewmodel.RequestItem
import com.example.ccsgadgethub.viewmodel.RequestViewModel

@Composable
fun MyRequestsScreen(navController: NavController, viewModel: RequestViewModel) {
    val requests = viewModel.requests
    var searchQuery by remember { mutableStateOf("") }
    var selectedStatus by remember { mutableStateOf("Pending") }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFFCF3E8))
            .padding(horizontal = 16.dp)
    ) {
        Spacer(modifier = Modifier.height(WindowInsets.statusBars.asPaddingValues().calculateTopPadding()))

        // Top Bar
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 8.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(36.dp)
                    .clip(CircleShape)
                    .background(Color(0xFFDE6A00))
                    .clickable { navController.popBackStack() },
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Default.ArrowBack,
                    contentDescription = "Back",
                    tint = Color.White,
                    modifier = Modifier.size(20.dp)
                )
            }

            Image(
                painter = painterResource(id = R.drawable.logo),
                contentDescription = "Logo",
                modifier = Modifier.height(60.dp),
                contentScale = ContentScale.Fit
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        Text("My Requests", fontWeight = FontWeight.Bold, fontSize = 28.sp, color = Color(0xFF4A4A4A))

        Spacer(modifier = Modifier.height(16.dp))

        Row(modifier = Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
            OutlinedTextField(
                value = searchQuery,
                onValueChange = { searchQuery = it },
                placeholder = { Text("Search by item name...") },
                modifier = Modifier
                    .weight(1f)
                    .height(56.dp)
                    .background(Color.White),
                shape = RoundedCornerShape(8.dp),
                colors = OutlinedTextFieldDefaults.colors(
                    unfocusedContainerColor = Color.White,
                    focusedContainerColor = Color(0xFFF1F1F1)
                )
            )

            Spacer(modifier = Modifier.width(8.dp))

            var expanded by remember { mutableStateOf(false) }
            Box {
                OutlinedButton(
                    onClick = { expanded = true },
                    modifier = Modifier.height(56.dp),
                    colors = ButtonDefaults.outlinedButtonColors(containerColor = Color.White)
                ) {
                    Text(selectedStatus, color = Color.Black)
                }

                DropdownMenu(expanded = expanded, onDismissRequest = { expanded = false }) {
                    listOf("Pending", "Approved", "Returned").forEach { status ->
                        DropdownMenuItem(text = { Text(status) }, onClick = {
                            selectedStatus = status
                            expanded = false
                        })
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Using Surface for shadow effect
        Surface(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(10.dp),
            shadowElevation = 4.dp
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(Color.White, RoundedCornerShape(10.dp))
            ) {
                Row(modifier = Modifier
                    .fillMaxWidth()
                    .padding(12.dp)) {
                    Text("Item", Modifier.weight(1f), fontWeight = FontWeight.Bold, color = Color(0xFF6A6A6A))
                    Text("Request Date", Modifier.weight(1f), fontWeight = FontWeight.Bold, color = Color(0xFF6A6A6A))
                    Text("Status", Modifier.weight(1f), fontWeight = FontWeight.Bold, color = Color(0xFF6A6A6A))
                    Text("Return Time", Modifier.weight(1f), fontWeight = FontWeight.Bold, color = Color(0xFF6A6A6A))
                    Text("Action", Modifier.weight(0.7f), fontWeight = FontWeight.Bold, color = Color(0xFF6A6A6A))
                }

                Divider()

                requests.filter {
                    it.status == selectedStatus && it.item.contains(searchQuery, ignoreCase = true)
                }.forEach { request ->
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 12.dp, vertical = 16.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(request.item, Modifier.weight(1f))
                        Text(request.requestDate, Modifier.weight(1f))
                        Box(
                            modifier = Modifier
                                .weight(1f)
                                .background(
                                    when (request.status) {
                                        "Pending" -> Color(0xFFFFF0A4)
                                        "Approved" -> Color(0xFF89E27D)
                                        else -> Color(0xFFFC8F8F)
                                    },
                                    RoundedCornerShape(12.dp)
                                )
                                .padding(horizontal = 10.dp, vertical = 4.dp)
                        ) {
                            Text(request.status, color = Color(0xFF9E7700), fontSize = 14.sp)
                        }
                        Text(request.returnTime, Modifier.weight(1f))
                        Button(
                            onClick = { /* View details */ },
                            modifier = Modifier.weight(0.7f),
                            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF43A9E3))
                        ) {
                            Text("View", color = Color.White)
                        }
                    }
                }
            }
        }
    }
}
