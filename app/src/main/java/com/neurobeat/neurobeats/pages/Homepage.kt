package com.neurobeat.neurobeats.pages

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.neurobeat.neurobeats.authentication.viewmodel.AuthenticationState
import com.neurobeat.neurobeats.authentication.viewmodel.AuthenticationViewModel
import com.neurobeat.neurobeats.ui.theme.BackgroundColor
import com.neurobeat.neurobeats.ui.theme.txtColor

@Composable
fun HomePage(navController: NavController) {

    val authenticationViewModel: AuthenticationViewModel= viewModel()
    val authState=authenticationViewModel.authState.observeAsState()

    LaunchedEffect(authState.value) {
        when(authState.value){
            is AuthenticationState.NotAuthenticated -> navController.navigate("LoginScreen")
            else -> Unit
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(BackgroundColor),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,

    ) {
        Text(text = "Home Page", fontSize = 32.sp, color = txtColor)
        Column(

            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "Hold Your",
                style = MaterialTheme.typography.titleLarge,
                textAlign = TextAlign.Center,
                color = txtColor,
                fontSize = 35.sp
            )
            Text(
                text = "BEATS",
                style = MaterialTheme.typography.titleLarge,
                textAlign = TextAlign.Center,
                color = txtColor,
                fontSize = 50.sp
            )
            Text(
                text = "We are under construction!!!",
                style = MaterialTheme.typography.titleLarge,
                textAlign = TextAlign.Center,
                color = txtColor,
                fontSize = 35.sp
            )

        }
        TextButton(onClick = {
            authenticationViewModel.signOut()
        }) {
            Text(text = "Sign out")
        }
    }

}