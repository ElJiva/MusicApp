// 'src/main/java/com/ivan/musicapp/screens/DetailScreen.kt'
package com.ivan.musicapp.screens

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import coil3.compose.AsyncImage
import com.ivan.musicapp.models.AlbumDetail
import com.ivan.musicapp.services.AlbumService
import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaType
import retrofit2.Retrofit
import retrofit2.converter.kotlinx.serialization.asConverterFactory

@Composable
fun DetailScreen(id: String) {
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
            CircularProgressIndicator()
        }
        error != null -> Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            Text("Error: $error")
        }
        else -> album?.let { a ->
            Column(modifier = Modifier.fillMaxSize().padding(16.dp)) {
                AsyncImage(
                    model = a.image,
                    contentDescription = a.title,
                    modifier = Modifier.fillMaxWidth().height(200.dp)
                )
                Spacer(Modifier.height(12.dp))
                Text(a.title ?: "Untitled", style = MaterialTheme.typography.headlineSmall)
                Text(a.artist ?: "Unknown", style = MaterialTheme.typography.titleMedium)
                Spacer(Modifier.height(8.dp))
                Text(a.description ?: "-", style = MaterialTheme.typography.bodyMedium)
            }
        }
    }
}
