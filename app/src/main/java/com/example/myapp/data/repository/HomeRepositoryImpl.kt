package com.example.myapp.data.repository

import com.example.myapp.data.model.PhotoResponse
import com.example.myapp.data.remote.UnsplashApiService
import com.example.myapp.repository.HomeRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import okhttp3.Dispatcher
import javax.inject.Inject

class HomeRepositoryImpl @Inject constructor(
    private val apiService: UnsplashApiService
) : HomeRepository {
    override suspend fun getAllPhotos(
        page: Int,
        perPage: Int
    ): Result<List<PhotoResponse>> {
        return withContext(Dispatchers.IO) {
            try {
                val photoList = apiService.getAllPhotos(page, perPage)
                Result.success(photoList)
            } catch (e: Exception) {
                Result.failure(e)
            }
        }
    }
}