package com.example.myapp.repository

import com.example.myapp.data.model.CollectionResponse
import com.example.myapp.data.model.PhotoResponse

interface CollectionRepository {

    suspend fun getAllCollections(page: Int, perPage: Int): Result<List<CollectionResponse>>
    suspend fun getCollectionPhotos(id: String, page: Int, perPage: Int) : Result<List<PhotoResponse>>

}