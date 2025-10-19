package com.ivan.musicapp.models
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class Album(
    val title: String,
    val artist: String,
    val description: String,
    val image: String,
    @SerialName("id") val id: String
)