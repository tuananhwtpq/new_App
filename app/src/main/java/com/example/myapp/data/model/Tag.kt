package com.example.myapp.data.model

import com.google.gson.annotations.SerializedName

data class Tag(
    @SerializedName("title")
    val title: String?,
)
