package com.example.ccsgadgethub

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.ccsgadgethub.ui.*
import com.example.ccsgadgethub.viewmodel.RequestViewModel
import com.example.ccsgadgethub.ui.theme.CCSGadgetHubTheme // ✅ Fixed import

import java.net.URLDecoder
import java.nio.charset.StandardCharsets

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            CCSGadgetHubTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    val navController = rememberNavController()

                    NavHost(
                        navController = navController,
                        startDestination = "splash"
                    ) {
                        // Basic Screens
                        composable("splash") { SplashScreen(navController) }
                        composable("login") { LoginScreen(navController) }
                        composable("register") { RegisterScreen(navController) }
                        composable("home") { HomeScreen(navController) }
                        composable("items") { ItemScreen(navController) }
                        composable("profile") { ProfileScreen(navController) }
                        composable("edit_profile") { EditProfileScreen(navController) }

                        // Dashboard Screen
                        composable("dashboard") { DashboardScreen(navController) }

                        // ✅ My Requests Screen
                        composable("my_requests") {
                            val requestViewModel: RequestViewModel = viewModel()
                            MyRequestsScreen(navController, requestViewModel)
                        }

                        // Item Detail Screen
                        composable(
                            route = "item_detail/{itemName}",
                            arguments = listOf(navArgument("itemName") { type = NavType.StringType })
                        ) { backStackEntry ->
                            val itemName = backStackEntry.arguments?.getString("itemName")?.let {
                                URLDecoder.decode(it, StandardCharsets.UTF_8.name())
                            }
                            ItemDetailScreen(navController, itemName)
                        }

                        // Request Form Screen
                        composable(
                            route = "request_form/{itemName}",
                            arguments = listOf(navArgument("itemName") { type = NavType.StringType })
                        ) { backStackEntry ->
                            val itemNameArg = backStackEntry.arguments?.getString("itemName")?.let {
                                URLDecoder.decode(it, StandardCharsets.UTF_8.name())
                            }
                            RequestFormScreen(navController, itemNameArg)
                        }
                    }
                }
            }
        }
    }
}
