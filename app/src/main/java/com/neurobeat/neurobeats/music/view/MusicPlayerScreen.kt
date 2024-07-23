package com.neurobeat.neurobeats.music.view

import android.annotation.SuppressLint
import android.net.Uri
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.basicMarquee
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
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
import androidx.compose.material.icons.filled.ChevronLeft
import androidx.compose.material.icons.filled.MoreVert
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
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.media3.common.MediaItem
import androidx.media3.common.Player
import androidx.media3.common.util.Log
import androidx.media3.common.util.UnstableApi
import androidx.media3.exoplayer.ExoPlayer
import androidx.navigation.NavController
import coil.compose.rememberAsyncImagePainter
import com.neurobeat.neurobeats.api.models.Track
import com.neurobeat.neurobeats.api.models.TrackItem
import com.neurobeat.neurobeats.artist.view.ArtistsDialog
import com.neurobeat.neurobeats.artist.viewmodel.AllArtistsViewModel
import com.neurobeat.neurobeats.artist.viewmodel.ArtistLibraryViewModel
import com.neurobeat.neurobeats.music.viewmodels.PlaylistViewModel
import com.neurobeat.neurobeats.ui.theme.BackgroundColor
import com.neurobeat.neurobeats.ui.theme.txtColor
import kotlinx.coroutines.delay

@androidx.annotation.OptIn(UnstableApi::class)
@OptIn(ExperimentalFoundationApi::class)
@Composable
fun MusicPlayerScreen(
    navController: NavController,
    viewModel: AllArtistsViewModel,
    playlistViewModel: PlaylistViewModel,
    accessToken: String,
    trackId: String,
    artistIds: String,
    artistLibraryViewModel: ArtistLibraryViewModel,
    fromArtist: String
) {
    val decodedDuration = 29000f
    val context = LocalContext.current
    val playlistTracks by playlistViewModel.tracks.observeAsState(emptyList())
    val artistTracks by artistLibraryViewModel.tracks.observeAsState(emptyList())

    var currentTrackIndex by remember { mutableIntStateOf(getTrackIndex(trackId, playlistTracks, artistTracks, fromArtist)) }
    val track = getTrack(currentTrackIndex, playlistTracks, artistTracks, fromArtist)

    val previewUrl = track?.preview_url ?: ""
    val artists = track?.artists?.joinToString(", ") { it.name } ?: ""
    val albumName = track?.album?.name ?: ""
    val albumImage = track?.album?.images?.firstOrNull()?.url ?: ""
    val trackName = track?.name ?: ""

    val player = remember {
        ExoPlayer.Builder(context).build().apply {
            val mediaItem = MediaItem.fromUri(Uri.parse(previewUrl))
            setMediaItem(mediaItem)
            prepare()
            play()
        }
    }

    var isPlaying by remember { mutableStateOf(true) }
    var isRepeating by remember { mutableStateOf(false) }
    var isShuffling by remember { mutableStateOf(false) }
    var currentPosition by remember { mutableFloatStateOf(0f) }

    DisposableEffect(Unit) {
        val listener = object : Player.Listener {
            override fun onPlaybackStateChanged(state: Int) {
                if (state == Player.STATE_ENDED) {
                    currentTrackIndex = if (isShuffling) {
                        getRandomTrackIndex(currentTrackIndex, playlistTracks, artistTracks, fromArtist)
                    } else {
                        getNextTrackIndex(currentTrackIndex, playlistTracks, artistTracks, fromArtist)
                    }
                    currentPosition = updatePlayer(player, getTrack(
                        currentTrackIndex,
                        playlistTracks,
                        artistTracks,
                        fromArtist
                    )?.preview_url)
                    isPlaying = true
                }
            }
        }
        player.addListener(listener)
        onDispose {
            player.release()
        }
    }

    LaunchedEffect(isPlaying) {
        while (isPlaying) {
            currentPosition = player.currentPosition.toFloat()
            delay(1000L)
        }
    }

    var showDialog by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(BackgroundColor)
            .padding(10.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Row {
            Icon(
                imageVector = Icons.Default.ChevronLeft,
                contentDescription = "Left Arrow",
                modifier = Modifier
                    .size(40.dp)
                    .clickable { navController.popBackStack() },
                tint = txtColor
            )
            Column(
                modifier = Modifier.width(280.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(text = "Playing from the album", color = txtColor, fontSize = 16.sp)
                Text(text = albumName, color = txtColor, fontSize = 14.sp, modifier = Modifier.basicMarquee())
            }
            Icon(imageVector = Icons.Default.MoreVert, contentDescription = "More Options", modifier = Modifier.size(35.dp), tint = txtColor)
        }
        Spacer(modifier = Modifier.height(30.dp))
        Image(
            painter = rememberAsyncImagePainter(model = albumImage),
            contentDescription = "Track image",
            contentScale = ContentScale.Crop,
            modifier = Modifier
                .size(320.dp)
                .clip(RoundedCornerShape(20.dp))
        )
        Spacer(modifier = Modifier.height(100.dp))
        Column(
            modifier = Modifier.padding(horizontal = 30.dp)
        ) {
            Text(text = trackName, color = txtColor, modifier = Modifier
                .fillMaxWidth()
                .basicMarquee())
            Text(text = artists, color = txtColor, fontSize = 16.sp,
                modifier = Modifier
                    .clickable { showDialog = true }
                    .basicMarquee()
            )
            Spacer(modifier = Modifier.height(16.dp))

            TrackSlider(
                value = currentPosition,
                onValueChange = {
                    player.seekTo(it.toLong())
                    currentPosition = it
                },
                songDuration = decodedDuration
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
                    text = formatTime(decodedDuration),
                    color = txtColor,
                    fontSize = 16.sp,
                    modifier = Modifier.padding(horizontal = 5.dp)
                )
            }

            PlaybackControls(
                isPlaying = isPlaying,
                isRepeating = isRepeating,
                isShuffling = isShuffling,
                onPlayPauseClicked = {
                    if (isPlaying) {
                        player.pause()
                    } else {
                        player.play()
                    }
                    isPlaying = !isPlaying
                },
                onNextClicked = {
                    currentTrackIndex = getNextTrackIndex(
                        currentTrackIndex,
                        playlistTracks,
                        artistTracks,
                        fromArtist
                    )
                    currentPosition = updatePlayer(player, getTrack(
                        currentTrackIndex,
                        playlistTracks,
                        artistTracks,
                        fromArtist
                    )?.preview_url)
                    isPlaying = true
                    isRepeating = false
                    isShuffling = false
                },
                onPreviousClicked = {
                    currentTrackIndex = getPreviousTrackIndex(currentTrackIndex, playlistTracks, artistTracks, fromArtist)
                    currentPosition = updatePlayer(player, getTrack(
                        currentTrackIndex,
                        playlistTracks,
                        artistTracks,
                        fromArtist
                    )?.preview_url)
                    isPlaying = true
                    isRepeating = false
                    isShuffling = false
                },
                onRepeatClicked = {
                    isRepeating = !isRepeating
                    isShuffling = false
                    player.repeatMode = if (isRepeating) Player.REPEAT_MODE_ONE else Player.REPEAT_MODE_OFF
                },
                onShuffleClicked = {
                    isShuffling = !isShuffling
                    isRepeating = false
                }
            )
        }
    }

    if (showDialog) {
        ArtistsDialog(
            navController = navController,
            viewModel = viewModel,
            accessToken = accessToken,
            artistIds = artistIds,
            onDismissRequest = { showDialog = false }
        )
    }
}

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
