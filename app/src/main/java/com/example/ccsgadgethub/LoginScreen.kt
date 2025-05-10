package com.example.ccsgadgethub

import android.app.Activity
import android.content.Intent
import android.util.Log
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.ccsgadgethub.RetrofitClient
import com.example.ccsgadgethub.FirebaseUserDto
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import kotlinx.coroutines.launch
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth

@Composable
fun LoginScreen(navController: NavController) {
    val context = LocalContext.current
    val auth = Firebase.auth
    val scope = rememberCoroutineScope()

    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var passwordVisible by remember { mutableStateOf(false) }
    var loading by remember { mutableStateOf(false) }

    val launcher = rememberLauncherForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
        val task = GoogleSignIn.getSignedInAccountFromIntent(result.data)
        try {
            val account = task.getResult(ApiException::class.java)
            val credential = GoogleAuthProvider.getCredential(account.idToken, null)
            loading = true
            auth.signInWithCredential(credential).addOnCompleteListener { authResult ->
                if (authResult.isSuccessful) {
                    val currentUser = FirebaseAuth.getInstance().currentUser
                    if (currentUser != null) {
                        val userDto = FirebaseUserDto(
                            uid = currentUser.uid,
                            email = currentUser.email ?: "",
                            firstName = currentUser.displayName?.split(" ")?.firstOrNull() ?: "",
                            lastName = currentUser.displayName?.split(" ")?.drop(1)?.joinToString(" ") ?: ""
                        )

                        Log.d("DEBUG_USER_SYNC", "Syncing user: $userDto")
                        Log.d("RequestFetch", "UID used: ${FirebaseAuth.getInstance().currentUser?.uid}")

                        scope.launch {
                            try {
                                RetrofitClient.api.syncUser(userDto)
                                navController.navigate("home") {
                                    popUpTo("login") { inclusive = true }
                                }
                            } catch (e: Exception) {
                                Toast.makeText(context, "Sync failed: ${e.message}", Toast.LENGTH_LONG).show()
                            } finally {
                                loading = false
                            }
                        }
                    } else {
                        Toast.makeText(context, "User not found after login", Toast.LENGTH_SHORT).show()
                        loading = false
                    }
                } else {
                    Toast.makeText(context, "Google Sign-In Failed", Toast.LENGTH_SHORT).show()
                    loading = false
                }
            }
        } catch (e: ApiException) {
            Toast.makeText(context, "Sign-In Cancelled", Toast.LENGTH_SHORT).show()
            loading = false
        }
    }


    fun launchGoogleSignIn() {
        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(context.getString(R.string.default_web_client_id))
            .requestEmail()
            .build()
        val client = GoogleSignIn.getClient(context, gso)
        launcher.launch(client.signInIntent)
    }

    fun emailLogin() {
        if (email.isBlank() || password.isBlank()) {
            Toast.makeText(context, "Email and password are required", Toast.LENGTH_SHORT).show()
            return
        }
        loading = true
        auth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    navController.navigate("home") {
                        popUpTo("login") { inclusive = true }
                    }
                } else {
                    Toast.makeText(context, "Invalid credentials", Toast.LENGTH_SHORT).show()
                }
                loading = false
            }
    }

    // UI
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFFBF3E5))
            .padding(24.dp),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Image(painter = painterResource(id = R.drawable.logo), contentDescription = "Logo", modifier = Modifier.size(300.dp))

            OutlinedTextField(
                value = email,
                onValueChange = { email = it },
                placeholder = { Text("Email") },
                modifier = Modifier.fillMaxWidth(0.9f),
                shape = RoundedCornerShape(12.dp),
                singleLine = true
            )

            OutlinedTextField(
                value = password,
                onValueChange = { password = it },
                placeholder = { Text("Password") },
                visualTransformation = if (passwordVisible) VisualTransformation.None else PasswordVisualTransformation(),
                trailingIcon = {
                    IconButton(onClick = { passwordVisible = !passwordVisible }) {
                        Icon(if (passwordVisible) Icons.Filled.Visibility else Icons.Filled.VisibilityOff, contentDescription = null)
                    }
                },
                modifier = Modifier.fillMaxWidth(0.9f),
                shape = RoundedCornerShape(12.dp),
                singleLine = true
            )

            Button(
                onClick = { emailLogin() },
                modifier = Modifier.fillMaxWidth(0.5f),
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFDE6A00)),
                enabled = !loading
            ) {
                Text("Sign In", color = Color.Black, fontWeight = FontWeight.Bold)
            }

            Row(
                modifier = Modifier.fillMaxWidth(0.9f),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Divider(modifier = Modifier.weight(1f), color = Color.Gray)
                Text("  or  ", color = Color.Gray, fontSize = 14.sp)
                Divider(modifier = Modifier.weight(1f), color = Color.Gray)
            }

            Button(
                onClick = { launchGoogleSignIn() },
                modifier = Modifier.fillMaxWidth(0.7f),
                shape = RoundedCornerShape(8.dp),
                colors = ButtonDefaults.buttonColors(containerColor = Color.White),
                enabled = !loading
            ) {

                Spacer(modifier = Modifier.width(12.dp))
                Text("Sign in with Google", color = Color.Black)
            }

            Row {
                Text("Don't have an account?")
                Spacer(modifier = Modifier.width(4.dp))
                Text("Sign Up", color = Color(0xFFDE6A00), fontWeight = FontWeight.Bold, modifier = Modifier.clickable {
                    navController.navigate("register")
                })
            }
        }
    }
}
