package com.otus.homework

import com.google.gson.annotations.SerializedName

data class Image(
    @field:SerializedName("file")
    val url: String
)