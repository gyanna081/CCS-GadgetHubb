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
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController

@Composable
fun ProfileScreen(navController: NavController) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFFCF3E8))
            .padding(horizontal = 16.dp)
    ) {
        Spacer(modifier = Modifier.height(WindowInsets.statusBars.asPaddingValues().calculateTopPadding()))

        // Top Bar
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
                modifier = Modifier.height(60.dp),
                contentScale = ContentScale.Fit
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Profile Content
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .background(Color(0xFFF9ECD8), RoundedCornerShape(16.dp))
                .padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            // Dynamic Name and Role
            Text(
                text = "${ProfileData.firstName} ${ProfileData.lastName}",
                fontWeight = FontWeight.Bold,
                fontSize = 18.sp
            )
            Text("Student", color = Color.Gray, fontSize = 14.sp)

            Spacer(modifier = Modifier.height(16.dp))

            // Contact Number and Email (static)
            Text("micaella.obeso@cit.edu", color = Color.Gray, fontSize = 14.sp)

            Spacer(modifier = Modifier.height(16.dp))

            // Dynamic Course and Year Level
            Text(
                text = ProfileData.course,
                fontWeight = FontWeight.Bold,
                fontSize = 16.sp
            )
            Text(
                text = ProfileData.yearLevel,
                color = Color.Gray,
                fontSize = 14.sp
            )

            Spacer(modifier = Modifier.height(24.dp))

            // ✅ Corrected Edit Profile Button (correct route "edit_profile")
            Button(
                onClick = { navController.navigate("edit_profile") }, // ✅ fixed here
                modifier = Modifier
                    .fillMaxWidth(0.5f)
                    .height(45.dp),
                shape = RoundedCornerShape(8.dp),
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFDE6A00))
            ) {
                Text("Edit Profile", color = Color.Black, fontWeight = FontWeight.Bold)
            }
        }
    }
}