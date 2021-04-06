package com.example.movieapp.data.network

import com.google.gson.annotations.SerializedName

data class StreamingAvailability (
    @SerializedName("country")
    val country : Country
)