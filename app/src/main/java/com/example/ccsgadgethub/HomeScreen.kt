package com.example.ccsgadgethub

import android.util.Log
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.*
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.example.ccsgadgethub.viewmodel.ItemViewModel
import com.example.ccsgadgethub.viewmodel.RequestViewModel
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.launch
import androidx.compose.runtime.Composable
import androidx.compose.foundation.clickable
import androidx.compose.ui.graphics.painter.ColorPainter

@OptIn(ExperimentalMaterial3Api::class)
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
        Text(text = count, fontWeight = FontWeight.Bold, fontSize = 26.sp, color = Color(0xFFDE6A00))
        Spacer(modifier = Modifier.height(4.dp))
        Text(text = label, fontSize = 13.sp, color = Color.Black, textAlign = TextAlign.Center, lineHeight = 16.sp)
    }
}

@Composable
fun RecentlyAddedItemCard(
    item: Item,
    onClick: (String) -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(Color.White, shape = RoundedCornerShape(12.dp))
            .clickable { onClick(item.id) }
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        AsyncImage(
            model = item.imageUrl,
            contentDescription = "Image of ${item.name}",
            modifier = Modifier
                .size(80.dp)
                .clip(RoundedCornerShape(8.dp)),
            contentScale = ContentScale.Crop,
            placeholder = ColorPainter(Color.LightGray),
            error = ColorPainter(Color(0xFFEEEEEE))
        )

        Spacer(modifier = Modifier.height(8.dp))

        Text(item.name, fontWeight = FontWeight.Bold, fontSize = 16.sp)
        Text(
            text = item.status,
            color = when {
                item.status.equals("Available", ignoreCase = true) -> Color(0xFF4CAF50)
                item.status.equals("Borrowed", ignoreCase = true) -> Color.Red
                else -> Color.Gray
            },
            fontSize = 14.sp
        )
    }
}

@Composable
fun HomeScreen(
    navController: NavController,
    itemViewModel: ItemViewModel,
    requestViewModel: RequestViewModel
) {
    val lifecycleOwner = LocalLifecycleOwner.current
    val coroutineScope = rememberCoroutineScope()

    // Get current Firebase user
    val currentUser = FirebaseAuth.getInstance().currentUser
    val currentUserUid = currentUser?.uid

    // Add debugging for user data
    var debugUserFetchAttempted by remember { mutableStateOf(false) }

    // Fetch data on first composition - with added debugging
    LaunchedEffect(Unit) {
        Log.d("HomeScreen", "Initial data fetch started")

        // First fetch user data
        if (currentUserUid != null) {
            Log.d("HomeScreen", "Fetching user data for UID: $currentUserUid")
            itemViewModel.fetchUserData()
            debugUserFetchAttempted = true
        } else {
            Log.e("HomeScreen", "Current user UID is null, cannot fetch user data")
        }

        // Then fetch other data
        itemViewModel.fetchItems()
        requestViewModel.fetchRequests()
    }

    // State collection
    val user by itemViewModel.userData.collectAsState()
    val allRequests by requestViewModel.requests.collectAsState()
    val items by itemViewModel.items.collectAsState()

    // Add debugging for user data state
    LaunchedEffect(user) {
        if (debugUserFetchAttempted) {
            if (user != null) {
                Log.d("HomeScreen", "User data received: ${user?.firstName} ${user?.lastName}")
            } else {
                Log.e("HomeScreen", "User data is still null after fetch attempt")

                // If user is still null after first attempt, try fetching again
                if (currentUserUid != null) {
                    Log.d("HomeScreen", "Retrying user data fetch")
                    itemViewModel.fetchUserData()
                }
            }
        }
    }

    // Function to navigate to item details
    val navigateToItemDetail: (String) -> Unit = { itemId ->
        navController.navigate("item_detail/$itemId")
    }

    // Use firstName with proper fallback and debug logging
    val userName = if (user?.firstName.isNullOrBlank()) {
        Log.d("HomeScreen", "Using fallback name 'User' because user?.firstName is null or blank")
        "User"
    } else {
        Log.d("HomeScreen", "Using actual user firstName: ${user?.firstName}")
        user?.firstName ?: "User"
    }

    // Filter requests
    val myRequests = allRequests.filter { it.borrowerId == currentUserUid }

    // Backend-based counts
    val borrowedCount = myRequests.count {
        it.status.equals("Approved", ignoreCase = true) || it.status.equals("Returned", ignoreCase = true)
    }
    val pendingCount = myRequests.count { it.status.equals("Pending", ignoreCase = true) }
    val overdueCount = myRequests.count { it.status.equals("Overdue", ignoreCase = true) }

    val recentItems = items.take(3)

    // Auto-fetch data on screen resume
    DisposableEffect(lifecycleOwner) {
        val observer = LifecycleEventObserver { _, event ->
            if (event == Lifecycle.Event.ON_RESUME) {
                coroutineScope.launch {
                    Log.d("HomeScreen", "Screen resumed, refreshing data")
                    if (currentUserUid != null) {
                        itemViewModel.fetchUserData()
                    }
                    itemViewModel.fetchItems()
                    requestViewModel.fetchRequests()
                }
            }
        }
        lifecycleOwner.lifecycle.addObserver(observer)
        onDispose { lifecycleOwner.lifecycle.removeObserver(observer) }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFFBF3E5))
            .verticalScroll(rememberScrollState())
            .padding(horizontal = 16.dp)
    ) {
        Spacer(modifier = Modifier.height(WindowInsets.statusBars.asPaddingValues().calculateTopPadding()))

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 12.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
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

        // Display user's first name from the user data flow
        Text("Welcome back, $userName!", fontWeight = FontWeight.Bold, fontSize = 24.sp)
        Spacer(modifier = Modifier.height(4.dp))
        Text("Here's a quick overview of your gadget hub activity.", fontSize = 14.sp, color = Color.Gray)

        Spacer(modifier = Modifier.height(16.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            StatisticCard(borrowedCount.toString(), "Items Borrowed")
            StatisticCard(pendingCount.toString(), "Pending Requests")
            StatisticCard(overdueCount.toString(), "Overdue")
        }

        Spacer(modifier = Modifier.height(20.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.Center
        ) {
            Button(
                onClick = { navController.navigate("items") },
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFDE6A00)),
                shape = RoundedCornerShape(8.dp),
                modifier = Modifier.padding(horizontal = 8.dp)
            ) {
                Text("Borrow Item", color = Color.Black)
            }
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

        Text("Featured Items", fontWeight = FontWeight.Bold, fontSize = 20.sp)
        Spacer(modifier = Modifier.height(16.dp))

        Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
            if (recentItems.isEmpty()) {
                Text("No items available.", color = Color.Gray)
            } else {
                recentItems.forEach { item ->
                    RecentlyAddedItemCard(
                        item = item,
                        onClick = navigateToItemDetail
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(24.dp))
    }
}