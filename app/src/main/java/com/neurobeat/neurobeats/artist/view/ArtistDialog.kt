package com.neurobeat.neurobeats.artist.view

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.navigation.NavController
import com.neurobeat.neurobeats.artist.viewmodel.AllArtistsViewModel

@Composable
fun ArtistsDialog(
    viewModel: AllArtistsViewModel,
    accessToken: String,
    artistIds: String,
    onDismissRequest: () -> Unit,
    navController: NavController
) {
    // Fetch artists data when the dialog is shown
    LaunchedEffect(artistIds, accessToken) {
        viewModel.fetchArtists(accessToken, artistIds)
    }

    val artists by viewModel.artists.observeAsState(emptyList())

    Log.d("ArtistsDialog", artists.toString())

    Dialog(onDismissRequest = onDismissRequest) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .background(Color.White)
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(text = "Artists", fontSize = 20.sp, color = Color.Black)
            Spacer(modifier = Modifier.height(16.dp))
            LazyColumn {
                items(artists) { artist ->
                    ArtistItemView(artist = artist) {
                        navController.navigate("ArtistLibrary/${artist.id}/$accessToken")
                    }
                }
            }
            Spacer(modifier = Modifier.height(16.dp))
            Button(onClick = onDismissRequest) {
                Text("Close")
            }
        }
    }
}
