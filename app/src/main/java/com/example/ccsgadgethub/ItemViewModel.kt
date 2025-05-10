package com.example.ccsgadgethub.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.ccsgadgethub.Item
import com.example.ccsgadgethub.RetrofitClient
import com.example.ccsgadgethub.User
import com.example.ccsgadgethub.FirebaseUserDto
import com.example.ccsgadgethub.ProfileUpdateDto
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class ItemViewModel : ViewModel() {

    private val _items = MutableStateFlow<List<Item>>(emptyList())
    val items: StateFlow<List<Item>> = _items.asStateFlow()

    private val _userData = MutableStateFlow<User?>(null)
    val userData: StateFlow<User?> = _userData.asStateFlow()

    fun fetchItems() {
        viewModelScope.launch {
            try {
                _items.value = RetrofitClient.api.getAllItems()
            } catch (e: Exception) {
                Log.e("ItemViewModel", "Failed to fetch items", e)
            }
        }
    }

    fun fetchUserData() {
        viewModelScope.launch {
            try {
                val currentUser = FirebaseAuth.getInstance().currentUser
                Log.d("ItemViewModel", "Fetching user data for UID: ${currentUser?.uid}")

                if (currentUser != null) {
                    // First, set up a basic user object from Firebase even before API calls
                    val displayName = currentUser.displayName ?: ""
                    val nameParts = displayName.split(" ")
                    val firstName = nameParts.firstOrNull() ?: ""
                    val lastName = if (nameParts.size > 1) nameParts.drop(1).joinToString(" ") else ""

                    // Create a fallback user directly from Firebase data
                    // Including all the required properties
                    val fallbackUser = User(
                        uid = currentUser.uid,
                        email = currentUser.email ?: "",
                        firstName = if (firstName.isNotBlank()) firstName else "User",
                        lastName = lastName,
                        course = "",
                        year = "",
                        // Add the missing properties
                        createdAt = System.currentTimeMillis().toString(),  // Current timestamp as string
                        profileImageUrl = currentUser.photoUrl?.toString() ?: "",  // User's Firebase photo URL or empty string
                        role = "user"  // Default role
                    )

                    // Set this as the initial user data so we have something to display
                    if (_userData.value == null) {
                        Log.d("ItemViewModel", "Setting initial fallback user data: ${fallbackUser.firstName}")
                        _userData.value = fallbackUser
                    }

                    // Still try the API call, but don't depend on it for initial display
                    try {
                        Log.d("ItemViewModel", "Attempting to get user data from API")
                        val userData = RetrofitClient.api.getUserByUid(currentUser.uid)
                        Log.d("ItemViewModel", "Got user data from API: ${userData.firstName}")
                        _userData.value = userData
                    } catch (e: Exception) {
                        Log.e("ItemViewModel", "Error getting user from API: ${e.message}")
                        // We already set the fallback user above, so no need to do it again
                    }
                } else {
                    Log.e("ItemViewModel", "Current user is null, can't fetch user data")
                }
            } catch (e: Exception) {
                Log.e("ItemViewModel", "Error in fetchUserData: ${e.message}")
                e.printStackTrace()
            }
        }
    }

    fun updateUserProfile(uid: String, firstName: String, lastName: String, course: String, year: String) {
        viewModelScope.launch {
            try {
                val updatePayload = mapOf(
                    "firstName" to firstName,
                    "lastName" to lastName,
                    "course" to course,
                    "year" to year
                )

                // Use the correct endpoint based on your API
                val response = RetrofitClient.api.updateUserProfile(uid, updatePayload)
                if (response.isSuccessful) {
                    Log.d("UPDATE_USER", "Profile updated successfully")
                    fetchUserData() // Refresh local state after update
                } else {
                    Log.e("UPDATE_USER", "Failed to update profile: ${response.code()}")
                }
            } catch (e: Exception) {
                Log.e("UPDATE_USER", "Exception during update", e)
            }
        }
    }
}