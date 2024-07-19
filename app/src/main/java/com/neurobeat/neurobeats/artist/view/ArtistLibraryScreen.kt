package com.neurobeat.neurobeats.artist.view

import android.util.Log
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.basicMarquee
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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.OutlinedCard
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.blur
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Paint
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import coil.compose.rememberAsyncImagePainter
import com.neurobeat.neurobeats.api.models.TopTrack
import com.neurobeat.neurobeats.api.models.Track
import com.neurobeat.neurobeats.artist.viewmodel.ArtistLibraryViewModel
import com.neurobeat.neurobeats.artist.viewmodel.ArtistViewModel
import com.neurobeat.neurobeats.music.view.TrackItemView
import com.neurobeat.neurobeats.music.viewmodels.PlaylistViewModel
import com.neurobeat.neurobeats.ui.theme.BackgroundColor
import com.neurobeat.neurobeats.ui.theme.CustomColor
import com.neurobeat.neurobeats.ui.theme.txtColor
import java.net.URLDecoder
import java.net.URLEncoder
import java.nio.charset.StandardCharsets

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
                    Image(
                        painter = rememberAsyncImagePainter(model = artist?.images?.first()?.url),
                        contentDescription = "Playlist image",
                        contentScale = ContentScale.Crop,
                        modifier = Modifier.fillMaxSize()
                    )
                    artist?.name?.let {
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .align(Alignment.BottomStart)
                                .background(Color.Black.copy(alpha = 0.6f))
                                .padding(horizontal = 20.dp)
                        ) {
                            Text(
                                text = it,
                                color = Color.White,
                                fontSize = 34.sp,
                                fontWeight = FontWeight.Bold,
                                overflow = TextOverflow.Ellipsis,
                                modifier = Modifier.padding(12.dp, 5.dp).basicMarquee()
                            )
                        }
                    }
                }
                Spacer(modifier = Modifier.height(15.dp))
                artist?.followers?.total?.let {
                    Text(text = "Followers: $it", color = Color(0xFF7D92FF), fontSize = 16.sp, modifier = Modifier.padding(15.dp, 0.dp))
                }
                if (genres != "") {
                    Text(
                        text = "Genres: $genres",
                        color = Color(0xFF7D92FF),
                        fontSize = 16.sp,
                        modifier = Modifier.padding(15.dp, 0.dp)
                    )
                }
                Spacer(modifier = Modifier.height(15.dp))

                Text(text = "Popular Tracks", color = Color(0xFF3051FF), fontSize = 28.sp, fontWeight = FontWeight.SemiBold, modifier = Modifier.padding(15.dp, 0.dp))

                Spacer(modifier = Modifier.height(8.dp))
            }

            items(tracks) { trackItem ->
                TrackItemView(track = trackItem) {
                    val previewUrl = trackItem.preview_url ?: return@TrackItemView
                    val encodedPreviewUrl = URLEncoder.encode(previewUrl, StandardCharsets.UTF_8.toString())
                    val encodedTrackName = URLEncoder.encode(trackItem.name, StandardCharsets.UTF_8.toString())
                    val encodedAlbumName = URLEncoder.encode(trackItem.album.name, StandardCharsets.UTF_8.toString())
                    val albumImageUrl = trackItem.album.images.first().url
                    val encodedAlbumImageUrl = URLEncoder.encode(albumImageUrl, StandardCharsets.UTF_8.toString())
                    val artists = trackItem.artists.joinToString(", ") { it.name }
                    val encodedArtists = URLEncoder.encode(artists, StandardCharsets.UTF_8.toString())
                    val artistsIds = trackItem.artists.joinToString(",") { it.id }
                    val encodedArtistsIds = URLEncoder.encode(artistsIds, StandardCharsets.UTF_8.toString())
                    val duration = trackItem.duration_ms.toString()
                    val encodedDuration = URLEncoder.encode(duration, StandardCharsets.UTF_8.toString())

                    navController.navigate("MusicPlayer/$accessToken/$encodedPreviewUrl/$encodedTrackName/$encodedAlbumName/$encodedAlbumImageUrl/$encodedArtists/$encodedDuration/$encodedArtistsIds")
                    Log.d("TrackItem", trackItem.album.images.first().url)
                }
            }
        }
    }
}

@Composable
fun TrackItemView(track: TopTrack, onClick: () -> Unit) {
    OutlinedCard(
        modifier = Modifier
            .padding(10.dp)
            .fillMaxWidth()
            .clickable { onClick() },
        shape = RoundedCornerShape(8.dp),
        colors = CardDefaults.outlinedCardColors(Color.Transparent),
        elevation = CardDefaults.cardElevation(0.dp)
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
                Text(text = track.name, fontSize = 16.sp, color = txtColor)
                Text(text = "By " + track.artists.joinToString(", ") { it.name }, fontSize = 14.sp, color = txtColor)
            }
        }
    }

}