package com.neurobeat.neurobeats.music.view

import android.net.Uri
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Slider
import androidx.compose.material3.SliderDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.media3.common.MediaItem
import androidx.media3.exoplayer.ExoPlayer
import androidx.navigation.NavController
import com.neurobeat.neurobeats.R
import com.neurobeat.neurobeats.ui.theme.BackgroundColor
import com.neurobeat.neurobeats.ui.theme.txtColor
import kotlinx.coroutines.delay

@Composable
fun MusicPlayerScreen(navController: NavController, previewUrl: String?) {
    val context = LocalContext.current

    // Remember ExoPlayer instance
    val player = remember {
        ExoPlayer.Builder(context).build().apply {
            val mediaItem = MediaItem.fromUri(Uri.parse(previewUrl))
            setMediaItem(mediaItem)
            prepare()
        }
    }

    var isPlaying by remember { mutableStateOf(false) }
    var currentPosition by remember { mutableFloatStateOf(0f) }
    val totalDuration by remember { mutableFloatStateOf(player.duration.toFloat()) }

    // Manage ExoPlayer lifecycle
    DisposableEffect(Unit) {
        onDispose {
            player.release()
        }
    }

    // Update current position
    LaunchedEffect(player, isPlaying) {
        while (true) {
            if (isPlaying) {
                currentPosition = player.currentPosition.toFloat()
            }
            delay(1000L)
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(BackgroundColor)
            .padding(10.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Row {
            Icon(imageVector = Icons.Default.ChevronLeft, contentDescription = "Left Arrow", modifier = Modifier.size(40.dp), tint = txtColor)
            Column(
                modifier = Modifier.width(280.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(text = "Playing from the album", color = txtColor, fontSize = 16.sp)
                Text(text = "Dangerous Days", color = txtColor, fontSize = 14.sp)
            }
            Icon(imageVector = Icons.Default.MoreVert, contentDescription = "More Options", modifier = Modifier.size(35.dp), tint = txtColor)
        }
        Spacer(modifier = Modifier.height(30.dp))
        Image(
            painter = painterResource(R.drawable.prison_perturbator),
            contentDescription = "Song Image",
            contentScale = ContentScale.Crop,
            modifier = Modifier
                .size(320.dp)
                .clip(RoundedCornerShape(20.dp))
        )
        Spacer(modifier = Modifier.height(100.dp))
        Column(
            modifier = Modifier.padding(horizontal = 30.dp)
        ) {
            Text(text = "BODY/PRISON", color = txtColor, modifier = Modifier.fillMaxWidth())
            Text(text = "Pertrubator", color = txtColor, fontSize = 16.sp)
            Spacer(modifier = Modifier.height(16.dp))

            TrackSlider(
                value = currentPosition,
                onValueChange = {
                    player.seekTo(it.toLong())
                    currentPosition = it
                },
                songDuration = totalDuration
            )
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = formatTime(currentPosition),
                    color = txtColor,
                    fontSize = 16.sp,
                    modifier = Modifier.padding(horizontal = 5.dp)
                )
                Text(
                    text = formatTime(totalDuration),
                    color = txtColor,
                    fontSize = 16.sp,
                    modifier = Modifier.padding(horizontal = 5.dp)
                )
            }

            PlaybackControls(
                isPlaying = isPlaying,
                onPlayPauseClicked = {
                    if (isPlaying) {
                        player.pause()
                    } else {
                        player.play()
                    }
                    isPlaying = !isPlaying
                },
                onNextClicked = {},
                onPreviousClicked = {}
            )
        }
    }
}

@Composable
fun PlaybackControls(
    isPlaying: Boolean,
    onPlayPauseClicked: () -> Unit,
    onNextClicked: () -> Unit,
    onPreviousClicked: () -> Unit
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceEvenly,
        modifier = Modifier.fillMaxWidth()
    ) {
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
                .background(Color.DarkGray)
                .padding(8.dp)
        ) {
            Icon(
                imageVector = if (isPlaying) Icons.Default.Pause else Icons.Default.PlayArrow,
                contentDescription = "Play/Pause",
                tint = Color.White,
                modifier = Modifier.size(48.dp)  // Adjust icon size if needed
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

fun formatTime(timeMs: Float): String {
    val totalSeconds = (timeMs / 1000).toInt()
    val minutes = totalSeconds / 60
    val seconds = totalSeconds % 60
    return String.format("%02d:%02d", minutes, seconds)
}
