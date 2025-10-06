package com.example.myapp.repository

import com.example.myapp.data.model.SearchCollectionResponse
import com.example.myapp.data.model.SearchPhotoResponse
import com.example.myapp.data.model.SearchUserResponse

interface SearchRepository {

    suspend fun searchPhotos(query: String, page: Int, perPage: Int): Result<SearchPhotoResponse>
    suspend fun searchCollections(
        query: String,
        page: Int,
        perPage: Int
    ): Result<SearchCollectionResponse>

    suspend fun searchUsers(query: String, page: Int, perPage: Int): Result<SearchUserResponse>
}