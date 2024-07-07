package com.neurobeat.neurobeats

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
import com.neurobeat.neurobeats.Pages.HomePage

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
}

@Composable
fun AppNavigation() {
    val navController = rememberNavController()

    NavHost(navController = navController, startDestination = "LoginScreen") {
        composable("LoginScreen") { LoginScreen(navController)}
        composable("SignupScreen") { SignupScreen(navController)}
        composable("Homepage") { HomePage(navController) }
    }
}