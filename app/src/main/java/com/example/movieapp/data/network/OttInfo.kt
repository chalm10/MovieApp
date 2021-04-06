package com.example.movieapp.data.network

import com.google.gson.annotations.SerializedName

data class OttInfo (
    @SerializedName("imdbid")
    val imdbid : String,
    @SerializedName("streamingAvailability")
    val streamingAvailability : StreamingAvailability
)