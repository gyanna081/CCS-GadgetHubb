package com.example.ccsgadgethub.ui

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Logout
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.ccsgadgethub.R

@Composable
fun DashboardScreen(navController: NavController) {
    Row(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFF3F3F3))
    ) {
        // Sidebar
        Column(
            modifier = Modifier
                .width(250.dp)
                .fillMaxHeight()
                .background(Color(0xFFFFF5E9), RoundedCornerShape(topEnd = 20.dp, bottomEnd = 20.dp)),
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            Column {
                // ✅ Top Bar (Menu + Logo) SAME ALIGNMENT as HomeScreen
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(Color(0xFFDE6A00))
                        .padding(horizontal = 16.dp, vertical = 12.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    IconButton(
                        onClick = { navController.navigate("home") }
                    ) {
                        Icon(
                            imageVector = Icons.Default.Menu,
                            contentDescription = "Back to Home",
                            tint = Color.White
                        )
                    }
                    Image(
                        painter = painterResource(id = R.drawable.logo2),
                        contentDescription = "Logo",
                        modifier = Modifier.size(60.dp) // ✅ Bigger logo to match HomeScreen
                    )
                }

                // Sidebar Links
                Column(
                    modifier = Modifier.padding(start = 24.dp, top = 32.dp),
                    verticalArrangement = Arrangement.spacedBy(24.dp)
                ) {
                    Text(
                        text = "Profile",
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.Black,
                        modifier = Modifier.clickable { navController.navigate("profile") }
                    )
                    Text(
                        text = "Items",
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.Black,
                        modifier = Modifier.clickable { navController.navigate("items") }
                    )
                    Text(
                        text = "My Requests",
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.Black
                    )
                    Text(
                        text = "Activity Log",
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.Black
                    )
                }
            }

            // Log Out Button at Bottom
            Button(
                onClick = {
                    navController.navigate("login") {
                        popUpTo("home") { inclusive = true }
                    }
                },
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFDE6A00)),
                shape = RoundedCornerShape(bottomEnd = 20.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.Logout,
                    contentDescription = "Logout",
                    tint = Color.White
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text("Log Out", color = Color.White, fontWeight = FontWeight.Bold)
            }
        }


    }
}
