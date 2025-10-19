
package com.ivan.musicapp.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil3.compose.AsyncImage
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.ui.unit.dp
import com.ivan.musicapp.models.Album
import com.ivan.musicapp.ui.theme.MusicAccent
import com.ivan.musicapp.ui.theme.MusicDark
import com.ivan.musicapp.ui.theme.MusicPrimary


// Album Large Card Component
@Composable
fun AlbumLargeCard(
    album: Album,
    onClick: () -> Unit
) {
    //Box del Album
    Box(
        modifier = Modifier
            .size(200.dp, 160.dp)
            .clip(RoundedCornerShape(20.dp))
            .clickable(onClick = onClick)
    ) {
        AsyncImage(
            model = album.image,
            contentDescription = album.title,
            contentScale = ContentScale.Crop,
            modifier = Modifier.fillMaxSize()
        )
        //Box del Title, Artist y Botón Play
        Box(
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .fillMaxWidth()
                .padding(horizontal = 12.dp, vertical = 12.dp)
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(60.dp)
                    .clip(RoundedCornerShape(15.dp))
                    .background(
                        Brush.verticalGradient(
                            colors = listOf(
                                MusicPrimary.copy(alpha = 0.8f),
                                MusicDark.copy(alpha = 0.95f)
                            )
                        )
                    )
                    .padding(horizontal = 12.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column(
                    modifier = Modifier.weight(1f)
                ) {
                    Text(
                        text = album.title,
                        fontWeight = FontWeight.Medium,
                        color = Color.White,
                        fontSize = 14.sp,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                    Text(
                        text = album.artist,
                        color = Color.White.copy(alpha = 0.8f),
                        fontSize = 12.sp,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                }

                IconButton(
                    onClick = { },
                    modifier = Modifier
                        .clip(CircleShape)
                        .background(Color.White.copy(alpha = 0.9f))
                        .size(30.dp),
                    colors = IconButtonDefaults.iconButtonColors(
                        contentColor = MusicDark
                    )
                ) {
                    Icon(
                        imageVector = Icons.Default.PlayArrow,
                        contentDescription = "Play Album"

                    )
                }
            }
        }
    }
}


// Recently Played Card Component
@Composable
fun RecentlyPlayedCard(
    album: Album,
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
            modifier = Modifier
                .padding(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            AsyncImage(
                model = album.image,
                contentDescription = album.title,
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
                    text = album.title,
                    fontWeight = FontWeight.Bold,
                    style = MaterialTheme.typography.titleMedium,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
                Spacer(modifier = Modifier.height(4.dp))

                Text(
                    text = "${album.artist} • Popular Song",
                    style = MaterialTheme.typography.bodySmall,
                    color = MusicDark.copy(alpha = 0.8f),
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
            }

            Spacer(modifier = Modifier.width(8.dp))

            IconButton(onClick = { }) {
                Icon(
                    imageVector = Icons.Default.MoreVert,
                    contentDescription = "More options",
                    tint = MusicDark.copy(alpha = 0.8f)
                )
            }
        }
    }
}