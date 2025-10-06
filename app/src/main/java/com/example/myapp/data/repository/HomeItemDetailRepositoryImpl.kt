package com.example.myapp.data.repository

import com.example.myapp.data.model.PhotoResponse
import com.example.myapp.data.model.User
import com.example.myapp.data.remote.UnsplashApiService
import com.example.myapp.repository.HomeItemDetailRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

class HomeItemDetailRepositoryImpl @Inject constructor(
    private val apiService: UnsplashApiService
) : HomeItemDetailRepository {
    override suspend fun getPrivateProfile(username: String): Result<User> {
        return withContext(Dispatchers.IO) {
            try {
                val result = apiService.getPublicProfile(username)
                Result.success(result)
            } catch (e: Exception) {
                Result.failure(e)
            }
        }
    }

    override suspend fun getPhotoById(id: String): Result<PhotoResponse> {
        return withContext(Dispatchers.IO) {
            try {

                val result = apiService.getPhotoById(id)
                Result.success(result)

            } catch (e: Exception) {
                Result.failure(e)
            }
        }
    }
}