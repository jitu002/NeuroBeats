package com.neurobeat.neurobeats.music.model

import android.annotation.SuppressLint
import android.net.Uri
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Pause
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Repeat
import androidx.compose.material.icons.filled.Shuffle
import androidx.compose.material.icons.filled.SkipNext
import androidx.compose.material.icons.filled.SkipPrevious
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Slider
import androidx.compose.material3.SliderDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.media3.common.MediaItem
import androidx.media3.common.util.Log
import androidx.media3.common.util.UnstableApi
import androidx.media3.exoplayer.ExoPlayer
import com.neurobeat.neurobeats.api.models.Track
import com.neurobeat.neurobeats.api.models.TrackItem

@Composable
fun PlaybackControls(
    isPlaying: Boolean,
    isRepeating: Boolean,
    isShuffling: Boolean,
    onPlayPauseClicked: () -> Unit,
    onNextClicked: () -> Unit,
    onPreviousClicked: () -> Unit,
    onRepeatClicked: () -> Unit,
    onShuffleClicked: () -> Unit
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceEvenly,
        modifier = Modifier.fillMaxWidth()
    ) {
        IconButton(onClick = onShuffleClicked) {
            Icon(
                imageVector = Icons.Default.Shuffle,
                contentDescription = "Shuffle",
                tint = if (isShuffling) Color.Magenta else Color.White,
                modifier = Modifier.size(32.dp)
            )
        }
        Spacer(modifier = Modifier.width(5.dp))
        IconButton(onClick = onPreviousClicked) {
            Icon(
                imageVector = Icons.Default.SkipPrevious,
                contentDescription = "Previous",
                tint = Color.White,
                modifier = Modifier.size(45.dp)
            )
        }
        IconButton(
            onClick = onPlayPauseClicked,
            modifier = Modifier
                .size(64.dp)
                .clip(CircleShape)
                .background(Color.White)
                .padding(8.dp)
        ) {
            Icon(
                imageVector = if (isPlaying) Icons.Default.Pause else Icons.Default.PlayArrow,
                contentDescription = "Play/Pause",
                tint = Color(0xFF030718),
                modifier = Modifier.size(48.dp)
            )
        }
        IconButton(onClick = onNextClicked) {
            Icon(
                imageVector = Icons.Default.SkipNext,
                contentDescription = "Next",
                tint = Color.White,
                modifier = Modifier.size(45.dp)
            )
        }
        Spacer(modifier = Modifier.width(3.dp))
        IconButton(onClick = onRepeatClicked) {
            Icon(
                imageVector = Icons.Default.Repeat,
                contentDescription = "Repeat",
                tint = if (isRepeating) Color.Magenta else Color.White,
                modifier = Modifier.size(32.dp)
            )
        }
    }
}

@Composable
fun TrackSlider(
    value: Float,
    onValueChange: (newValue: Float) -> Unit,
    songDuration: Float
) {
    Slider(
        value = value,
        onValueChange = {
            onValueChange(it)
        },
        valueRange = 0f..songDuration,
        colors = SliderDefaults.colors(
            thumbColor = Color.White,
            activeTrackColor = Color.DarkGray,
            inactiveTrackColor = Color.LightGray,
        )
    )
}

@SuppressLint("DefaultLocale")
fun formatTime(timeMs: Float): String {
    val totalSeconds = (timeMs / 1000).toInt()
    val minutes = totalSeconds / 60
    val seconds = totalSeconds % 60
    return String.format("%02d:%02d", minutes, seconds)
}

@androidx.annotation.OptIn(UnstableApi::class)
fun getTrackIndex(
    trackId: String,
    playlistTracks: List<TrackItem>,
    artistTracks: List<Track>,
    fromArtist: String
): Int {
    Log.d("MusicPlayerScreen", fromArtist)
    return if (fromArtist == "true") {
        artistTracks.indexOfFirst { it.id == trackId }.takeIf { it != -1 } ?: 0
    } else {
        playlistTracks.indexOfFirst { it.track.id == trackId }.takeIf { it != -1 } ?: 0
    }
}

@androidx.annotation.OptIn(UnstableApi::class)
fun getTrack(
    index: Int,
    playlistTracks: List<TrackItem>,
    artistTracks: List<Track>,
    fromArtist: String
): Track? {
    Log.d("MusicPlayerScreen", fromArtist)
    return if (fromArtist == "true") {
        artistTracks.getOrNull(index)
    } else {
        playlistTracks.getOrNull(index)?.track
    }
}

@androidx.annotation.OptIn(UnstableApi::class)
fun getNextTrackIndex(
    currentIndex: Int,
    playlistTracks: List<TrackItem>,
    artistTracks: List<Track>,
    fromArtist: String
): Int {
    Log.d("MusicPlayerScreen", fromArtist)
    return if (fromArtist == "true") {
        (currentIndex + 1) % artistTracks.size
    } else {
        (currentIndex + 1) % playlistTracks.size
    }
}

@androidx.annotation.OptIn(UnstableApi::class)
fun getPreviousTrackIndex(
    currentIndex: Int,
    playlistTracks: List<TrackItem>,
    artistTracks: List<Track>,
    fromArtist: String
): Int {
    Log.d("MusicPlayerScreen", fromArtist)
    return if (fromArtist == "true") {
        (currentIndex - 1 + artistTracks.size) % artistTracks.size
    } else {
        (currentIndex - 1 + playlistTracks.size) % playlistTracks.size
    }
}

fun getRandomTrackIndex(
    currentIndex: Int,
    playlistTracks: List<TrackItem>,
    artistTracks: List<Track>,
    fromArtist: String
): Int {
    return if (fromArtist == "true") {
        artistTracks.indices.filter { it != currentIndex }.random()
    } else {
        playlistTracks.indices.filter { it != currentIndex }.random()
    }
}

fun updatePlayer(player: ExoPlayer, previewUrl: String?): Float {
    previewUrl?.let {
        val mediaItem = MediaItem.fromUri(Uri.parse(previewUrl))
        player.setMediaItem(mediaItem)
        player.prepare()
        player.play()
    }
    return 0f
}
