package com.neurobeat.neurobeats.pages.view

import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.LibraryMusic
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.BottomAppBarDefaults
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberDrawerState
import androidx.compose.material3.rememberTopAppBarState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import coil.compose.rememberAsyncImagePainter
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.neurobeat.neurobeats.DatabaseOperation
import com.neurobeat.neurobeats.api.models.Category
import com.neurobeat.neurobeats.api.models.Playlist
import com.neurobeat.neurobeats.authentication.SpotifyAuth
import com.neurobeat.neurobeats.authentication.viewmodel.AuthenticationState
import com.neurobeat.neurobeats.authentication.viewmodel.AuthenticationViewModel
import com.neurobeat.neurobeats.pages.viewmodel.CategoriesViewModel
import com.neurobeat.neurobeats.ui.theme.BackgroundColor
import com.neurobeat.neurobeats.ui.theme.BarColor
import com.neurobeat.neurobeats.ui.theme.profileColor
import com.neurobeat.neurobeats.ui.theme.txtColor
import kotlinx.coroutines.launch
import java.net.URLEncoder
import java.nio.charset.StandardCharsets

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomePage(navController: NavController) {

    val categoriesViewModel: CategoriesViewModel = viewModel()

    val categoryPlaylistsMap by categoriesViewModel.categoryPlaylistsMap.collectAsState()
    val authenticationViewModel: AuthenticationViewModel= viewModel()
    val authState=authenticationViewModel.authState.observeAsState()

    val topScrollBehavior= TopAppBarDefaults.enterAlwaysScrollBehavior(rememberTopAppBarState())
    val bottomScrollBehavior= BottomAppBarDefaults.exitAlwaysScrollBehavior()

    var accessToken by remember { mutableStateOf<String?>(null) }

    val coroutineScope = rememberCoroutineScope()
    val dboperation=DatabaseOperation()

    val drawerState = rememberDrawerState(DrawerValue.Closed)
    val scope= rememberCoroutineScope()
    val userScope= rememberCoroutineScope()

    var profileTxt by remember { mutableStateOf("") }
    val backgroundColor = remember { profileColor.random() }


    var username by remember {
        mutableStateOf("")
    }


    LaunchedEffect(authState.value) {
        when(authState.value){
            is AuthenticationState.NotAuthenticated -> navController.navigate("LoginScreen")
            else ->userScope.launch {
                Log.d("AuthState","${authState.value}")
                dboperation.fetchDataFromFirebase(FirebaseAuth.getInstance(), FirebaseFirestore.getInstance()){user->
                    username = user?.usrName?.split(" ")?.getOrNull(0) ?: "Guest"
                    profileTxt=username.first().uppercaseChar().toString()
                }
                SpotifyAuth.getAccessToken { token ->
                    accessToken = token
                    token?.let {
                        coroutineScope.launch {
                            accessToken?.let { it1 -> categoriesViewModel.category(it1) }
                        }
                    }
                }
            }
        }
    }

    ModalNavigationDrawer(

        drawerState = drawerState,
        drawerContent = {

            Column(
                modifier= Modifier
                    .background(BackgroundColor)
                    .fillMaxHeight()
                    .width(200.dp)
                    .padding(top = 50.dp),
                horizontalAlignment = Alignment.Start,
                verticalArrangement = Arrangement.Top
            ) {

                Column(
                    verticalArrangement = Arrangement.spacedBy(10.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Box(
                        contentAlignment = Alignment.Center,
                        modifier = Modifier
                            .background(backgroundColor, CircleShape)
                            .size(120.dp)
                            .fillMaxSize()
                            .clickable {
                                navController.navigate("Profile")
                            }
                    ) {
                        Text(text = profileTxt,color= txtColor, fontSize = 50.sp)
                    }
                    Text(text = username, style = MaterialTheme.typography.bodyLarge, color = txtColor)

                }
                TextButton(
                    onClick = {
                        authenticationViewModel.signOut()
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .width(200.dp)
                        .padding(20.dp)
                ) {
                    Text(text = "Sign out", style = MaterialTheme.typography.bodyLarge, color = txtColor, fontSize = 18.sp,modifier = Modifier.fillMaxWidth())
                }
            }

        }
    ) {
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
                            "Welcome $username",
                            maxLines = 3,
                            fontSize =27.sp,
                            overflow = TextOverflow.Ellipsis
                        )
                    },
                    navigationIcon = {
                        IconButton(
                            onClick = {
                                scope.launch {
                                    drawerState.open()
                                }
                            }) {
                            Icon(
                                imageVector = Icons.Filled.Menu,
                                contentDescription = "Menu Icon",
                                tint = txtColor
                            )
                        }
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
                        IconButton(onClick = { navController.navigate("Homepage") },colors=IconButtonDefaults.iconButtonColors(Color.Magenta)) {
                            Icon(imageVector = Icons.Default.Home, contentDescription ="Home icon" )
                        }
                        IconButton(onClick = { navController.navigate("Search") }) {
                            Icon(imageVector = Icons.Default.Search, contentDescription = "Search icon")
                        }
                        IconButton(onClick = { navController.navigate("Library/$accessToken") }){
                            Icon(imageVector = Icons.Default.LibraryMusic, contentDescription = "Library music")
                        }
                    }
                }
            }
        ) { innerPadding ->
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding)
                    .background(BackgroundColor),
                verticalArrangement = Arrangement.Top,

                ) {

                categoryPlaylistsMap?.let {
                    CategoryPlaylistsList(it, navController, accessToken)
                } ?: run {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center,
                        modifier = Modifier.fillMaxSize()
                    ) {
                        Text("Hang On!!!", color = txtColor)
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
}




@Composable
fun CategoryPlaylistsList(
    categoryPlaylistsMap: Map<Category, List<Playlist>>,
    navController: NavController,
    accessToken: String?
) {
    LazyColumn{
        categoryPlaylistsMap.forEach { (category, playlists) ->
            item {
                Column(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalAlignment = Alignment.Start
                ){
                    Text(
                        text = category.name,
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.Bold,
                        fontSize = 26.sp,
                        modifier = Modifier.padding(16.dp),
                        color = txtColor
                    )
                    LazyRow(
                        contentPadding = PaddingValues(start = 16.dp, end = 16.dp),
                        horizontalArrangement = Arrangement.spacedBy(24.dp),
                        modifier= Modifier
                            .fillMaxSize()
                            .height(200.dp)
                    ) {
                        if(playlists.isNotEmpty()){
                            items(playlists) { playlist ->
                                PlaylistItem(playlist, navController, accessToken)
                            }
                        }
                        else{
                            item {
                                Text(text = "Start listening to get recommendations.",
                                    fontSize = 18.sp,
                                    maxLines = 3,
                                    color = txtColor,
                                    modifier = Modifier.
                                    width(LocalConfiguration.current.screenWidthDp.dp))
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun PlaylistItem(playlist: Playlist, navController: NavController, accessToken: String?) {
    var playlistImage = ""
    Column (
        modifier = Modifier.clickable {
            val encodedImageUrl = URLEncoder.encode(playlistImage, StandardCharsets.UTF_8.toString())
            navController.navigate("TracksScreen/${playlist.id}/$accessToken/$encodedImageUrl")
        }
    ) {
        if (playlist.images.isNotEmpty()){
            playlistImage = playlist.images.first().url
            Image(
                painter = rememberAsyncImagePainter(model = playlistImage ),
                contentDescription = "playlist image",
                modifier = Modifier
                    .size(150.dp)
                    .clip(RoundedCornerShape(21.dp)),
            )
            Text(
                text = playlist.name,
                fontSize = 14.sp,
                overflow = TextOverflow.Ellipsis, // Use Ellipsis to indicate text overflow
                maxLines = 2,
                modifier = Modifier.width(150.dp),
                color = txtColor
            )
        }
    }
}
