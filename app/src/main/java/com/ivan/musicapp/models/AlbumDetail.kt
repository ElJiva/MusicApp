package com.ivan.musicapp.models

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class AlbumDetail(
    @SerialName("id") val id: String? = null,
    val title: String? = null,
    val artist: String? = null,
    val description: String? = null,
    val image: String? = null
)
