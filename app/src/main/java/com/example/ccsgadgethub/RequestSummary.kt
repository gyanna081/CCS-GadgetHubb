package com.example.ccsgadgethub

import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.ccsgadgethub.viewmodel.RequestViewModel
import com.example.ccsgadgethub.viewmodel.ItemViewModel
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RequestSummary(
    navController: NavController,
    requestId: String,
    requestViewModel: RequestViewModel,
    itemViewModel: ItemViewModel
) {
    val coroutineScope = rememberCoroutineScope()

    // Fetch data when the screen is shown
    LaunchedEffect(Unit) {
        coroutineScope.launch {
            requestViewModel.fetchRequests()
            itemViewModel.fetchItems()
        }
    }

    // Get requests and items
    val requests by requestViewModel.requests.collectAsState()
    val items by itemViewModel.items.collectAsState()

    // Find the specific request
    val request = requests.find { it.id == requestId }

    // Find the associated item
    val item = items.find { it.id == request?.itemId }

    // Local UI state
    var showCancelDialog by remember { mutableStateOf(false) }
    var showSuccessDialog by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFFCF3E8))
            .verticalScroll(rememberScrollState())
            .padding(16.dp)
    ) {
        // Top Bar
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 16.dp),
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
                    imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                    contentDescription = "Back",
                    tint = Color.White,
                    modifier = Modifier.size(20.dp)
                )
            }

            Text(
                text = "Request Summary",
                fontSize = 22.sp,
                fontWeight = FontWeight.Bold,
                color = Color(0xFFDE6A00)
            )

            Spacer(modifier = Modifier.width(36.dp)) // For alignment
        }

        Spacer(modifier = Modifier.height(16.dp))

        if (request == null) {
            // Request not found state
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    CircularProgressIndicator(color = Color(0xFFDE6A00))
                    Spacer(modifier = Modifier.height(16.dp))
                    Text(
                        text = "Loading request details...",
                        fontSize = 16.sp,
                        color = Color.Gray
                    )
                }
            }
        } else {
            // Request details card
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 16.dp),
                shape = RoundedCornerShape(12.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White)
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp)
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(
                            text = "Request #${request.id}",
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Bold
                        )

                        // Status badge
                        Box(
                            modifier = Modifier
                                .background(
                                    when (request.status) {
                                        "Pending" -> Color(0xFFFFF0A4)
                                        "Approved" -> Color(0xFF89E27D)
                                        "Returned" -> Color(0xFF89C4E2)
                                        else -> Color(0xFFFC8F8F)
                                    },
                                    RoundedCornerShape(12.dp)
                                )
                                .padding(horizontal = 12.dp, vertical = 4.dp)
                        ) {
                            Text(
                                text = request.status ?: "Unknown",
                                color = when (request.status) {
                                    "Pending" -> Color(0xFF9E7700)
                                    "Approved" -> Color(0xFF125C00)
                                    "Returned" -> Color(0xFF004080)
                                    else -> Color(0xFF800000)
                                },
                                fontWeight = FontWeight.Bold,
                                fontSize = 14.sp
                            )
                        }
                    }

                    Divider(modifier = Modifier.padding(vertical = 12.dp))

                    // Item details section
                    Text(
                        text = "Item Details",
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF4A4A4A)
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        // Item image (using a placeholder if item is null)
                        Box(
                            modifier = Modifier
                                .size(80.dp)
                                .clip(RoundedCornerShape(8.dp))
                                .background(Color.LightGray)
                        ) {
                            if (item != null) {
                                Image(
                                    painter = painterResource(id = R.drawable.logo), // Replace with actual item image
                                    contentDescription = "Item Image",
                                    modifier = Modifier.fillMaxSize(),
                                    contentScale = ContentScale.Crop
                                )
                            } else {
                                Text(
                                    text = request.itemName?.firstOrNull()?.toString() ?: "?",
                                    modifier = Modifier.align(Alignment.Center),
                                    fontSize = 24.sp,
                                    color = Color.White
                                )
                            }
                        }

                        Spacer(modifier = Modifier.width(16.dp))

                        Column {
                            Text(
                                text = request.itemName ?: "Unknown Item",
                                fontSize = 18.sp,
                                fontWeight = FontWeight.Bold
                            )

                            Spacer(modifier = Modifier.height(4.dp))

                            Text(
                                text = "Item ID: ${request.itemId}",
                                fontSize = 14.sp,
                                color = Color.Gray
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    // Request details section
                    Text(
                        text = "Request Details",
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF4A4A4A)
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    DetailRow("Request Date", request.requestDate ?: "Not specified")
                    DetailRow("Borrow Date", request.borrowDate ?: "Not specified")
                    DetailRow("Time Range", request.timeRange ?: "Not specified")
                    DetailRow("Return Time", request.returnTime ?: "Not specified")

                    Spacer(modifier = Modifier.height(16.dp))

                    // Purpose section
                    Text(
                        text = "Purpose",
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF4A4A4A)
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .background(Color(0xFFF5F5F5), RoundedCornerShape(8.dp))
                            .padding(12.dp)
                    ) {
                        Text(
                            text = request.purpose ?: "No purpose specified",
                            fontSize = 14.sp,
                            color = Color(0xFF4A4A4A)
                        )
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    // Borrower details section
                    Text(
                        text = "Borrower Details",
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF4A4A4A)
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    DetailRow("Name", request.borrowerName ?: "Not specified")
                    DetailRow("Email", request.borrowerEmail ?: "Not specified")

                    Spacer(modifier = Modifier.height(24.dp))

                    // Action buttons based on status
                    when (request.status) {
                        "Pending" -> {
                            Button(
                                onClick = { showCancelDialog = true },
                                modifier = Modifier.fillMaxWidth(),
                                colors = ButtonDefaults.buttonColors(containerColor = Color.Red),
                                shape = RoundedCornerShape(8.dp)
                            ) {
                                Text("Cancel Request", color = Color.White)
                            }
                        }
                        "Approved" -> {
                            Button(
                                onClick = {
                                    coroutineScope.launch {
                                        val updateData = mapOf("status" to "Returned")
                                        // Get a non-null request ID
                                        val nonNullRequestId = request.id ?: ""
                                        requestViewModel.updateRequest(nonNullRequestId, updateData)
                                        showSuccessDialog = true
                                    }
                                },
                                modifier = Modifier.fillMaxWidth(),
                                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF4CAF50)),
                                shape = RoundedCornerShape(8.dp)
                            ) {
                                Text("Mark as Returned", color = Color.White)
                            }
                        }
                        else -> {
                            // No action needed for returned or denied requests
                        }
                    }
                }
            }
        }
    }

    // Cancel confirmation dialog
    if (showCancelDialog) {
        AlertDialog(
            onDismissRequest = { showCancelDialog = false },
            title = { Text("Cancel Request") },
            text = { Text("Are you sure you want to cancel this request?") },
            confirmButton = {
                Button(
                    onClick = {
                        // Cancel logic
                        coroutineScope.launch {
                            val updateData = mapOf("status" to "Cancelled")
                            // Using non-nullable requestId from function parameter
                            requestViewModel.updateRequest(requestId, updateData)
                            showCancelDialog = false
                            showSuccessDialog = true
                        }
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = Color.Red)
                ) {
                    Text("Yes, Cancel", color = Color.White)
                }
            },
            dismissButton = {
                OutlinedButton(onClick = { showCancelDialog = false }) {
                    Text("No, Keep Request")
                }
            }
        )
    }

    // Success dialog
    if (showSuccessDialog) {
        AlertDialog(
            onDismissRequest = { showSuccessDialog = false },
            title = { Text("Request Updated") },
            text = { Text("Your request has been successfully updated.") },
            confirmButton = {
                Button(
                    onClick = {
                        showSuccessDialog = false
                        navController.popBackStack()
                    }
                ) {
                    Text("OK")
                }
            }
        )
    }
}

@Composable
fun DetailRow(label: String, value: String) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(
            text = label,
            fontSize = 14.sp,
            color = Color.Gray,
            modifier = Modifier.weight(1f)
        )
        Text(
            text = value,
            fontSize = 14.sp,
            fontWeight = FontWeight.SemiBold,
            textAlign = TextAlign.End,
            modifier = Modifier.weight(2f)
        )
    }
}