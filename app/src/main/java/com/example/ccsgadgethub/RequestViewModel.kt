package com.example.ccsgadgethub.viewmodel

import androidx.compose.runtime.mutableStateListOf
import androidx.lifecycle.ViewModel

data class RequestItem(
    val id: Int,  // Added an ID to identify each request uniquely
    val item: String,
    val requestDate: String,
    val status: String,
    val returnTime: String
)

class RequestViewModel : ViewModel() {
    var requests = mutableStateListOf<RequestItem>()
        private set

    // Add a new request to the list
    fun addRequest(item: String, requestDate: String, status: String, returnTime: String) {
        val newId = requests.size + 1  // Simple ID generation
        requests.add(RequestItem(newId, item, requestDate, status, returnTime))
    }

    // Function to confirm a request (change its status)
    fun confirmRequest(requestId: Int) {
        // Modify the request status to "Confirmed" for the given ID
        val updatedRequests = requests.map {
            if (it.id == requestId) {
                it.copy(status = "Confirmed")  // Update status to "Confirmed"
            } else {
                it
            }
        }
        // Assign the updated list back to `requests`
        requests.clear()
        requests.addAll(updatedRequests)
    }
}
