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
import com.neurobeat.neurobeats.authentication.view.LoginScreen
import com.neurobeat.neurobeats.ui.theme.NeuroBeatsTheme
import com.neurobeat.neurobeats.authentication.view.SignupScreen
import com.neurobeat.neurobeats.artist.view.ArtistLibraryScreen
import com.neurobeat.neurobeats.music.view.MusicPlayerScreen
import com.neurobeat.neurobeats.music.view.TracksScreen
import com.neurobeat.neurobeats.music.viewmodels.PlaylistViewModel
import com.neurobeat.neurobeats.pages.HomePage
import com.neurobeat.neurobeats.pages.Profile
import com.neurobeat.neurobeats.pages.Search

class MainActivity : ComponentActivity() {
    private val viewModel: PlaylistViewModel by viewModels()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            NeuroBeatsTheme {
                AppNavigation(viewModel)
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
        val code = uri.getQueryParameter("code")
    }
}

@Composable
fun AppNavigation(
    playlistViewModel: PlaylistViewModel
) {
    val navController = rememberNavController()

    NavHost(navController = navController, startDestination = "LoginScreen") {
        composable("LoginScreen") { LoginScreen(navController) }
        composable("SignupScreen") { SignupScreen(navController) }
        composable("Homepage") { HomePage(navController) }
        composable("ArtistLibrary") { ArtistLibraryScreen(navController) }
        composable("MusicPlayer/{preview_url}/{trackName}/{albumName}/{albumImage}/{artists}/{duration_ms}") { backStackEntry ->
            MusicPlayerScreen(
                navController = navController,
                preview_url = backStackEntry.arguments?.getString("preview_url") ?: "",
                trackName = backStackEntry.arguments?.getString("trackName") ?: "",
                albumName = backStackEntry.arguments?.getString("albumName") ?: "",
                albumImage = backStackEntry.arguments?.getString("albumImage") ?: "",
                artists = backStackEntry.arguments?.getString("artists") ?: "",
                duration_ms = backStackEntry.arguments?.getString("duration_ms") ?: ""
            )
        }
        composable("TracksScreen/{playlistId}/{token}/{playlistImage}") { backStackEntry ->
            TracksScreen(
                navController = navController,
                viewModel = playlistViewModel,
                playlistId = backStackEntry.arguments?.getString("playlistId") ?: "",
                accessToken = backStackEntry.arguments?.getString("token") ?: "",
                playlistImage = backStackEntry.arguments?.getString("playlistImage") ?: ""
            )
        }
        composable("Profile") { Profile(navController) }
        composable("Search") { Search(navController) }
    }
}