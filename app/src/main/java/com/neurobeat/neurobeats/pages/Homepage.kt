package com.neurobeat.neurobeats.pages

import androidx.compose.foundation.background
import androidx.compose.foundation.checkScrollableContainerConstraints
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.Button
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.TopAppBarScrollBehavior
import androidx.compose.material3.rememberTopAppBarState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.neurobeat.neurobeats.authentication.viewmodel.AuthenticationState
import com.neurobeat.neurobeats.authentication.viewmodel.AuthenticationViewModel
import com.neurobeat.neurobeats.ui.theme.BackgroundColor
import com.neurobeat.neurobeats.ui.theme.txtColor

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomePage(navController: NavController) {

    val authenticationViewModel: AuthenticationViewModel= viewModel()
    val authState=authenticationViewModel.authState.observeAsState()
    val scrollBehavior= TopAppBarDefaults.enterAlwaysScrollBehavior(rememberTopAppBarState())


    LaunchedEffect(authState.value) {
        when(authState.value){
            is AuthenticationState.NotAuthenticated -> navController.navigate("LoginScreen")
            else -> Unit
        }
    }
    Scaffold(
        modifier = Modifier.nestedScroll(scrollBehavior.nestedScrollConnection),

        topBar = {
            CenterAlignedTopAppBar(
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer,
                    titleContentColor = MaterialTheme.colorScheme.primary,
                ),
                title = {
                    Text(
                        "Centered Top App Bar",
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                },
                actions = {
                    IconButton(onClick = { /* do something */ }) {
                        Icon(
                            imageVector = Icons.Filled.Menu,
                            contentDescription = "Localized description"
                        )
                    }
                },
                scrollBehavior = scrollBehavior,
            )
        },
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .background(BackgroundColor),
            verticalArrangement = Arrangement.Center,

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

                Button(onClick = { navController.navigate("ArtistLibrary") }) {
                    Text(text = "ArtistLibrary")
                }
                Button(onClick = { navController.navigate("MusicPlayer") }) {
                    Text(text = "MusicPlayer")
                }
            }
            TextButton(onClick = {
                authenticationViewModel.signOut()
            }) {
                Text(text = "Sign out")
            }
        }
    }
}