package com.example.myapp.data.repository

import com.example.myapp.data.model.PhotoResponse
import com.example.myapp.data.model.User
import com.example.myapp.data.remote.UnsplashApiService
import com.example.myapp.repository.UserRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

class UserRepositoryImpl @Inject constructor(
    private val apiService: UnsplashApiService
) : UserRepository {
    override suspend fun getUserProfile(username: String): Result<User> {
        return withContext(Dispatchers.IO) {
            try {
                val response = apiService.getUserProfile(username)
                Result.success(response)
            } catch (e: Exception) {
                Result.failure(e)
            }
        }
    }

    override suspend fun getUserPhotos(
        username: String,
        page: Int
    ): Result<List<PhotoResponse>> {
        return withContext(Dispatchers.IO) {
            try {
                val response = apiService.getUserPhotos(username, page, 10)
                Result.success(response)
            } catch (e: Exception) {
                Result.failure(e)
            }

        }
    }
}
