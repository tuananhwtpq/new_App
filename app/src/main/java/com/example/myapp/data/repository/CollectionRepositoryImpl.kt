package com.example.myapp.data.repository

import com.example.myapp.data.model.CollectionResponse
import com.example.myapp.data.remote.UnsplashApiService
import com.example.myapp.repository.CollectionRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

class CollectionRepositoryImpl @Inject constructor(
    private val apiService: UnsplashApiService
) : CollectionRepository {
    override suspend fun getAllCollections(
        page: Int,
        perPage: Int
    ): Result<List<CollectionResponse>> {
        return withContext(Dispatchers.IO) {
            try {
                val collections = apiService.getAllCollections(page, perPage)
                Result.success(collections)
            } catch (e: Exception) {
                Result.failure(e)
            }
        }
    }

    override suspend fun getPrivateCollections(id: String): Result<List<CollectionResponse>> {
        return withContext(Dispatchers.IO) {
            try {
                val collections = apiService.getPrivateCollections(id)
                Result.success(collections)
            } catch (e: Exception) {
                Result.failure(e)
            }
        }
    }
}