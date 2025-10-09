package com.example.myapp.data.model

import com.google.gson.annotations.SerializedName

data class CollectionResponse(

    @SerializedName("id")
    val id: String,

    @SerializedName("title")
    val title: String?,

    @SerializedName("description")
    val description: String?,

    @SerializedName("total_photos")
    val total_photos: Int,

    @SerializedName("blur_hash")
    val blur_hash: String?,

    @SerializedName("color")
    val color: String?,

    @SerializedName("links")
    val links: Link?,

    @SerializedName("user")
    val user: User,

    @SerializedName("cover_photo")
    val cover_photo: CoverPhoto?,


    )