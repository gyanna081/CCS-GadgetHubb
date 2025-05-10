package com.example.ccsgadgethub

data class ProfileUpdateDto(
    val uid: String,
    val firstName: String?,
    val lastName: String?,
    val course: String?,
    val year: String?
)
