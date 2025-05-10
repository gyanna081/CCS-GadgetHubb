package com.example.ccsgadgethub

import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Logout
import androidx.compose.material.icons.filled.Menu
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
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.example.ccsgadgethub.viewmodel.ItemViewModel
import java.net.URLEncoder
import java.nio.charset.StandardCharsets
import androidx.compose.runtime.collectAsState

@Composable
fun DashboardScreen(navController: NavController, viewModel: ItemViewModel, userFirstName: String = "User") {
    val lifecycleOwner = LocalLifecycleOwner.current
    val items by viewModel.items.collectAsState() // ✅ FIXED HERE

    LaunchedEffect(Unit) {
        val observer = LifecycleEventObserver { _, event ->
            if (event == Lifecycle.Event.ON_RESUME) {
                viewModel.fetchItems()
            }
        }
        lifecycleOwner.lifecycle.addObserver(observer)
    }

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
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(Color(0xFFDE6A00))
                        .padding(horizontal = 16.dp, vertical = 12.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    IconButton(onClick = { navController.navigate("home") }) {
                        Icon(
                            imageVector = Icons.Default.Menu,
                            contentDescription = "Back to Home",
                            tint = Color.White
                        )
                    }
                    Image(
                        painter = painterResource(id = R.drawable.logo2),
                        contentDescription = "Logo",
                        modifier = Modifier.size(60.dp)
                    )
                }

                Column(
                    modifier = Modifier.padding(start = 24.dp, top = 32.dp),
                    verticalArrangement = Arrangement.spacedBy(24.dp)
                ) {
                    Text("Profile", fontSize = 18.sp, fontWeight = FontWeight.Bold, color = Color.Black,
                        modifier = Modifier.clickable { navController.navigate("profile") })

                    Text("Items", fontSize = 18.sp, fontWeight = FontWeight.Bold, color = Color.Black,
                        modifier = Modifier.clickable { navController.navigate("items") })

                    Text("My Requests", fontSize = 18.sp, fontWeight = FontWeight.Bold, color = Color.Black,
                        modifier = Modifier.clickable { navController.navigate("my_requests") })
                }
            }

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
                Icon(Icons.AutoMirrored.Filled.Logout, contentDescription = "Logout", tint = Color.White)
                Spacer(modifier = Modifier.width(8.dp))
                Text("Log Out", color = Color.White, fontWeight = FontWeight.Bold)
            }
        }

        // Main content
        Column(
            modifier = Modifier
                .weight(1f)
                .fillMaxHeight()
                .verticalScroll(rememberScrollState())
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Text("Welcome, $userFirstName!", fontSize = 22.sp, fontWeight = FontWeight.Bold)
            Text("Featured Items", fontSize = 20.sp, fontWeight = FontWeight.Medium)

            if (items.isEmpty()) {
                Text("No items available.", color = Color.Gray)
            } else {
                items.forEach { item ->
                    val encodedItemName = URLEncoder.encode(item.name, StandardCharsets.UTF_8.toString())

                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .background(Color.White, RoundedCornerShape(12.dp))
                            .clickable {
                                navController.navigate("item_detail/$encodedItemName")
                            }
                            .padding(16.dp)
                    ) {
                        AsyncImage(
                            model = item.imageUrl,
                            contentDescription = "Item Image",
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(150.dp)
                                .clip(RoundedCornerShape(12.dp)),
                            contentScale = ContentScale.Crop
                        )

                        Spacer(modifier = Modifier.height(8.dp))

                        Text(item.name, fontWeight = FontWeight.Bold, fontSize = 18.sp)

                        Spacer(modifier = Modifier.height(4.dp))

                        Text(
                            text = item.status,
                            color = if (item.status == "Available") Color(0xFF2E7D32) else Color(0xFFC62828),
                            fontWeight = FontWeight.Bold,
                            fontSize = 14.sp
                        )
                    }
                }
            }
        }
    }
}
