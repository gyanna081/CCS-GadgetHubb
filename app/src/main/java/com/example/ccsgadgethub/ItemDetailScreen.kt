package com.example.ccsgadgethub

import android.util.Log
import androidx.compose.foundation.background
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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.example.ccsgadgethub.viewmodel.ItemViewModel
import androidx.compose.foundation.clickable  // Add this import

@Composable
fun ItemDetailScreen(
    navController: NavController,
    itemId: String,
    itemViewModel: ItemViewModel
) {
    // Add debug logging
    Log.d("ItemDetailScreen", "Opening item details for ID: $itemId")

    // States
    val items by itemViewModel.items.collectAsState()
    val item = items.find { it.id == itemId }
    var isLoading by remember { mutableStateOf(item == null) }

    // If item is not already in our items list, fetch it
    LaunchedEffect(itemId) {
        if (items.isEmpty() || item == null) {
            isLoading = true
            Log.d("ItemDetailScreen", "Items list is empty or item not found, fetching items")
            itemViewModel.fetchItems()
        }
    }

    // Update loading state when items change
    LaunchedEffect(items) {
        val foundItem = items.find { it.id == itemId }
        if (foundItem != null) {
            isLoading = false
            Log.d("ItemDetailScreen", "Found item: ${foundItem.name}")
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFFFF5E9))
            .verticalScroll(rememberScrollState())
            .padding(16.dp)
    ) {
        // Top bar with back button and title
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Back button
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

            // Title
            Text(
                text = "Item Details",
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
                color = Color(0xFFDE6A00)
            )

            // Empty box for alignment
            Box(modifier = Modifier.size(36.dp))
        }

        Spacer(modifier = Modifier.height(20.dp))

        // Main content
        if (isLoading) {
            // Show loading indicator
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator(color = Color(0xFFDE6A00))
            }
        } else if (item == null) {
            // Show error message if item not found
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text(
                        text = "Item not found.",
                        color = Color.Red,
                        fontWeight = FontWeight.Bold,
                        fontSize = 18.sp
                    )

                    Spacer(modifier = Modifier.height(20.dp))

                    Button(
                        onClick = { navController.popBackStack() },
                        shape = RoundedCornerShape(12.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFDE6A00))
                    ) {
                        Text("Go Back", color = Color.White)
                    }
                }
            }
        } else {
            // Display item details
            AsyncImage(
                model = item.imageUrl,
                contentDescription = "Image of ${item.name}",
                modifier = Modifier
                    .fillMaxWidth()
                    .height(200.dp)
                    .clip(RoundedCornerShape(12.dp)),
                contentScale = ContentScale.Crop
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Item name
            Text(
                text = item.name,
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold,
                color = Color(0xFF333333)
            )

            Spacer(modifier = Modifier.height(8.dp))

            // Status badge
            Box(
                modifier = Modifier
                    .background(
                        color = when {
                            item.status.equals("Available", ignoreCase = true) -> Color(0xFF4CAF50)
                            else -> Color.Red
                        },
                        shape = RoundedCornerShape(16.dp)
                    )
                    .padding(horizontal = 12.dp, vertical = 6.dp)
            ) {
                Text(
                    text = item.status,
                    color = Color.White,
                    fontWeight = FontWeight.Medium,
                    fontSize = 14.sp
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Item details
            DetailSection(title = "Condition", value = item.condition)
            DetailSection(title = "Description", value = item.description)

            Spacer(modifier = Modifier.height(24.dp))

            // Borrow button (if available)
            if (item.status.equals("Available", ignoreCase = true)) {
                Button(
                    onClick = {
                        navController.navigate("borrow_request/${item.id}")
                    },
                    shape = RoundedCornerShape(12.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFDE6A00)),
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(50.dp)
                ) {
                    Text("Borrow This Item", color = Color.White, fontWeight = FontWeight.Bold)
                }
            } else {
                // Item not available message
                Text(
                    text = "This item is currently not available for borrowing.",
                    color = Color.Gray,
                    fontSize = 16.sp,
                    modifier = Modifier.fillMaxWidth()
                )
            }
        }
    }
}

@Composable
fun DetailSection(title: String, value: String) {
    Column(modifier = Modifier.padding(vertical = 8.dp)) {
        Text(
            text = title,
            fontSize = 16.sp,
            fontWeight = FontWeight.Bold,
            color = Color(0xFF666666)
        )

        Spacer(modifier = Modifier.height(4.dp))

        Text(
            text = value,
            fontSize = 16.sp,
            color = Color(0xFF333333)
        )
    }
}