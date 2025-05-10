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

// ✅ Correct composable imports
import com.example.ccsgadgethub.MyRequestsScreen
import com.example.ccsgadgethub.ItemScreen
import com.example.ccsgadgethub.ui.theme.CCSGadgetHubTheme

// ✅ ViewModels
import com.example.ccsgadgethub.viewmodel.ItemViewModel
import com.example.ccsgadgethub.viewmodel.RequestViewModel

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
                        composable("splash") { SplashScreen(navController) }
                        composable("login") { LoginScreen(navController) }
                        composable("register") { RegisterScreen(navController) }

                        composable("home") {
                            val itemViewModel: ItemViewModel = viewModel()
                            val requestViewModel: RequestViewModel = viewModel()
                            HomeScreen(
                                navController = navController,
                                itemViewModel = itemViewModel,
                                requestViewModel = requestViewModel
                            )
                        }

                        composable("items") {
                            val itemViewModel: ItemViewModel = viewModel()
                            ItemScreen(navController, itemViewModel)
                        }

                        composable("profile") {
                            val itemViewModel: ItemViewModel = viewModel()
                            ProfileScreen(navController = navController, itemViewModel = itemViewModel)
                        }

                        composable("edit_profile") {
                            val itemViewModel: ItemViewModel = viewModel()
                            EditProfileScreen(navController, itemViewModel)
                        }

                        composable("dashboard") {
                            val itemViewModel: ItemViewModel = viewModel()
                            DashboardScreen(navController, itemViewModel)
                        }

                        composable("my_requests") {
                            val requestViewModel: RequestViewModel = viewModel()
                            MyRequestsScreen(navController, requestViewModel)
                        }

                        // Updated to use itemId instead of itemName
                        composable(
                            route = "item_detail/{itemId}",
                            arguments = listOf(navArgument("itemId") { type = NavType.StringType })
                        ) { backStackEntry ->
                            val itemId = backStackEntry.arguments?.getString("itemId") ?: ""
                            val itemViewModel: ItemViewModel = viewModel()
                            ItemDetailScreen(
                                navController = navController,
                                itemId = itemId,
                                itemViewModel = itemViewModel
                            )
                        }

                        // Updated to use itemId for borrowing requests
                        composable(
                            route = "borrow_request/{itemId}",
                            arguments = listOf(navArgument("itemId") { type = NavType.StringType })
                        ) { backStackEntry ->
                            val itemId = backStackEntry.arguments?.getString("itemId") ?: ""
                            val itemViewModel: ItemViewModel = viewModel()
                            val requestViewModel: RequestViewModel = viewModel()
                            RequestFormScreen(
                                navController = navController,
                                itemId = itemId,
                                viewModel = requestViewModel,
                                itemViewModel = itemViewModel
                            )
                        }

                        // Keep old route for backward compatibility with updated parameters
                        composable(
                            route = "request_form/{itemName}",
                            arguments = listOf(navArgument("itemName") { type = NavType.StringType })
                        ) { backStackEntry ->
                            val itemName = backStackEntry.arguments?.getString("itemName") ?: ""
                            val itemViewModel: ItemViewModel = viewModel()
                            val requestViewModel: RequestViewModel = viewModel()
                            // Use itemName as itemId for backward compatibility
                            RequestFormScreen(
                                navController = navController,
                                itemId = itemName,
                                viewModel = requestViewModel,
                                itemViewModel = itemViewModel
                            )
                        }
                    }
                }
            }
        }
    }
}