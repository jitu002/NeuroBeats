package com.neurobeat.neurobeats.artist.view

import android.util.Log
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.basicMarquee
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import coil.compose.rememberAsyncImagePainter
import com.neurobeat.neurobeats.artist.viewmodel.ArtistLibraryViewModel
import com.neurobeat.neurobeats.artist.viewmodel.ArtistViewModel
import com.neurobeat.neurobeats.music.view.TrackItemView
import com.neurobeat.neurobeats.ui.theme.BackgroundColor

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun ArtistLibraryScreen(
    navController: NavHostController,
    artistLibraryViewModel: ArtistLibraryViewModel,
    artistViewModel: ArtistViewModel,
    artistId: String,
    accessToken: String
) {
    LaunchedEffect(artistId, accessToken) {
        artistLibraryViewModel.fetchArtistTracks(accessToken, artistId)
        artistViewModel.fetchArtistDetails(accessToken, artistId)
    }

    val tracks by artistLibraryViewModel.tracks.observeAsState(emptyList())
    val artist by artistViewModel.artistDetails.observeAsState()

    val genres = artist?.genres?.joinToString(",") { it } ?: "Not Available"
    val fromArtist = true.toString()
    val albumId = "dummy"

    Column (
        modifier = Modifier
            .fillMaxSize()
            .background(BackgroundColor),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        LazyColumn(
            modifier = Modifier.fillMaxSize()
        ) {
            item {
                Box(modifier = Modifier
                    .fillMaxWidth()
                    .height(400.dp)
                ) {
                    val firstTrack = tracks.firstOrNull()
                    val firstTrackId = firstTrack?.id ?: ""
                    val firstArtistsId = firstTrack?.artists?.joinToString(",") { it.id } ?: ""

                    Image(
                        painter = rememberAsyncImagePainter(model = artist?.images?.first()?.url),
                        contentDescription = "Playlist image",
                        contentScale = ContentScale.Crop,
                        modifier = Modifier.fillMaxSize()
                    )
                    artist?.name?.let {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .align(Alignment.BottomStart)
                                .background(Color.Black.copy(alpha = 0.6f))
                                .padding(horizontal = 12.dp)
                        ) {
                            Text(
                                text = it,
                                color = Color.White,
                                fontSize = 34.sp,
                                fontWeight = FontWeight.Bold,
                                overflow = TextOverflow.Ellipsis,
                                modifier = Modifier
                                    .padding(12.dp)
                                    .width(270.dp)
                                    .basicMarquee(Int.MAX_VALUE)
                            )
                            IconButton(
                                onClick = { navController.navigate("MusicPlayer/$accessToken/$firstTrackId/$albumId/$firstArtistsId/$fromArtist") },
                                modifier = Modifier
                                    .size(70.dp)
                                    .offset(y = 28.dp)
                                    .clip(CircleShape)
                                    .background(Color.White)
                                    .padding(8.dp)
                            ) {
                                Icon(
                                    imageVector = Icons.Default.PlayArrow,
                                    contentDescription = "Play/Pause",
                                    tint = Color(0xFF030718),
                                    modifier = Modifier.size(48.dp)
                                )
                            }
                        }
                    }
                }
                Spacer(modifier = Modifier.height(26.dp))

                artist?.followers?.total?.let {
                    Text(
                        text = "Followers: $it",
                        color = Color(0xFF7D92FF),
                        fontSize = 16.sp,
                        modifier = Modifier.padding(15.dp, 0.dp)
                    )
                }
                if (genres != "") {
                    Row (modifier = Modifier.padding(15.dp, 0.dp)) {
                        Text(text = "Genres: ",
                            color = Color(0xFF7D92FF),
                            fontSize = 16.sp
                        )
                        Text(
                            text = genres,
                            color = Color(0xFF7D92FF),
                            fontSize = 16.sp,
                            modifier = Modifier
                                .basicMarquee(Int.MAX_VALUE)
                        )
                    }
                }
                Spacer(modifier = Modifier.height(15.dp))
                Text(
                    text = "Popular Tracks",
                    color = Color(0xFF3051FF),
                    fontSize = 28.sp,
                    fontWeight = FontWeight.SemiBold,
                    modifier = Modifier.padding(15.dp, 0.dp)
                )
                Spacer(modifier = Modifier.height(8.dp))
            }

            items(tracks) { trackItem ->
                TrackItemView(track = trackItem) {
                    val artistsIds = trackItem.artists.joinToString(",") { it.id }
                    val trackId = trackItem.id
                    Log.d("TrackItem", artistsIds)
                    Log.d("TrackItem", accessToken)
                    Log.d("TrackItem", trackId)

                    navController.navigate("MusicPlayer/$accessToken/$trackId/$albumId/$artistsIds/$fromArtist")
                    Log.d("TrackItem", trackItem.album.images.first().url)
                }
            }
        }
    }
}

