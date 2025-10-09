package com.example.myapp.data.remote

import com.example.myapp.data.model.CollectionResponse
import com.example.myapp.data.model.PhotoResponse
import com.example.myapp.data.model.SearchCollectionResponse
import com.example.myapp.data.model.SearchPhotoResponse
import com.example.myapp.data.model.SearchUserResponse
import com.example.myapp.data.model.User
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface UnsplashApiService {

    @GET("/photos")
    suspend fun getAllPhotos(
        @Query("page") page: Int,
        @Query("per_page") perPage: Int,
        @Query("order_by") orderBy: String?

    ): List<PhotoResponse>

    @GET("/collections")
    suspend fun getAllCollections(
        @Query("page") page: Int,
        @Query("per_page") perPage: Int
    ): List<CollectionResponse>

    @GET("/collections/{id}")
    suspend fun getCollectionById(
        @Path("id") id: String
    ): CollectionResponse

    @GET("/collections/{id}/photos")
    suspend fun getCollectionPhotos(
        @Path("id") id: String,
        @Query("page") page: Int,
        @Query("per_page") perPage: Int
    ): List<PhotoResponse>

    @GET("/users/{username}")
    suspend fun getPublicProfile(
        @Path("username") username: String
    ): User

    @GET("/photos/{id}")
    suspend fun getPhotoById(
        @Path("id") id: String
    ): PhotoResponse


    @GET("/search/photos")
    suspend fun searchPhotos(
        @Query("query") query: String,
        @Query("page") page: Int? = null,
        @Query("per_page") perPage: Int? = null,
        @Query("order_by") orderBy: String? = null,
        @Query("collections") collections: String? = null,
        @Query("content_filter") contentFilter: String? = null,
        @Query("color") color: String? = null,
        @Query("orientation") orientation: String? = null,
        @Query("lang") lang: String? = null
    ): SearchPhotoResponse

    @GET("/search/collections")
    suspend fun searchCollections(
        @Query("query") query: String,
        @Query("page") page: Int? = null,
        @Query("per_page") perPage: Int? = null,
    ): SearchCollectionResponse

    @GET("/search/users")
    suspend fun searchUsers(
        @Query("query") query: String,
        @Query("page") page: Int? = null,
        @Query("per_page") perPage: Int? = null
    ): SearchUserResponse

    @GET("users/{username}")
    suspend fun getUserProfile(
        @Path("username") username: String
    ): User

    @GET("users/{username}/photos")
    suspend fun getUserPhotos(
        @Path("username") username: String,
        @Query("page") page: Int,
        @Query("per_page") perPage: Int
    ): List<PhotoResponse>

    @GET("users/{username}/likes")
    suspend fun getUserLikes(
        @Path("username") username: String,
        @Query("page") page: Int,
        @Query("per_page") perPage: Int
    ): List<PhotoResponse>

    @GET("users/{username}/collections")
    suspend fun getUserCollections(
        @Path("username") username: String,
        @Query("page") page: Int,
        @Query("per_page") perPage: Int
    ): List<CollectionResponse>

}