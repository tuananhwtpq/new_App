package com.example.myapp.data.repository

import com.example.myapp.data.model.SearchCollectionResponse
import com.example.myapp.data.model.SearchPhotoResponse
import com.example.myapp.data.model.SearchUserResponse
import com.example.myapp.data.remote.UnsplashApiService
import com.example.myapp.repository.SearchRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

class SearchRepositoryImpl @Inject constructor(
    private val apiService: UnsplashApiService
) : SearchRepository {
    override suspend fun searchPhotos(
        query: String,
        page: Int,
        perPage: Int
    ): Result<SearchPhotoResponse> {
        return withContext(Dispatchers.IO) {
            try {
                val result = apiService.searchPhotos(query, page, perPage)
                Result.success(result)
            } catch (e: Exception) {
                Result.failure(e)
            }
        }
    }

    override suspend fun searchCollections(
        query: String,
        page: Int,
        perPage: Int
    ): Result<SearchCollectionResponse> {
        return withContext(Dispatchers.IO) {
            try {
                val result = apiService.searchCollections(query, page, perPage)
                Result.success(result)
            } catch (e: Exception) {
                Result.failure(e)
            }
        }
    }

    override suspend fun searchUsers(
        query: String,
        page: Int,
        perPage: Int
    ): Result<SearchUserResponse> {
        return withContext(Dispatchers.IO) {
            try {
                val result = apiService.searchUsers(query, page, perPage)
                Result.success(result)
            } catch (e: Exception) {
                Result.failure(e)
            }
        }
    }
}