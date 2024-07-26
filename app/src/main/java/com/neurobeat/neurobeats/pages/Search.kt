package com.neurobeat.neurobeats.pages

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.staggeredgrid.LazyVerticalStaggeredGrid
import androidx.compose.foundation.lazy.staggeredgrid.StaggeredGridCells
import androidx.compose.foundation.lazy.staggeredgrid.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ChevronLeft
import androidx.compose.material.icons.filled.ChevronRight
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.BottomAppBarDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.constraintlayout.compose.DesignElements.map
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.google.gson.Gson
import com.neurobeat.neurobeats.authentication.SpotifyAuth
import com.neurobeat.neurobeats.music.view.TrackItemView
import com.neurobeat.neurobeats.ui.theme.BackgroundColor
import com.neurobeat.neurobeats.ui.theme.BarColor
import com.neurobeat.neurobeats.ui.theme.txtColor
import kotlinx.coroutines.launch
import java.net.URLEncoder
import java.nio.charset.StandardCharsets

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Search(navController: NavController) {

    val bottomScrollBehavior = BottomAppBarDefaults.exitAlwaysScrollBehavior()

    val categories = listOf("Pop", "Hindi", "Ghazals", "Indie", "Devotional", "Party", "Love", "Trending")
    val genreColor=listOf(
        Color(0xFF00FFFF),
        Color(0xFFFF69B4),
        Color(0xFF32CD32),
        Color(0xFFFFFF00),
        Color(0xFF8A2BE2),
        Color(0xFFFFA500),
        Color(0xFFFF00FF),
        Color(0xFF40E0D0),
        Color(0xFFFF7F50),
        Color(0xFF7FFF00)
    )



    val searchViewModel: SearchViewModel = viewModel()
    val searchResults by searchViewModel.searchResults.collectAsState()

    val coroutineScope = rememberCoroutineScope()
    val categoriesViewModel: CategoriesViewModel = viewModel()
    val categoryPlaylistsMap by categoriesViewModel.categoryPlaylistsMap.collectAsState()
    val isLoading by categoriesViewModel.isLoading.collectAsState()

    var query by remember { mutableStateOf("") }
    var token by remember { mutableStateOf<String?>(null) }

    fun getGenreColor(genre: String): Color {

        val index = categories.indexOf(genre)
        return if (index != -1) {
            genreColor[index % genreColor.size]
        } else {
            Color.Gray
        }
    }

    LaunchedEffect(Unit) {
        SpotifyAuth.getAccessToken { accessToken ->
            token = accessToken
            token?.let {
                coroutineScope.launch {
                    categoriesViewModel.category(it)
                }
            }
        }
    }
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(BackgroundColor)
            .padding(vertical = 50.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        TextButton(
            onClick = {
            navController.navigate("Homepage")
            }
            ) {
            Row(
                modifier = Modifier.fillMaxWidth(),

                horizontalArrangement = Arrangement.Start,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(imageVector = Icons.Default.ChevronLeft, contentDescription = "Right Arrow", modifier = Modifier.size(50.dp,50.dp))
                Text(text = "Homepage")
            }
        }
        TextField(
            value = query,
            onValueChange = { query = it },
            maxLines = 1,
            singleLine = true,
            label = { Text("Tune in....") },
            modifier = Modifier
                .padding(vertical = 50.dp)
                .border(1.dp, txtColor, CircleShape)
                .width(370.dp),
            colors = TextFieldDefaults.colors(
                unfocusedContainerColor = Color.Transparent,
                unfocusedIndicatorColor = Color.Transparent,
                focusedIndicatorColor = Color.Transparent,
                focusedContainerColor = Color.Transparent
            ),
            trailingIcon = {
                Icon(
                    imageVector = Icons.Default.Search,
                    contentDescription = "Search icon",
                    modifier = Modifier.clickable {
                        token?.let {
                            searchViewModel.search(query, it)
                        }
                    }
                )
            }
        )

        if (query.isNotEmpty()) {
            LazyColumn {
                items(searchResults) { trackItem ->
                    TrackItemView(track = trackItem) {
                        val previewUrl = trackItem.preview_url ?: return@TrackItemView
                        val encodedPreviewUrl = URLEncoder.encode(previewUrl, StandardCharsets.UTF_8.toString())
                        val encodedTrackName = URLEncoder.encode(trackItem.name, StandardCharsets.UTF_8.toString())
                        val encodedAlbumName = URLEncoder.encode(trackItem.album.name, StandardCharsets.UTF_8.toString())
                        val albumImageUrl = trackItem.album.images.first().url
                        val encodedAlbumImageUrl = URLEncoder.encode(albumImageUrl, StandardCharsets.UTF_8.toString())
                        val artists = trackItem.artists.joinToString(", ") { it.name }
                        val encodedArtists = URLEncoder.encode(artists, StandardCharsets.UTF_8.toString())
                        val duration = trackItem.duration_ms.toString()
                        val encodedDuration = URLEncoder.encode(duration, StandardCharsets.UTF_8.toString())

                        navController.navigate("MusicPlayer/$encodedPreviewUrl/$encodedTrackName/$encodedAlbumName/$encodedAlbumImageUrl/$encodedArtists/$encodedDuration")
                    }
                }
            }
        } else {
            if(isLoading)
            {
                LazyVerticalStaggeredGrid(
                    columns = StaggeredGridCells.Fixed(2),
                    modifier = Modifier.fillMaxSize(),
                    contentPadding = PaddingValues(8.dp)
                ) {
                    categoryPlaylistsMap?.forEach { (category, playlists) ->
                        if (categories.contains(category.name)) {
                            item {
                                Column(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalAlignment = Alignment.Start
                                ) {
                                    LazyRow(
                                        contentPadding = PaddingValues(start = 16.dp, end = 16.dp),
                                        horizontalArrangement = Arrangement.spacedBy(24.dp),
                                        modifier = Modifier
                                            .fillMaxSize()
                                            .height(200.dp)
                                    ) {
                                        items(playlists) { playlist ->
                                            Card(
                                                colors = CardDefaults.cardColors(getGenreColor(category.name)),
                                                modifier = Modifier
                                                    .padding(8.dp) // Optional: add padding around the card
                                                    .fillMaxHeight()
                                            ) {
                                                PlaylistItem(playlist, navController, token)
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
            else {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center,
                    modifier = Modifier.fillMaxSize()
                ) {
                    Text("Curating some finness!!!")
                    CircularProgressIndicator(
                        modifier = Modifier.width(64.dp),
                        color = Color.White,
                        trackColor = MaterialTheme.colorScheme.surfaceVariant,
                    )
                }
            }
        }
    }
}
