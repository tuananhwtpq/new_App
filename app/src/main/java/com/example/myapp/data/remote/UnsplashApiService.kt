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
        @Query("per_page") perPage: Int
    ): List<PhotoResponse>

    @GET("/collections")
    suspend fun getAllCollections(
        @Query("page") page: Int,
        @Query("per_page") perPage: Int
    ): List<CollectionResponse>

    @GET("/collections/{id}")
    suspend fun getPrivateCollections(
        @Query("id") id: String
    ): List<CollectionResponse>

    @GET("/users/{username}")
    suspend fun getPublicProfile(
        @Path("username") username: String
    ): User

    @GET("/photos/{id}")
    suspend fun getPhotoById(
        @Path("id") id: String
    ): PhotoResponse

    /**
     * Tìm kiếm ảnh theo một query.
     * @param query Từ khóa tìm kiếm (bắt buộc).
     * @param page Số trang (tùy chọn).
     * @param perPage Số lượng item trên mỗi trang (tùy chọn).
     * @param orderBy Cách sắp xếp (tùy chọn, ví dụ: 'latest' hoặc 'relevant').
     * @param collections Lọc theo ID của collection (tùy chọn).
     * @param contentFilter Lọc nội dung an toàn (tùy chọn, 'low' hoặc 'high').
     * @param color Lọc theo màu sắc (tùy chọn).
     * @param orientation Lọc theo hướng ảnh (tùy chọn, 'landscape', 'portrait', 'squarish').
     * @param lang Ngôn ngữ của query (tùy chọn).
     */
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
}