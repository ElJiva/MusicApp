package com.ivan.musicapp.services

import com.ivan.musicapp.models.Album
import com.ivan.musicapp.models.AlbumDetail
import retrofit2.http.GET
import retrofit2.http.Path

interface AlbumService {
    @GET ("albums")
    suspend fun getAllAlbums(): List<Album>

    @GET ("albums/{id}")
    suspend fun getAlbumById(@Path("id") id: String): AlbumDetail
}