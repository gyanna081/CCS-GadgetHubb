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

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EditProfileScreen(navController: NavController) {
    var firstName by remember { mutableStateOf(ProfileData.firstName) }
    var lastName by remember { mutableStateOf(ProfileData.lastName) }
    var course by remember { mutableStateOf(ProfileData.course) }
    var yearLevel by remember { mutableStateOf(ProfileData.yearLevel) }

    val courseOptions = listOf("BSIT", "BSCS", "BSEMC")
    val yearOptions = listOf("1st Year", "2nd Year", "3rd Year", "4th Year")

    var courseDropdownExpanded by remember { mutableStateOf(false) }
    var yearDropdownExpanded by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFFBF3E5))
            .padding(horizontal = 16.dp)
    ) {
        Spacer(modifier = Modifier.height(WindowInsets.statusBars.asPaddingValues().calculateTopPadding()))

        // Top bar (back and logo)
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
                modifier = Modifier.size(50.dp)
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Form Card
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .background(Color(0xFFF9ECD8), RoundedCornerShape(16.dp))
                .padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Profile Image Placeholder
            Box(
                modifier = Modifier
                    .size(110.dp)
                    .clip(RoundedCornerShape(16.dp))
                    .background(Color.White),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "Profile Image",
                    fontSize = 12.sp,
                    color = Color.Gray,
                    textAlign = TextAlign.Center
                )
            }

            Spacer(modifier = Modifier.height(24.dp))

            // First Name
            Text("First Name:", fontWeight = FontWeight.Bold)
            OutlinedTextField(
                value = firstName,
                onValueChange = { firstName = it },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 4.dp, bottom = 12.dp),
                shape = RoundedCornerShape(8.dp),
                colors = TextFieldDefaults.colors(
                    focusedContainerColor = Color.White,
                    unfocusedContainerColor = Color.White
                )
            )

            // Last Name
            Text("Last Name:", fontWeight = FontWeight.Bold)
            OutlinedTextField(
                value = lastName,
                onValueChange = { lastName = it },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 4.dp, bottom = 12.dp),
                shape = RoundedCornerShape(8.dp),
                colors = TextFieldDefaults.colors(
                    focusedContainerColor = Color.White,
                    unfocusedContainerColor = Color.White
                )
            )

            // Static contact and email
            Text("micaella.obeso@cit.edu", color = Color.Gray, fontSize = 14.sp)

            Spacer(modifier = Modifier.height(16.dp))

            // Course
            Text("Course/Program:", fontWeight = FontWeight.Bold)
            ExposedDropdownMenuBox(
                expanded = courseDropdownExpanded,
                onExpandedChange = { courseDropdownExpanded = !courseDropdownExpanded }
            ) {
                OutlinedTextField(
                    value = course,
                    onValueChange = {},
                    readOnly = true,
                    trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = courseDropdownExpanded) },
                    modifier = Modifier
                        .fillMaxWidth()
                        .menuAnchor()
                        .padding(top = 4.dp, bottom = 12.dp),
                    shape = RoundedCornerShape(8.dp),
                    colors = TextFieldDefaults.colors(
                        focusedContainerColor = Color.White,
                        unfocusedContainerColor = Color.White
                    )
                )
                ExposedDropdownMenu(
                    expanded = courseDropdownExpanded,
                    onDismissRequest = { courseDropdownExpanded = false }
                ) {
                    courseOptions.forEach { selectionOption ->
                        DropdownMenuItem(
                            text = { Text(selectionOption) },
                            onClick = {
                                course = selectionOption
                                courseDropdownExpanded = false
                            }
                        )
                    }
                }
            }

            // Year Level
            Text("Year Level:", fontWeight = FontWeight.Bold)
            ExposedDropdownMenuBox(
                expanded = yearDropdownExpanded,
                onExpandedChange = { yearDropdownExpanded = !yearDropdownExpanded }
            ) {
                OutlinedTextField(
                    value = yearLevel,
                    onValueChange = {},
                    readOnly = true,
                    trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = yearDropdownExpanded) },
                    modifier = Modifier
                        .fillMaxWidth()
                        .menuAnchor()
                        .padding(top = 4.dp, bottom = 12.dp),
                    shape = RoundedCornerShape(8.dp),
                    colors = TextFieldDefaults.colors(
                        focusedContainerColor = Color.White,
                        unfocusedContainerColor = Color.White
                    )
                )
                ExposedDropdownMenu(
                    expanded = yearDropdownExpanded,
                    onDismissRequest = { yearDropdownExpanded = false }
                ) {
                    yearOptions.forEach { selectionOption ->
                        DropdownMenuItem(
                            text = { Text(selectionOption) },
                            onClick = {
                                yearLevel = selectionOption
                                yearDropdownExpanded = false
                            }
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            // Save and Cancel Buttons
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically
            ) {
                TextButton(onClick = { navController.popBackStack() }) {
                    Text("Cancel", color = Color(0xFFB71C1C))
                }
                Spacer(modifier = Modifier.width(24.dp))
                Button(
                    onClick = {
                        // ✅ Save updated profile
                        ProfileData.firstName = firstName
                        ProfileData.lastName = lastName
                        ProfileData.course = course
                        ProfileData.yearLevel = yearLevel

                        // ✅ Navigate back to ProfileScreen
                        navController.navigate("profile") {
                            popUpTo("dashboard") { inclusive = false }
                        }
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFDE6A00)),
                    shape = RoundedCornerShape(8.dp)
                ) {
                    Text("Save", color = Color.Black, fontWeight = FontWeight.Bold)
                }
            }
        }
    }
}
