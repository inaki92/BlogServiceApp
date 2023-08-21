package com.bignerdranch.android.blognerdranch.model

import com.google.gson.annotations.SerializedName

class Author {
    var name: String? = null
    @SerializedName("image")
    var imageUrl: String? = null
    var title: String? = null
}