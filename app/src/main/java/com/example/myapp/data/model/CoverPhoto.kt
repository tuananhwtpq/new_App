package com.example.myapp.data.model

import com.google.gson.annotations.SerializedName

data class CoverPhoto (
    @SerializedName("id")
    val id: String,

    @SerializedName("urls")
    val urls: PhotoUrl?
)