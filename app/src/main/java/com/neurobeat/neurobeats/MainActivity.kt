package com.neurobeat.neurobeats

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.neurobeat.neurobeats.authentication.view.LoginScreen
import com.neurobeat.neurobeats.ui.theme.NeuroBeatsTheme
import com.neurobeat.neurobeats.authentication.view.SignupScreen
import com.neurobeat.neurobeats.artist.view.ArtistLibraryScreen
import com.neurobeat.neurobeats.music.view.MusicPlayerScreen
import com.neurobeat.neurobeats.pages.HomePage

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            NeuroBeatsTheme {
                AppNavigation()
            }
        }
    }
    override fun onNewIntent(intent: Intent) {
        super.onNewIntent(intent)
        intent?.data?.let { uri ->
            handleRedirectUri(uri)
        }
    }

    private fun handleRedirectUri(uri: Uri) {
        // Extract the authorization code or token from the URI
        val code = uri.getQueryParameter("code")
        if (code != null) {
            // Use the authorization code to get the access token
        }
    }
}

@Composable
fun AppNavigation() {
    val navController = rememberNavController()

    NavHost(navController = navController, startDestination = "LoginScreen") {
        composable("LoginScreen") { LoginScreen(navController)}
        composable("SignupScreen") { SignupScreen(navController)}
        composable("Homepage") { HomePage(navController) }
        composable("ArtistLibrary") { ArtistLibraryScreen(navController) }
        composable("MusicPlayer") { MusicPlayerScreen(navController) }
    }
}