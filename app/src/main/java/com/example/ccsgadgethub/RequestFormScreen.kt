package com.example.ccsgadgethub

import android.app.DatePickerDialog
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.ccsgadgethub.viewmodel.RequestViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import java.util.*

@Composable
fun RequestFormScreen(
    navController: NavController,
    itemNameArg: String?,
    viewModel: RequestViewModel = viewModel() // Injecting the RequestViewModel
) {
    var itemName by remember { mutableStateOf(itemNameArg ?: "") }
    var selectedDate by remember { mutableStateOf("") }
    var reason by remember { mutableStateOf("") }
    var termsAccepted by remember { mutableStateOf(false) }
    var showConfirmationDialog by remember { mutableStateOf(false) }
    var showSuccessMessage by remember { mutableStateOf(false) }

    val timeSlots = listOf(
        "7:30 AM - 8:00 AM",
        "8:00 AM - 8:30 AM",
        "8:30 AM - 9:00 AM",
        "9:00 AM - 9:30 AM (Unavailable)",
        "9:30 AM - 10:00 AM (Unavailable)",
        "10:00 AM - 10:30 AM",
        "10:30 AM - 11:00 AM",
        "11:00 AM - 11:30 AM",
        "11:30 AM - 12:00 PM",
        "12:00 PM - 12:30 PM",
        "12:30 PM - 1:00 PM",
        "1:00 PM - 1:30 PM (Unavailable)",
        "1:30 PM - 2:00 PM",
        "2:00 PM - 2:30 PM",
        "2:30 PM - 3:00 PM",
        "3:00 PM - 3:30 PM",
        "3:30 PM - 4:00 PM",
        "4:00 PM - 4:30 PM",
        "4:30 PM - 5:00 PM",
        "5:00 PM - 5:30 PM",
        "5:30 PM - 6:00 PM",
        "6:00 PM - 6:30 PM",
        "6:30 PM - 7:00 PM",
        "7:00 PM - 7:30 PM",
        "7:30 PM - 8:00 PM",
        "8:00 PM - 8:30 PM",
        "8:30 PM - 9:00 PM",
        "9:00 PM - 9:30 PM"
    )

    val durationOptions = listOf(
        "30 minutes" to 30,
        "1 hour" to 60,
        "1 hour 30 minutes" to 90,
        "2 hours" to 120
    )

    var selectedStartTime by remember { mutableStateOf<String?>(null) }
    var expandedDuration by remember { mutableStateOf(false) }

    var selectedDurationLabel by remember { mutableStateOf<String?>(null) }
    var selectedDurationMinutes by remember { mutableStateOf(0) }
    var expandedDurationMenu by remember { mutableStateOf(false) }

    fun parseTimeSlotToEnd(startTime: String?, duration: Int): String {
        if (startTime == null) return ""
        val formatter = java.time.format.DateTimeFormatter.ofPattern("h:mm a", Locale.ENGLISH)
        val range = startTime.split(" - ").firstOrNull() ?: return ""
        return try {
            val start = java.time.LocalTime.parse(range, formatter)
            val end = start.plusMinutes(duration.toLong())
            end.format(formatter)
        } catch (e: Exception) {
            ""
        }
    }

    val context = LocalContext.current
    val calendar = Calendar.getInstance()
    val datePickerDialog = DatePickerDialog(
        context,
        { _, year, month, dayOfMonth ->
            selectedDate = "${month + 1}/$dayOfMonth/$year"
        },
        calendar.get(Calendar.YEAR),
        calendar.get(Calendar.MONTH),
        calendar.get(Calendar.DAY_OF_MONTH)
    )

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFFFF5E9))
            .padding(horizontal = 24.dp, vertical = 16.dp)
            .verticalScroll(rememberScrollState())
    ) {
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

        // Item Name TextField
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

        // Date Picker
        Spacer(modifier = Modifier.height(20.dp))
        Text(text = "Date of Borrowing:", fontWeight = FontWeight.SemiBold)
        Spacer(modifier = Modifier.height(4.dp))
        OutlinedTextField(
            value = selectedDate,
            onValueChange = {},
            readOnly = true,
            placeholder = { Text("Select date") },
            trailingIcon = {
                Icon(
                    imageVector = Icons.Default.ArrowDropDown,
                    contentDescription = "Date Picker",
                    modifier = Modifier.clickable { datePickerDialog.show() }
                )
            },
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

        // Reason for Borrowing TextField
        Spacer(modifier = Modifier.height(20.dp))
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

        // Time Slot Selection
        Spacer(modifier = Modifier.height(20.dp))
        Text(text = "Select Time Slot (Start):", fontWeight = FontWeight.SemiBold)
        Spacer(modifier = Modifier.height(4.dp))

        Box(modifier = Modifier.fillMaxWidth()) {
            OutlinedTextField(
                value = selectedStartTime ?: "",
                onValueChange = {},
                readOnly = true,
                placeholder = { Text("Select Time Slot") },
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
                timeSlots.forEach { slot ->
                    val isAvailable = !slot.contains("Unavailable")
                    DropdownMenuItem(
                        text = {
                            Text(slot, color = if (isAvailable) Color.Black else Color.Red)
                        },
                        onClick = {
                            if (isAvailable) {
                                selectedStartTime = slot
                                expandedDuration = false
                            }
                        },
                        enabled = isAvailable
                    )
                }
            }
        }

        // Duration Selection
        Spacer(modifier = Modifier.height(20.dp))
        Text(text = "Select Duration:", fontWeight = FontWeight.SemiBold)
        Spacer(modifier = Modifier.height(4.dp))

        Box(modifier = Modifier.fillMaxWidth()) {
            OutlinedTextField(
                value = selectedDurationLabel ?: "",
                onValueChange = {},
                readOnly = true,
                placeholder = { Text("Select Duration") },
                trailingIcon = {
                    Icon(
                        imageVector = Icons.Default.ArrowDropDown,
                        contentDescription = "Dropdown",
                        Modifier.clickable { expandedDurationMenu = true }
                    )
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .background(Color.White, shape = RoundedCornerShape(12.dp)),
                shape = RoundedCornerShape(12.dp)
            )
            DropdownMenu(
                expanded = expandedDurationMenu,
                onDismissRequest = { expandedDurationMenu = false }
            ) {
                durationOptions.forEach { (label, minutes) ->
                    DropdownMenuItem(
                        text = { Text(label) },
                        onClick = {
                            selectedDurationLabel = label
                            selectedDurationMinutes = minutes
                            expandedDurationMenu = false
                        }
                    )
                }
            }
        }

        // Show selected time slot and return time
        Spacer(modifier = Modifier.height(12.dp))

        if (selectedStartTime != null && selectedDurationLabel != null) {
            val endTime = parseTimeSlotToEnd(selectedStartTime, selectedDurationMinutes)
            val returnTime = parseTimeSlotToEnd(selectedStartTime, selectedDurationMinutes + 10)

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(Color(0xFFFFF2D5), shape = RoundedCornerShape(8.dp))
                    .padding(12.dp)
            ) {
                Text(
                    text = "Selected Time Slot: $selectedStartTime - $endTime",
                    color = Color(0xFFCC6600),
                    fontWeight = FontWeight.SemiBold
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            Text(text = "Expected Return Time:", fontWeight = FontWeight.SemiBold)
            Spacer(modifier = Modifier.height(4.dp))
            OutlinedTextField(
                value = "$returnTime (+10 mins grace period)",
                onValueChange = {},
                readOnly = true,
                modifier = Modifier
                    .fillMaxWidth()
                    .background(Color.LightGray, shape = RoundedCornerShape(12.dp))
            )
        }

        // Terms and Agreement Checkbox
        Spacer(modifier = Modifier.height(20.dp))

        Text(text = "Terms and Agreement", fontWeight = FontWeight.SemiBold)
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            text = "I agree to return the item in good condition and follow the borrowing policies.",
            fontSize = 14.sp,
            color = Color.Gray
        )

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

        // Submit Button
        Spacer(modifier = Modifier.height(12.dp))

        Button(
            onClick = {
                // Add request to ViewModel
                if (termsAccepted) {
                    val returnTime = parseTimeSlotToEnd(selectedStartTime, selectedDurationMinutes + 10)
                    viewModel.addRequest(
                        item = itemName,
                        requestDate = selectedDate,
                        status = "Pending",
                        returnTime = returnTime
                    )
                    showConfirmationDialog = true
                }
            },
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

    // Confirmation Dialog
    if (showConfirmationDialog) {
        val returnTime = parseTimeSlotToEnd(selectedStartTime, selectedDurationMinutes + 10)
        AlertDialog(
            onDismissRequest = { showConfirmationDialog = false },
            title = { Text("Confirm Request") },
            text = {
                Column {
                    Text("Item Name: $itemName")
                    Text("Date of Borrowing: $selectedDate")
                    Text("Reason: $reason")
                    Text("Selected Time Slot: ${selectedStartTime ?: "Not selected"}")
                    Text("Duration: ${selectedDurationLabel ?: "Not selected"}")
                    Text("Expected Return Time: $returnTime")
                }
            },
            confirmButton = {
                Button(
                    onClick = {
                        showSuccessMessage = true
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

    // Success Message
    if (showSuccessMessage) {
        AlertDialog(
            onDismissRequest = { showSuccessMessage = false },
            title = { Text("Request Submitted") },
            text = { Text("Your request has been submitted successfully!") },
            confirmButton = {
                Button(
                    onClick = {
                        navController.popBackStack()
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
