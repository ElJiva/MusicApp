package com.ivan.musicapp.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Card

import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.ivan.musicapp.components.AlbumLargeCard
import com.ivan.musicapp.components.RecentlyPlayedCard
import com.ivan.musicapp.components.MiniPlayer
import com.ivan.musicapp.models.Album
import com.ivan.musicapp.services.AlbumService
import com.ivan.musicapp.ui.theme.MusicBackground
import com.ivan.musicapp.ui.theme.MusicDark
import com.ivan.musicapp.ui.theme.MusicPrimary
import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaType
import retrofit2.Retrofit
import retrofit2.converter.kotlinx.serialization.asConverterFactory

@Composable
fun HomeScreen(onAlbumClick: (String) -> Unit) {
    val json = remember {
        Json {
            ignoreUnknownKeys = true
            isLenient = true
            explicitNulls = false
        }
    }
    val retrofit = remember {
        Retrofit.Builder()
            .baseUrl("https://music.juanfrausto.com/api/")
            .addConverterFactory(json.asConverterFactory("application/json".toMediaType()))
            .build()
    }
    val api = remember { retrofit.create(AlbumService::class.java) }

    var isLoading by remember { mutableStateOf(true) }
    var error by remember { mutableStateOf<String?>(null) }
    var albums by remember { mutableStateOf<List<Album>>(emptyList()) }

    LaunchedEffect(Unit) {
        try {
            albums = api.getAllAlbums()
        } catch (e: Exception) {
            error = e.message ?: "Unknown error"
        } finally {
            isLoading = false
        }
    }

    when {
        isLoading -> Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            CircularProgressIndicator(color = MusicPrimary)
        }

        error != null -> Box(Modifier
            .fillMaxSize(),
            contentAlignment = Alignment.Center)
        {
            Text("Error: $error")
        }

        else -> {
            // Scaffold Para el MiniPlayer
            Scaffold(
                containerColor = MusicBackground,
                bottomBar = {
                    if (albums.isNotEmpty()) {
                        val firstAlbum = albums[0]
                        MiniPlayer(
                            title = firstAlbum.title,
                            artist = firstAlbum.artist,
                            image = firstAlbum.image,
                        )
                    }
                }
            ) { paddingValues ->
                // LazyColumn de las canciones
                LazyColumn(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(paddingValues)
                ) {
                    // 1. Header
                    item {
                        HomeHeader(name = "Ivan Rincon")
                    }

                    // 2. Sección "Albums"
                    item {
                        SectionHeader(title = "Albums")
                    }
                    item {
                        AlbumsHorizontalList(
                            albums = albums,
                            onAlbumClick = onAlbumClick
                        )
                    }
                    item {
                        SectionHeader(title = "Recently Played")
                    }
                    items(albums, key = { it.id }) { album ->
                        RecentlyPlayedCard(
                            album = album,
                            onClick = { onAlbumClick(album.id) }
                        )
                    }
                }
            }
        }
    }
}



@Composable
private fun HomeHeader(name: String) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        shape = RoundedCornerShape(20.dp),
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .background(
                    Brush.verticalGradient(
                        colors = listOf(
                            MusicPrimary,
                            MusicDark.copy(alpha = 0.9f)
                        )
                    )
                )
                .padding(horizontal = 20.dp, vertical = 24.dp)
        ) {
            //Contenido del Header
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    imageVector = Icons.Default.Menu,
                    contentDescription = "Menu",
                    tint = Color.White
                )
                Icon(
                    imageVector = Icons.Default.Search,
                    contentDescription = "Search",
                    tint = Color.White
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            Column(
                modifier = Modifier.fillMaxWidth(),
                horizontalAlignment = Alignment.Start
            ) {
                Text(
                    text = "Good Morning!",
                    color = Color.White.copy(alpha = 0.8f),
                    fontSize = 14.sp
                )
                Text(
                    text = name,
                    color = Color.White,
                    // Hacemos el nombre más grande
                    fontSize = 22.sp,
                    fontWeight = FontWeight.Bold
                )
            }
        }
    }
}
@Composable
private fun SectionHeader(title: String) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = title,
            style = MaterialTheme.typography.titleLarge,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.weight(1f)
        )
        Text(
            text = "See more",
            style = MaterialTheme.typography.bodyMedium,
            fontWeight = FontWeight.Medium,
            color = MusicPrimary
        )
    }
}

@Composable
private fun AlbumsHorizontalList(
    albums: List<Album>,
    onAlbumClick: (String) -> Unit
) {
    LazyRow(
        contentPadding = PaddingValues(horizontal = 16.dp),
        horizontalArrangement = Arrangement.spacedBy(16.dp),
        modifier = Modifier.padding(top = 8.dp, bottom = 16.dp)
    ) {
        items(albums) { album ->
            AlbumLargeCard(
                album = album,
                onClick = { onAlbumClick(album.id) }
            )
        }
    }
}
