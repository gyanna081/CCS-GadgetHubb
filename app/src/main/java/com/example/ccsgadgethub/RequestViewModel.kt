package com.example.ccsgadgethub.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.ccsgadgethub.BorrowRequest
import com.example.ccsgadgethub.RetrofitClient
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class RequestViewModel : ViewModel() {

    private val _requests = MutableStateFlow<List<BorrowRequest>>(emptyList())
    val requests: StateFlow<List<BorrowRequest>> = _requests.asStateFlow()

    // Add initialization to fetch requests when the ViewModel is created
    init {
        fetchRequests()
    }

    fun fetchRequests() {
        viewModelScope.launch {
            try {
                val result = RetrofitClient.api.getAllRequests()
                _requests.value = result
                Log.d("RequestViewModel", "Fetched ${result.size} requests")
            } catch (e: Exception) {
                Log.e("RequestViewModel", "Error fetching requests", e)
                // Keep the list as is or set to empty depending on your preference
                // _requests.value = emptyList()
            }
        }
    }

    fun createRequestOnBackend(request: BorrowRequest) {
        viewModelScope.launch {
            try {
                val response = RetrofitClient.api.createRequest(request)
                if (response.isSuccessful) {
                    Log.d("RequestViewModel", "Successfully created request")
                    fetchRequests() // Refresh the list
                } else {
                    Log.e("RequestViewModel", "Failed to create request: ${response.code()}")
                }
            } catch (e: Exception) {
                Log.e("RequestViewModel", "Error creating request", e)
            }
        }
    }

    // Add the missing updateRequest method that's used in RequestSummary.kt
    fun updateRequest(requestId: String, updateData: Map<String, String>) {
        viewModelScope.launch {
            try {
                Log.d("RequestViewModel", "Updating request $requestId with data: $updateData")
                val response = RetrofitClient.api.updateRequestStatus(requestId, updateData)
                if (response.isSuccessful) {
                    Log.d("RequestViewModel", "Successfully updated request")
                    fetchRequests() // Refresh the list
                } else {
                    Log.e("RequestViewModel", "Failed to update request: ${response.code()}")
                }
            } catch (e: Exception) {
                Log.e("RequestViewModel", "Error updating request", e)
            }
        }
    }
}