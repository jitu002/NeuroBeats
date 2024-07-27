package com.neurobeat.neurobeats.music.view

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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ChevronLeft
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.Icon
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
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.media3.common.MediaItem
import androidx.media3.common.Player
import androidx.media3.common.util.UnstableApi
import androidx.media3.exoplayer.ExoPlayer
import androidx.navigation.NavController
import coil.compose.rememberAsyncImagePainter
import com.neurobeat.neurobeats.artist.view.ArtistsDialog
import com.neurobeat.neurobeats.artist.viewmodel.AllArtistsViewModel
import com.neurobeat.neurobeats.artist.viewmodel.ArtistLibraryViewModel
import com.neurobeat.neurobeats.music.model.PlaybackControls
import com.neurobeat.neurobeats.music.model.TrackSlider
import com.neurobeat.neurobeats.music.model.formatTime
import com.neurobeat.neurobeats.music.model.getNextTrackIndex
import com.neurobeat.neurobeats.music.model.getPreviousTrackIndex
import com.neurobeat.neurobeats.music.model.getRandomTrackIndex
import com.neurobeat.neurobeats.music.model.getTrack
import com.neurobeat.neurobeats.music.model.getTrackIndex
import com.neurobeat.neurobeats.music.model.updatePlayer
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
    val playlistTracks by playlistViewModel.tracks.observeAsState()
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
                    currentTrackIndex = if (isShuffling) ({
                        getRandomTrackIndex(currentTrackIndex, playlistTracks, artistTracks, fromArtist)
                    }) as Int else {
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
                    player.repeatMode = Player.REPEAT_MODE_OFF
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