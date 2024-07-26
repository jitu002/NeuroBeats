package com.neurobeat.neurobeats

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.neurobeat.neurobeats.artist.view.ArtistLibraryScreen
import com.neurobeat.neurobeats.artist.viewmodel.AllArtistsViewModel
import com.neurobeat.neurobeats.artist.viewmodel.ArtistLibraryViewModel
import com.neurobeat.neurobeats.artist.viewmodel.ArtistViewModel
import com.neurobeat.neurobeats.authentication.view.LoginScreen
import com.neurobeat.neurobeats.authentication.view.SignupScreen
import com.neurobeat.neurobeats.music.view.MusicPlayerScreen
import com.neurobeat.neurobeats.music.view.TracksScreen
import com.neurobeat.neurobeats.music.viewmodels.PlaylistViewModel
import com.neurobeat.neurobeats.pages.view.HomePage
import com.neurobeat.neurobeats.pages.view.Profile
import com.neurobeat.neurobeats.ui.theme.NeuroBeatsTheme

class MainActivity : ComponentActivity() {
    private val playlistViewModel: PlaylistViewModel by viewModels()
    private val artistLibraryViewModel: ArtistLibraryViewModel by viewModels()
    private val artistViewModel: ArtistViewModel by viewModels()
    private val allArtistViewModel: AllArtistsViewModel by viewModels()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            NeuroBeatsTheme {
                AppNavigation(playlistViewModel, artistLibraryViewModel, artistViewModel, allArtistViewModel)
            }
        }
    }
    override fun onNewIntent(intent: Intent) {
        super.onNewIntent(intent)
        intent.data?.let { uri ->
            handleRedirectUri(uri)
        }
    }

    private fun handleRedirectUri(uri: Uri) {
        // Extract the authorization code or token from the URI
        uri.getQueryParameter("code")
    }
}

@Composable
fun AppNavigation(
    playlistViewModel: PlaylistViewModel,
    artistLibraryViewModel: ArtistLibraryViewModel,
    artistViewModel: ArtistViewModel,
    allArtistViewModel: AllArtistsViewModel
) {
    val navController = rememberNavController()

    NavHost(navController = navController, startDestination = "LoginScreen") {
        composable("LoginScreen") { LoginScreen(navController)}
        composable("SignupScreen") { SignupScreen(navController)}
        composable("Homepage") { HomePage(navController) }
        composable("ArtistLibrary/{artistId}/{token}") {backStackEntry ->
            ArtistLibraryScreen(
                navController = navController,
                artistLibraryViewModel = artistLibraryViewModel,
                artistViewModel = artistViewModel,
                artistId = backStackEntry.arguments?.getString("artistId") ?: "",
                accessToken = backStackEntry.arguments?.getString("token") ?: ""
            )
        }
        composable("MusicPlayer/{token}/{trackId}/{artistsIds}/{fromArtist}") { backStackEntry ->
            MusicPlayerScreen(
                navController = navController,
                viewModel = allArtistViewModel,
                playlistViewModel = playlistViewModel,
                artistLibraryViewModel = artistLibraryViewModel,
                accessToken = backStackEntry.arguments?.getString("token") ?: "",
                trackId = backStackEntry.arguments?.getString("trackId") ?: "",
                artistIds = backStackEntry.arguments?.getString("artistsIds") ?: "",
                fromArtist = backStackEntry.arguments?.getString("fromArtist") ?: ""
            )
        }
        composable("TracksScreen/{playlistId}/{token}/{playlistImage}") {backStackEntry ->
            TracksScreen(
                navController = navController,
                viewModel = playlistViewModel,
                playlistId = backStackEntry.arguments?.getString("playlistId") ?: "",
                accessToken = backStackEntry.arguments?.getString("token") ?: "",
                playlistImage = backStackEntry.arguments?.getString("playlistImage") ?: ""
            )
        }
        composable("Profile"){ Profile(navController) }
    }
}