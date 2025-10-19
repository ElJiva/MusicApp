// 'src/main/java/com/ivan/musicapp/screens/DetailScreen.kt'
package com.ivan.musicapp.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items


import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
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
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil3.compose.AsyncImage
import com.ivan.musicapp.models.AlbumDetail
import com.ivan.musicapp.services.AlbumService
import com.ivan.musicapp.components.MiniPlayer
import com.ivan.musicapp.ui.theme.MusicAccent
import com.ivan.musicapp.ui.theme.MusicBackground
import com.ivan.musicapp.ui.theme.MusicDark
import com.ivan.musicapp.ui.theme.MusicPrimary
import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaType
import retrofit2.Retrofit
import retrofit2.converter.kotlinx.serialization.asConverterFactory

@Composable
fun DetailScreen(id: String, onBackClick: () -> Unit) {
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
    var album by remember { mutableStateOf<AlbumDetail?>(null) }

    LaunchedEffect(id) {
        isLoading = true
        try {
            album = api.getAlbumById(id)
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
        error != null -> Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            Text("Error: $error")
        }
        else -> album?.let { a ->
            Scaffold(
                containerColor = MusicBackground,
                bottomBar = {
                    MiniPlayer(
                        title = a.title ?: "",
                        artist = a.artist ?: "",
                        image = a.image ?: ""
                    )
                }
            ) { paddingValues ->
                LazyColumn(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(bottom = paddingValues.calculateBottomPadding())
                ) {
                    // Header
                    item {
                        DetailHeader(
                            album = a,
                            onBackClick = onBackClick
                        )
                    }

                    // 2. Tarjeta "About this album"
                    item {
                        AboutCard(description = a.description ?: "No description available.")
                    }

                    item {
                        Card(
                            modifier = Modifier
                                .padding(start = 16.dp, end = 16.dp, bottom = 8.dp),
                            shape = RoundedCornerShape(12.dp),
                            colors = CardDefaults.cardColors(containerColor = Color.White),
                            elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
                        ) {
                            Text(
                                text = "Artist: ${a.artist ?: "Unknown"}",
                                style = MaterialTheme.typography.bodyMedium,
                                fontWeight = FontWeight.Medium,
                                color = Color.Gray,
                                modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)
                            )
                        }
                    }

                    // 4. Lista de 10 tracks FICTICIOS
                    items(10) { index ->
                        val trackTitle = "${a.title ?: "Track"} • Track ${index + 1}"
                        TrackCard(
                            trackTitle = trackTitle,
                            albumImage = a.image ?: "",
                            artistName = a.artist ?: "",
                            onClick = { }
                        )
                    }
                }
            }
        }
    }
}


@Composable
private fun DetailHeader(album: AlbumDetail, onBackClick: () -> Unit) {

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp),
        shape = RoundedCornerShape(20.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
        colors = CardDefaults.cardColors(containerColor = Color.Transparent)
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(350.dp)
        ) {
            AsyncImage(
                model = album.image,
                contentDescription = album.title,
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .fillMaxSize()
                    .clip(RoundedCornerShape(0.dp))
            )


            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(
                        Brush.verticalGradient(
                            colors = listOf(
                                Color.Transparent,
                                MusicDark.copy(alpha = 0.8f)
                            ),
                            startY = 400f
                        )
                    )
            )

            // Contenido (Iconos, Texto, Botones)
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    IconButton(onClick = onBackClick) {
                        Icon(
                            imageVector = Icons.Default.ArrowBack,
                            contentDescription = "Back",
                            tint = Color.White
                        )
                    }
                    IconButton(onClick = { }) {
                        Icon(
                            imageVector = Icons.Default.FavoriteBorder,
                            contentDescription = "Favorite",
                            tint = Color.White
                        )
                    }
                }

                Spacer(modifier = Modifier.weight(1f))

                // Textos
                Text(
                    text = album.title ?: "Untitled",
                    color = Color.White,
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Bold
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = album.artist ?: "Unknown Artist",
                    color = Color.White.copy(alpha = 0.8f),
                    fontSize = 16.sp
                )
                Spacer(modifier = Modifier.height(16.dp))

                // Botones de Play (Shuffle)
                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    // Botón de Play principal (verde)
                    IconButton(
                        onClick = {},
                        modifier = Modifier
                            .size(56.dp)
                            .clip(CircleShape)
                            .background(MusicPrimary)
                    ) {
                        Icon(
                            imageVector = Icons.Default.PlayArrow,
                            contentDescription = "Play",
                            tint = Color.White
                        )
                    }
                    Spacer(modifier = Modifier.width(16.dp))
                    IconButton(
                        onClick = {},
                        modifier = Modifier
                            .size(56.dp)
                            .clip(CircleShape)
                            .background(Color.White.copy(alpha = 0.9f))
                    ) {
                        Icon(
                            imageVector = Icons.Default.PlayArrow,
                            contentDescription = "Shuffle",
                            tint = MusicDark
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun AboutCard(description: String) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            Text(
                text = "About this album",
                fontWeight = FontWeight.Bold,
                style = MaterialTheme.typography.titleMedium
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = description,
                style = MaterialTheme.typography.bodyMedium,
                color = Color.Gray
            )
        }
    }
}

@Composable
private fun TrackCard(
    trackTitle: String,
    albumImage: String,
    artistName: String,
    onClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .padding(vertical = 4.dp, horizontal = 16.dp)
            .fillMaxWidth()
            .clickable(onClick = onClick),
        shape = RoundedCornerShape(12.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White)
    ) {
        Row(
            modifier = Modifier.padding(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            AsyncImage(
                model = albumImage,
                contentDescription = trackTitle,
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .size(48.dp)
                    .clip(RoundedCornerShape(8.dp))
                    .background(MusicAccent.copy(alpha = 0.2f))
            )
            Spacer(modifier = Modifier.width(16.dp))
            Column(
                modifier = Modifier.weight(1f)
            ) {
                Text(
                    text = trackTitle,
                    fontWeight = FontWeight.Bold,
                    style = MaterialTheme.typography.titleMedium,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = artistName,
                    style = MaterialTheme.typography.bodySmall,
                    color = MusicDark.copy(alpha = 0.8f),
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
            }
            Spacer(modifier = Modifier.width(8.dp))
            IconButton(onClick = {}) {
                Icon(
                    imageVector = Icons.Default.MoreVert,
                    contentDescription = "More options",
                    tint = MusicDark.copy(alpha = 0.8f)
                )
            }
        }
    }
}

