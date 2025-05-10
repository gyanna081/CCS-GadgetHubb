package com.example.ccsgadgethub

data class BorrowRequest(
    val id: String? = null,

    // Item info
    val itemId: String = "",
    val itemName: String = "",

    // Borrower info
    val borrowerId: String = "",       // Firebase UID
    val borrowerName: String = "",     // First + Last name
    val borrowerEmail: String = "",

    // Request details
    val borrowDate: String = "",       // Same as startDate
    val startTime: String = "",        // Start of borrowing
    val returnTime: String = "",       // End/return time
    val timeRange: String = "",        // Display format
    val status: String = "Pending",
    val purpose: String = "",

    // Optional item fields
    val description: String = "",
    val itemCondition: String = "",

    // Timestamps
    val requestDate: String = "",
    val createdAt: String = ""
)
