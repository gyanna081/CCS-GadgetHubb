package com.example.ccsgadgethub

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
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import java.net.URLEncoder
import java.nio.charset.StandardCharsets

// Dummy items list
data class Item(val name: String, val status: String, val rating: Int)

val allItems = listOf(
    Item("Dell Laptop", "Available", 5),
    Item("Huawei D15 Matebook", "Borrowed", 4),
    Item("Dell Laptop 2", "Available", 3),
    Item("Asus Zenbook", "Available", 5),
    Item("MacBook Air", "Borrowed", 5)
)

@Composable
fun ItemScreen(navController: NavController) {
    var searchQuery by remember { mutableStateOf("") }
    var selectedStatus by remember { mutableStateOf("All") }
    var selectedRating by remember { mutableStateOf("All") }

    val statuses = listOf("All", "Available", "Borrowed")
    val ratings = listOf("All", "★", "★★", "★★★", "★★★★", "★★★★★")

    val filteredItems = allItems.filter { item ->
        (selectedStatus == "All" || item.status == selectedStatus) &&
                (selectedRating == "All" || item.rating == selectedRating.length)
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFFBF3E5))
            .padding(horizontal = 16.dp)
    ) {
        Spacer(modifier = Modifier.height(WindowInsets.statusBars.asPaddingValues().calculateTopPadding()))

        // Top bar
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
                contentDescription = "CCS Gadget Hub Logo",
                modifier = Modifier.size(60.dp)
            )
        }

        Spacer(modifier = Modifier.height(12.dp))

        // Search Field
        TextField(
            value = searchQuery,
            onValueChange = { searchQuery = it },
            placeholder = { Text("Enter items here") },
            modifier = Modifier
                .fillMaxWidth()
                .height(50.dp),
            shape = RoundedCornerShape(12.dp),
            colors = TextFieldDefaults.colors(
                focusedContainerColor = Color.White,
                unfocusedContainerColor = Color.White,
                focusedIndicatorColor = Color.Transparent,
                unfocusedIndicatorColor = Color.Transparent
            ),
            singleLine = true
        )

        Spacer(modifier = Modifier.height(16.dp))

        // Filters
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Text("Status:", fontWeight = FontWeight.Bold)
                DropdownMenuBox(
                    selectedText = selectedStatus,
                    items = statuses,
                    onItemSelected = { selectedStatus = it },
                    backgroundColor = Color.White,
                    textColor = Color(0xFFDE6A00)
                )
            }

            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Text("Ratings:", fontWeight = FontWeight.Bold)
                DropdownMenuBox(
                    selectedText = selectedRating,
                    items = ratings,
                    onItemSelected = { selectedRating = it },
                    backgroundColor = Color.White,
                    textColor = Color(0xFFFFA000)
                )
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Items List
        Column(
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            if (filteredItems.isEmpty()) {
                Text(
                    text = "No items found.",
                    color = Color.Gray,
                    fontSize = 16.sp,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.fillMaxWidth()
                )
            } else {
                filteredItems.forEach { item ->
                    val encodedItemName = URLEncoder.encode(item.name, StandardCharsets.UTF_8.toString())

                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .background(Color.White, RoundedCornerShape(12.dp))
                            .clickable {
                                navController.navigate("item_detail/$encodedItemName")
                            }
                            .padding(vertical = 16.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Box(
                            modifier = Modifier
                                .size(60.dp)
                                .background(Color.LightGray, RoundedCornerShape(8.dp)),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(text = "Image", fontSize = 10.sp, color = Color.DarkGray)
                        }

                        Spacer(modifier = Modifier.height(8.dp))

                        Text(item.name, fontWeight = FontWeight.Bold, fontSize = 18.sp)

                        Spacer(modifier = Modifier.height(4.dp))

                        Text(
                            text = item.status,
                            color = if (item.status == "Available") Color(0xFF2E7D32) else Color(0xFFC62828),
                            fontWeight = FontWeight.Bold,
                            fontSize = 14.sp
                        )

                        Spacer(modifier = Modifier.height(4.dp))

                        Text(
                            text = "★".repeat(item.rating),
                            color = Color(0xFFFFA000),
                            fontSize = 18.sp
                        )

                        Spacer(modifier = Modifier.height(8.dp))

                        Text(
                            text = "View Details",
                            color = Color(0xFFD35400),
                            fontWeight = FontWeight.Bold,
                            fontSize = 14.sp
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun DropdownMenuBox(
    selectedText: String,
    items: List<String>,
    onItemSelected: (String) -> Unit,
    backgroundColor: Color,
    textColor: Color
) {
    var expanded by remember { mutableStateOf(false) }

    Box(
        modifier = Modifier
            .background(backgroundColor, RoundedCornerShape(8.dp))
            .clickable { expanded = true }
            .padding(horizontal = 16.dp, vertical = 8.dp)
    ) {
        Text(text = selectedText, color = textColor, fontWeight = FontWeight.Bold)

        DropdownMenu(expanded = expanded, onDismissRequest = { expanded = false }) {
            items.forEach { item ->
                DropdownMenuItem(
                    text = { Text(item) },
                    onClick = {
                        onItemSelected(item)
                        expanded = false
                    }
                )
            }
        }
    }
}
