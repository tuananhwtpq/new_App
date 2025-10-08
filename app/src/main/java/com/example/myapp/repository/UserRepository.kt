package com.example.myapp.repository

import com.example.myapp.data.model.PhotoResponse
import com.example.myapp.data.model.User

interface UserRepository {

    suspend fun getUserProfile(username: String): Result<User>
    suspend fun getUserPhotos(
        username: String,
        page: Int,
        perPage: Int
    ): Result<List<PhotoResponse>>

    suspend fun getUserLikePhoto(
        username: String,
        page: Int,
        perPage: Int
    ): Result<List<PhotoResponse>>
}