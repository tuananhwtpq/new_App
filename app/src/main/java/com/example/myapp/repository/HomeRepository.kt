package com.example.myapp.repository

import com.example.myapp.data.model.PhotoResponse
import dagger.Binds

interface HomeRepository {

    suspend fun getAllPhotos(page: Int, perPage: Int, orderBy: String?): Result<List<PhotoResponse>>


}