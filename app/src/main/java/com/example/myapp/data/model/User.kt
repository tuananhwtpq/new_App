package com.example.myapp.data.model

import com.google.gson.annotations.SerializedName

data class User (

    @SerializedName("id")
    val id: String,

    @SerializedName("name")
    val name: String?,

    @SerializedName("username")
    val username: String?,

    @SerializedName("profile_image")
    val profile_image: ProfileImage?,

    @SerializedName("first_name")
    val first_name: String?,

    @SerializedName("last_name")
    val last_name: String?,

    @SerializedName("twitter_username")
    val twitter_username: String?,

    @SerializedName("portfolio_url")
    val portfolio_url: String?,

    @SerializedName("bio")
    val bio: String?,

    @SerializedName("location")
    val location: String?,

    @SerializedName("instagram_username")
    val instagram_username: String?,

    @SerializedName("total_collections")
    val total_collections: Int?,

    @SerializedName("total_likes")
    val total_likes: Int?,

    @SerializedName("total_photos")
    val total_photos: Int?,

    @SerializedName("downloads")
    val downloads: Int?,

    @SerializedName("social")
    val social: Social?,



    )