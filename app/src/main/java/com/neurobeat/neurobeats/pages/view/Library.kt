package com.neurobeat.neurobeats.pages.view

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.neurobeat.neurobeats.DatabaseOperation
import com.neurobeat.neurobeats.api.models.Track
import com.neurobeat.neurobeats.music.view.TrackItemView
import com.neurobeat.neurobeats.pages.viewmodel.LibraryViewModel
import com.neurobeat.neurobeats.ui.theme.BackgroundColor


@Composable
fun Library(navController: NavController, accessToken: String){

    val dbOps=DatabaseOperation()
    val coroutineScope= rememberCoroutineScope()
    val libraryViewModel:LibraryViewModel = viewModel()

    val favTracks = remember { mutableStateListOf<Track>() }

    val tracks by libraryViewModel.favTrack.observeAsState()

    val fromArtist = false.toString()
    val albumId = "dummy"

    LaunchedEffect(Unit) {

        dbOps.fetchTracksFromFavorites(FirebaseAuth.getInstance(), FirebaseFirestore.getInstance()) { tracks ->
            if (tracks != null) {

                tracks.forEach { trackId ->
                    println("Library token:$accessToken")
                    println("Track:$trackId")
                    libraryViewModel.getTracks(accessToken,trackId)
                    println("Step 2 exec:$tracks")
                }
            } else {
                println("Failed to fetch favorite tracks")
            }
        }
    }
    LaunchedEffect(tracks) {
        tracks?.let { newTracks ->
            favTracks.addAll(listOf(newTracks))
            println("Library screen fav:$favTracks")
        }
    }

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .background(BackgroundColor),
        contentPadding = PaddingValues(16.dp)
    ) {
        //ui GOES here

        items(favTracks) { trackItem ->
            TrackItemView(track = trackItem) {
                val artistsIds = trackItem.artists.joinToString(",") { it.id }
                val trackId = trackItem.id

                navController.navigate("MusicPlayer/$accessToken/$trackId/$albumId/$artistsIds/$fromArtist")
            }
        }

    }
}