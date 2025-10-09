package com.example.myapp.data.model

import com.google.gson.annotations.SerializedName

data class CoverPhoto(
    @SerializedName("id")
    val id: String,

    @SerializedName("urls")
    val urls: PhotoUrl?,

    @SerializedName("width")
    val width: Int?,

    @SerializedName("height")
    val height: Int?,

    @SerializedName("color")
    val color: String?,

    @SerializedName("blur_hash")
    val blur_hash: String?,
)