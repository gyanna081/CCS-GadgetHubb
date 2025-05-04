package com.example.ccsgadgethub

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
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
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(navController: NavController) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFFBF3E5))
            .padding(horizontal = 16.dp)
    ) {
        Spacer(modifier = Modifier.height(WindowInsets.statusBars.asPaddingValues().calculateTopPadding()))

        // Top Bar
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 12.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            // ✅ Menu Icon navigates to Dashboard
            IconButton(onClick = { navController.navigate("dashboard") }) {
                Icon(
                    imageVector = Icons.Default.Menu,
                    contentDescription = "Dashboard",
                    tint = Color(0xFFDE6A00)
                )
            }

            Image(
                painter = painterResource(id = R.drawable.logo),
                contentDescription = "App Logo",
                modifier = Modifier.size(60.dp)
            )
        }

        Spacer(modifier = Modifier.height(8.dp))

        // Welcome Text
        Text(
            text = "Welcome back, Mica!",
            fontWeight = FontWeight.Bold,
            fontSize = 24.sp
        )
        Spacer(modifier = Modifier.height(4.dp))
        Text(
            text = "Here's a quick overview of your gadget hub activity.",
            fontSize = 14.sp,
            color = Color.Gray
        )

        Spacer(modifier = Modifier.height(16.dp))

        // Statistics Cards
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            StatisticCard(count = "3", label = "Items Borrowed")
            StatisticCard(count = "1", label = "Pending Requests")
            StatisticCard(count = "0", label = "Overdue")
        }

        Spacer(modifier = Modifier.height(20.dp))

        // Centered Action Buttons
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically
        ) {
            // ✅ Borrow Item button
            Button(
                onClick = { navController.navigate("items") },
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFDE6A00)),
                shape = RoundedCornerShape(8.dp),
                modifier = Modifier.padding(horizontal = 8.dp)
            ) {
                Text("Borrow Item", color = Color.Black)
            }
            // ✅ View Requests button (UPDATED)
            Button(
                onClick = { navController.navigate("my_requests") },
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFDE6A00)),
                shape = RoundedCornerShape(8.dp),
                modifier = Modifier.padding(horizontal = 8.dp)
            ) {
                Text("View Requests", color = Color.Black)
            }
        }

        Spacer(modifier = Modifier.height(24.dp))

        // Recently Added Title
        Text(
            text = "Recently Added",
            fontWeight = FontWeight.Bold,
            fontSize = 20.sp
        )

        Spacer(modifier = Modifier.height(16.dp))

        // Recently Added Items
        Column(
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            RecentlyAddedItemCard("Dell Laptop", "Available", 5)
            RecentlyAddedItemCard("Huawei D15 Matebook", "Not Available", 4)
            RecentlyAddedItemCard("Dell Laptop 2", "Available", 3)
        }

        Spacer(modifier = Modifier.weight(1f))
    }
}

@Composable
fun StatisticCard(count: String, label: String) {
    Column(
        modifier = Modifier
            .width(100.dp)
            .height(100.dp)
            .background(Color.White, shape = RoundedCornerShape(16.dp))
            .padding(8.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = count,
            fontWeight = FontWeight.Bold,
            fontSize = 26.sp,
            color = Color(0xFFDE6A00)
        )
        Spacer(modifier = Modifier.height(4.dp))
        Text(
            text = label,
            fontSize = 13.sp,
            color = Color.Black,
            textAlign = TextAlign.Center,
            lineHeight = 16.sp
        )
    }
}

@Composable
fun RecentlyAddedItemCard(itemName: String, status: String, rating: Int) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(Color.White, shape = RoundedCornerShape(12.dp))
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // Placeholder for item image
        Box(
            modifier = Modifier
                .size(50.dp)
                .background(Color.LightGray, shape = RoundedCornerShape(8.dp)),
            contentAlignment = Alignment.Center
        ) {
            Text("Image", fontSize = 10.sp, color = Color.DarkGray)
        }

        Spacer(modifier = Modifier.height(8.dp))

        Text(itemName, fontWeight = FontWeight.Bold, fontSize = 16.sp)
        Text(
            text = status,
            color = if (status == "Available") Color(0xFF4CAF50) else Color.Red,
            fontSize = 14.sp
        )

        Spacer(modifier = Modifier.height(4.dp))

        // Star Rating
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center
        ) {
            repeat(rating) {
                Text(text = "⭐", fontSize = 16.sp)
            }
        }
    }
}
