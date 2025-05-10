package com.example.ccsgadgethub

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.ccsgadgethub.components.CommonBackButton
import com.example.ccsgadgethub.viewmodel.ItemViewModel
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EditProfileScreen(
    navController: NavController,
    itemViewModel: ItemViewModel = viewModel()
) {
    val user by itemViewModel.userData.collectAsState()
    var firstName by remember { mutableStateOf(user?.firstName ?: "") }
    var lastName by remember { mutableStateOf(user?.lastName ?: "") }
    var course by remember { mutableStateOf(user?.course ?: "") }
    var yearLevel by remember { mutableStateOf(user?.year ?: "") }

    val courseOptions = listOf("BSIT", "BSCS")
    val yearOptions = listOf("1st Year", "2nd Year", "3rd Year", "4th Year")
    var courseDropdownExpanded by remember { mutableStateOf(false) }
    var yearDropdownExpanded by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFFFF5E9))
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        CommonBackButton(navController = navController)

        Text(
            text = "Edit Profile",
            fontSize = 24.sp,
            fontWeight = FontWeight.Bold,
            color = Color(0xFFCC6600)
        )

        Spacer(modifier = Modifier.height(24.dp))

        OutlinedTextField(
            value = firstName,
            onValueChange = { firstName = it },
            label = { Text("First Name") },
            modifier = Modifier
                .fillMaxWidth()
                .background(Color.White, RoundedCornerShape(12.dp))
        )

        Spacer(modifier = Modifier.height(16.dp))

        OutlinedTextField(
            value = lastName,
            onValueChange = { lastName = it },
            label = { Text("Last Name") },
            modifier = Modifier
                .fillMaxWidth()
                .background(Color.White, RoundedCornerShape(12.dp))
        )

        Spacer(modifier = Modifier.height(16.dp))

        ExposedDropdownMenuBox(
            expanded = courseDropdownExpanded,
            onExpandedChange = { courseDropdownExpanded = !courseDropdownExpanded }
        ) {
            OutlinedTextField(
                readOnly = true,
                value = course,
                onValueChange = {},
                label = { Text("Course") },
                modifier = Modifier
                    .menuAnchor()
                    .fillMaxWidth()
                    .background(Color.White, RoundedCornerShape(12.dp))
            )
            ExposedDropdownMenu(
                expanded = courseDropdownExpanded,
                onDismissRequest = { courseDropdownExpanded = false }
            ) {
                courseOptions.forEach { option ->
                    DropdownMenuItem(
                        text = { Text(option) },
                        onClick = {
                            course = option
                            courseDropdownExpanded = false
                        }
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        ExposedDropdownMenuBox(
            expanded = yearDropdownExpanded,
            onExpandedChange = { yearDropdownExpanded = !yearDropdownExpanded }
        ) {
            OutlinedTextField(
                readOnly = true,
                value = yearLevel,
                onValueChange = {},
                label = { Text("Year Level") },
                modifier = Modifier
                    .menuAnchor()
                    .fillMaxWidth()
                    .background(Color.White, RoundedCornerShape(12.dp))
            )
            ExposedDropdownMenu(
                expanded = yearDropdownExpanded,
                onDismissRequest = { yearDropdownExpanded = false }
            ) {
                yearOptions.forEach { option ->
                    DropdownMenuItem(
                        text = { Text(option) },
                        onClick = {
                            yearLevel = option
                            yearDropdownExpanded = false
                        }
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(24.dp))

        Button(
            onClick = {
                val uid = FirebaseAuth.getInstance().currentUser?.uid
                if (uid != null) {
                    CoroutineScope(Dispatchers.IO).launch {
                        itemViewModel.updateUserProfile(
                            uid = uid,
                            firstName = firstName,
                            lastName = lastName,
                            course = course,
                            year = yearLevel
                        )
                    }
                    navController.popBackStack()
                }
            },
            shape = RoundedCornerShape(12.dp),
            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFCC6600)),
            modifier = Modifier
                .fillMaxWidth()
                .height(50.dp)
        ) {
            Text("Save", fontWeight = FontWeight.Bold, fontSize = 16.sp, color = Color.White)
        }
    }
}
