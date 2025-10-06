package com.example.myapp.data.model

import com.google.gson.annotations.SerializedName

data class SearchPhotoResponse(

    @SerializedName("total")
    val total: Int,

    @SerializedName("total_pages")
    val total_pages: Int,

    @SerializedName("results")
    val results: List<PhotoResponse>

)