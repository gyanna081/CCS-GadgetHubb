package com.example.ccsgadgethub

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowDropDown
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
import kotlinx.coroutines.delay
import java.time.LocalTime
import java.time.format.DateTimeFormatter

@Composable
fun RequestFormScreen(navController: NavController, itemNameArg: String?) {
    var itemName by remember { mutableStateOf(itemNameArg ?: "") }
    var reason by remember { mutableStateOf("") }
    var termsAccepted by remember { mutableStateOf(false) }
    var showConfirmationDialog by remember { mutableStateOf(false) } // Show confirmation dialog
    var showSuccessMessage by remember { mutableStateOf(false) } // Show success message

    val timeFormatter = DateTimeFormatter.ofPattern("h:mm a")
    val availableTimes = (7..20).map {
        LocalTime.of(it, 0).format(timeFormatter)
    }
    val durationOptions = listOf(1, 2, 3)

    var selectedStartTime by remember { mutableStateOf<String?>(null) }
    var selectedDuration by remember { mutableStateOf<Int?>(null) }
    var returnTime by remember { mutableStateOf<String?>(null) }

    var expandedStartTime by remember { mutableStateOf(false) }
    var expandedDuration by remember { mutableStateOf(false) }

    val filteredStartTimes = remember {
        availableTimes.map { timeString ->
            val time = LocalTime.parse(timeString, timeFormatter)
            val isAvailable = durationOptions.any { duration ->
                time.plusHours(duration.toLong()) <= LocalTime.of(21, 0)
            }
            Pair(timeString, isAvailable)
        }
    }

    LaunchedEffect(selectedStartTime, selectedDuration) {
        if (selectedStartTime != null && selectedDuration != null) {
            val start = LocalTime.parse(selectedStartTime, timeFormatter)
            returnTime = start.plusHours(selectedDuration!!.toLong()).format(timeFormatter)
        } else {
            returnTime = null
        }
    }

    // Handle navigation delay after success message
    LaunchedEffect(showSuccessMessage) {
        if (showSuccessMessage) {
            delay(2000) // Delay for 2 seconds before navigating
            navController.popBackStack() // Pop the current screen off the back stack and go to the previous one
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFFFF5E9))
            .padding(horizontal = 24.dp, vertical = 16.dp)
            .verticalScroll(rememberScrollState())
    ) {
        // Topbar
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

        Spacer(modifier = Modifier.height(20.dp))

        Text(
            text = "Request Form",
            fontSize = 28.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.align(Alignment.CenterHorizontally)
        )

        Spacer(modifier = Modifier.height(28.dp))

        // Item Name
        Text(text = "Item Name:", fontWeight = FontWeight.SemiBold)
        Spacer(modifier = Modifier.height(4.dp))
        OutlinedTextField(
            value = itemName,
            onValueChange = { itemName = it },
            placeholder = { Text("Search available item here") },
            modifier = Modifier
                .fillMaxWidth()
                .background(Color.White, shape = RoundedCornerShape(12.dp)),
            shape = RoundedCornerShape(12.dp),
            colors = TextFieldDefaults.colors(
                focusedContainerColor = Color.White,
                unfocusedContainerColor = Color.White,
                focusedIndicatorColor = Color(0xFFCC6600),
                unfocusedIndicatorColor = Color(0xFFCC6600)
            )
        )

        Spacer(modifier = Modifier.height(20.dp))

        // Reason for borrowing
        Text(text = "Reason for borrowing:", fontWeight = FontWeight.SemiBold)
        Spacer(modifier = Modifier.height(4.dp))
        OutlinedTextField(
            value = reason,
            onValueChange = { reason = it },
            placeholder = { Text("State reason here...") },
            modifier = Modifier
                .fillMaxWidth()
                .background(Color.LightGray, shape = RoundedCornerShape(12.dp))
                .height(120.dp),
            shape = RoundedCornerShape(12.dp),
            maxLines = 5
        )

        Spacer(modifier = Modifier.height(20.dp))

        // Duration Section
        Text(text = "Duration:", fontWeight = FontWeight.SemiBold)
        Spacer(modifier = Modifier.height(4.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Start Time Dropdown
            Box(modifier = Modifier.weight(1f)) {
                OutlinedTextField(
                    value = selectedStartTime ?: "",
                    onValueChange = {},
                    readOnly = true,
                    placeholder = { Text("Start Time") },
                    trailingIcon = {
                        Icon(
                            imageVector = Icons.Default.ArrowDropDown,
                            contentDescription = "Dropdown",
                            Modifier.clickable { expandedStartTime = true }
                        )
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(Color.White, shape = RoundedCornerShape(12.dp)),
                    shape = RoundedCornerShape(12.dp)
                )
                DropdownMenu(
                    expanded = expandedStartTime,
                    onDismissRequest = { expandedStartTime = false }
                ) {
                    filteredStartTimes.forEach { (time, isAvailable) ->
                        DropdownMenuItem(
                            text = {
                                Text(
                                    "$time ${if (isAvailable) "(Available)" else "(Not Available)"}",
                                    color = if (isAvailable) Color.Black else Color.Gray
                                )
                            },
                            onClick = {
                                if (isAvailable) {
                                    selectedStartTime = time
                                    expandedStartTime = false
                                }
                            },
                            enabled = isAvailable
                        )
                    }
                }
            }

            // Duration Dropdown
            Box(modifier = Modifier.weight(1f)) {
                OutlinedTextField(
                    value = selectedDuration?.let { "$it hr${if (it > 1) "s" else ""}" } ?: "",
                    onValueChange = {},
                    readOnly = true,
                    placeholder = { Text("Duration") },
                    trailingIcon = {
                        Icon(
                            imageVector = Icons.Default.ArrowDropDown,
                            contentDescription = "Dropdown",
                            Modifier.clickable { expandedDuration = true }
                        )
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(Color.White, shape = RoundedCornerShape(12.dp)),
                    shape = RoundedCornerShape(12.dp)
                )
                DropdownMenu(
                    expanded = expandedDuration,
                    onDismissRequest = { expandedDuration = false }
                ) {
                    durationOptions.forEach { duration ->
                        DropdownMenuItem(
                            text = { Text("$duration hour${if (duration > 1) "s" else ""}") },
                            onClick = {
                                selectedDuration = duration
                                expandedDuration = false
                            }
                        )
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(20.dp))

        // Return Time Section
        Text(text = "Return Time:", fontWeight = FontWeight.SemiBold)
        Spacer(modifier = Modifier.height(4.dp))
        Text(
            text = returnTime ?: "Select start time and duration",
            fontSize = 14.sp,
            color = Color.Gray,
            textAlign = TextAlign.Start
        )

        Spacer(modifier = Modifier.height(12.dp))

        // Terms and Agreement
        Text(text = "Terms and Agreement", fontWeight = FontWeight.SemiBold)
        Spacer(modifier = Modifier.height(8.dp))
        Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
            repeat(1) {
                Text(
                    text = "I agree to return the item in good condition and follow the borrowing policies.",
                    fontSize = 14.sp,
                    color = Color.Gray
                )
            }
        }

        Spacer(modifier = Modifier.height(12.dp))

        Row(verticalAlignment = Alignment.CenterVertically) {
            Checkbox(
                checked = termsAccepted,
                onCheckedChange = { termsAccepted = it },
                colors = CheckboxDefaults.colors(
                    checkedColor = Color(0xFFCC6600),
                    uncheckedColor = Color(0xFFCC6600),
                    checkmarkColor = Color.White
                )
            )
            Spacer(modifier = Modifier.width(8.dp))
            Text(
                text = "I agree to the Terms and Agreement.",
                fontSize = 14.sp,
                color = Color.Black
            )
        }

        Spacer(modifier = Modifier.height(12.dp))

        Button(
            onClick = { showConfirmationDialog = true }, // Show confirmation dialog
            enabled = termsAccepted,
            shape = RoundedCornerShape(16.dp),
            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFCC6600)),
            modifier = Modifier
                .fillMaxWidth()
                .height(55.dp)
        ) {
            Text(
                text = "Submit Request",
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                color = Color.White
            )
        }
    }

    // ================= Confirmation Dialog =================
    if (showConfirmationDialog) {
        AlertDialog(
            onDismissRequest = { showConfirmationDialog = false },
            title = { Text("Confirm Request") },
            text = {
                Column {
                    Text("Item Name: $itemName")
                    Text("Reason for borrowing: $reason")
                    Text("Duration: ${selectedStartTime ?: ""} - $returnTime")
                    Text("Return Time: $returnTime")
                    Spacer(modifier = Modifier.height(12.dp))
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Checkbox(
                            checked = termsAccepted,
                            onCheckedChange = { termsAccepted = it },
                            colors = CheckboxDefaults.colors(
                                checkedColor = Color(0xFFCC6600),
                                uncheckedColor = Color(0xFFCC6600),
                                checkmarkColor = Color.White
                            )
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text("I agree to return the item in good condition and follow the borrowing policies.")
                    }
                }
            },
            confirmButton = {
                Button(
                    onClick = {
                        showSuccessMessage = true // Show success message after confirmation
                        showConfirmationDialog = false
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFCC6600))
                ) {
                    Text("Confirm Request", color = Color.White)
                }
            },
            dismissButton = {
                OutlinedButton(
                    onClick = { showConfirmationDialog = false },
                    colors = ButtonDefaults.outlinedButtonColors(contentColor = Color(0xFFCC6600))
                ) {
                    Text("Cancel")
                }
            },
            containerColor = Color.White,
            shape = RoundedCornerShape(12.dp)
        )
    }

    // ================= Success Message =================
    if (showSuccessMessage) {
        AlertDialog(
            onDismissRequest = { showSuccessMessage = false },
            title = { Text("Request Submitted") },
            text = { Text("Your request has been submitted successfully!") },
            confirmButton = {
                Button(
                    onClick = {
                        navController.popBackStack() // Navigate back to ItemScreen using popBackStack
                        showSuccessMessage = false
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFCC6600))
                ) {
                    Text("Okay", color = Color.White)
                }
            },
            containerColor = Color.White,
            shape = RoundedCornerShape(12.dp)
        )
    }
}
