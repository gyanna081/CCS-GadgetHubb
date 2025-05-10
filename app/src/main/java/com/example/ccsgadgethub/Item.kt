package com.example.ccsgadgethub

data class Item(
    val id: String,
    val name: String,
    val description: String,
    val condition: String,
    val status: String,
    val imagePath: String, // This now contains the full Firebase Storage URL
    val createdAt: String
) {
    val imageUrl: String
        get() = if (imagePath.startsWith("http")) {
            // If imagePath is already a full URL, use it directly
            imagePath
        } else {
            // If it's a relative path, prepend the base URL
            "https://ccs-gadgethubb-frontend.onrender.com/$imagePath"
        }
}