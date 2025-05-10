package com.example.ccsgadgethub

import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Response
import retrofit2.http.*

interface ApiService {

    // === Items ===
    @GET("api/items")
    suspend fun getAllItems(): List<Item>

    @GET("api/items/{id}")
    suspend fun getItemById(@Path("id") id: String): Map<String, Any>

    @POST("api/items")
    suspend fun addItem(@Body itemData: Map<String, String>): Response<Map<String, Any>>

    @PUT("api/items/{id}")
    suspend fun updateItem(@Path("id") id: String, @Body updateData: Map<String, String>): Response<Map<String, Any>>

    @DELETE("api/items/{id}")
    suspend fun deleteItem(@Path("id") id: String): Response<Map<String, Any>>

    // === Borrow Requests ===
    @GET("api/requests")
    suspend fun getAllRequests(@Query("status") status: String? = null): List<BorrowRequest>

    @GET("api/requests/user/{userId}")
    suspend fun getRequestsByUser(@Path("userId") userId: String): List<BorrowRequest>

    @GET("api/requests/{id}")
    suspend fun getRequestById(@Path("id") id: String): BorrowRequest

    @POST("api/requests")
    suspend fun createRequest(@Body request: BorrowRequest): Response<Map<String, Any>>

    @PUT("api/requests/{id}")
    suspend fun updateRequestStatus(@Path("id") id: String, @Body updateData: Map<String, String>): Response<Map<String, Any>>

    @DELETE("api/requests/{id}")
    suspend fun deleteRequest(@Path("id") id: String): Response<Map<String, Any>>

    // === Users ===
    @POST("api/users/sync")
    suspend fun syncUser(@Body dto: FirebaseUserDto): Response<User>

    // ✅ Fixed to match backend endpoint: /api/sync/get-by-uid?uid={uid}
    @GET("api/sync/get-by-uid")
    suspend fun getUserByUid(@Query("uid") uid: String): User

    @PUT("api/users/update")
    suspend fun updateUserProfile(@Body dto: ProfileUpdateDto): Response<User>

    @Multipart
    @POST("api/users/upload")
    suspend fun uploadProfileImage(
        @Part file: MultipartBody.Part,
        @Part("uid") uid: RequestBody
    ): Response<String>

    @PUT("users/{uid}/profile")
    suspend fun updateUserProfile(
        @Path("uid") uid: String,
        @Body profile: Map<String, String>
    ): retrofit2.Response<Unit>

}
