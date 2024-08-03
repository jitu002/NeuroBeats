package com.neurobeat.neurobeats.pages.view

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.LibraryMusic
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.BottomAppBarDefaults
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberTopAppBarState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.neurobeat.neurobeats.DatabaseOperation
import com.neurobeat.neurobeats.api.models.Track
import com.neurobeat.neurobeats.music.view.TrackItemView
import com.neurobeat.neurobeats.pages.viewmodel.LibraryViewModel
import com.neurobeat.neurobeats.ui.theme.BackgroundColor
import com.neurobeat.neurobeats.ui.theme.BarColor
import com.neurobeat.neurobeats.ui.theme.txtColor


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Library(navController: NavController, accessToken: String){

    val topScrollBehavior= TopAppBarDefaults.enterAlwaysScrollBehavior(rememberTopAppBarState())
    val bottomScrollBehavior= BottomAppBarDefaults.exitAlwaysScrollBehavior()

    val dbOps=DatabaseOperation()
    val libraryViewModel: LibraryViewModel = viewModel()
    val fromArtist = "search"

    LaunchedEffect(Unit) {
        dbOps.fetchTracksFromFavorites(
            FirebaseAuth.getInstance(),
            FirebaseFirestore.getInstance()
        ) { likedTracks ->
            if (likedTracks != null) {

                likedTracks.forEach { trackId ->
//                    println("Library token:$accessToken")
//                    println("Track:$trackId")
                    libraryViewModel.getTrack(accessToken, trackId)
//                    println("Step 2 exec:$tracks")
                }
            } else {
                println("Failed to fetch favorite tracks")
            }
        }
    }
    val tracks by libraryViewModel.favTracks.collectAsState()
    println("tracks: $tracks")

    Scaffold(
        modifier = Modifier
            .nestedScroll(topScrollBehavior.nestedScrollConnection)
            .nestedScroll(bottomScrollBehavior.nestedScrollConnection),

        topBar = {
            CenterAlignedTopAppBar(
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = BarColor,
                    titleContentColor = txtColor,
                ),
                title = {
                    Text(
                        "Favorites",
                        maxLines = 3,
                        fontSize =27.sp,
                        overflow = TextOverflow.Ellipsis
                    )
                },

                scrollBehavior = topScrollBehavior,
            )
        },
        bottomBar = {
            BottomAppBar(
                containerColor = BarColor,
                contentColor = txtColor,
                scrollBehavior = bottomScrollBehavior
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceEvenly
                ) {
                    IconButton(onClick = { navController.navigate("Homepage") }) {
                        Icon(imageVector = Icons.Default.Home, contentDescription ="Home icon" )
                    }
                    IconButton(onClick = { navController.navigate("Search") }) {
                        Icon(imageVector = Icons.Default.Search, contentDescription = "Search icon")
                    }
                    IconButton(onClick = { navController.navigate("Library/$accessToken") },colors= IconButtonDefaults.iconButtonColors(Color.Magenta)){
                        Icon(imageVector = Icons.Default.LibraryMusic, contentDescription = "Library music")
                    }
                }
            }
        }
    ) { innerPadding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .background(BackgroundColor),
            contentPadding = PaddingValues(16.dp)
        ) {
            //ui GOES here

            items(tracks.distinct()) { trackItem ->
                TrackItemView(track = trackItem) {
                    val artistsIds = trackItem.artists.joinToString(",") { it.id }
                    val trackId = trackItem.id
                    val albumId = trackItem.album.id

                    navController.navigate("MusicPlayer/$accessToken/$trackId/$albumId/$artistsIds/$fromArtist")
                }
            }

        }
    }

}