package com.example.myapp.repository

import com.example.myapp.data.model.PhotoResponse
import com.example.myapp.data.model.User

interface HomeItemDetailRepository  {

    suspend fun getPrivateProfile(username: String): Result<User>

    suspend fun getPhotoById(id: String) : Result<PhotoResponse>
}