package com.example.myapp.repository

import com.example.myapp.data.model.CollectionResponse

interface CollectionRepository {

    suspend fun getAllCollections(page: Int, perPage: Int): Result<List<CollectionResponse>>
    suspend fun getPrivateCollections(id: String) : Result<List<CollectionResponse>>

}