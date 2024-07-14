package com.neurobeat.neurobeats.music.view

import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import coil.compose.rememberAsyncImagePainter
import com.neurobeat.neurobeats.api.models.Track
import com.neurobeat.neurobeats.music.viewmodels.PlaylistViewModel

@Composable
fun TracksScreen(
    navController: NavHostController,
    viewModel: PlaylistViewModel,
    playlistId: String,
    accessToken: String
) {
    LaunchedEffect(playlistId, accessToken) {
        viewModel.fetchPlaylistTracks(accessToken, playlistId)
    }

    val tracks by viewModel.tracks.observeAsState(emptyList())

    LazyColumn {
        items(tracks) { trackItem ->
            TrackItemView(track = trackItem.track) {
                navController.navigate("MusicPlayer/${trackItem.track}")
                Log.d("TracksScreen", "Track clicked: ${trackItem.track.name}")
            }
        }
    }
}

@Composable
fun TrackItemView(track: Track, onClick: () -> Unit) {
    Card(
        modifier = Modifier
            .padding(10.dp, 15.dp)
            .fillMaxWidth()
            .clickable { onClick() },
        shape = RoundedCornerShape(8.dp)
    ) {
        Row(modifier = Modifier.padding(8.dp)) {
            if (track.album.images.isNotEmpty()) {
                val imageUrl = track.album.images.first().url
                Image(
                    painter = rememberAsyncImagePainter(model = imageUrl),
                    contentDescription = "Track image",
                    modifier = Modifier
                        .size(50.dp)
                        .clip(RoundedCornerShape(8.dp))
                )
            }
            Spacer(modifier = Modifier.width(15.dp))
            Column {
                Text(text = track.name, fontSize = 18.sp)
                Text(text = track.artists.joinToString(", ") { it.name }, fontSize = 16.sp)
            }
        }
    }
}
