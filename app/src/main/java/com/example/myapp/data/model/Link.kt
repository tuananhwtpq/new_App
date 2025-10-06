package com.example.myapp.data.model

import com.google.gson.annotations.SerializedName

data class Link(

    @SerializedName("self")
    val self: String,

    @SerializedName("html")
    val html: String,

    @SerializedName("photos")
    val photos: String,

    @SerializedName("related")
    val related: String

)
