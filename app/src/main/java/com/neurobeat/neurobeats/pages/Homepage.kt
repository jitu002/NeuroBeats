package com.neurobeat.neurobeats.pages

import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
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
import com.neurobeat.neurobeats.api.models.CategoriesResponse
import com.neurobeat.neurobeats.api.models.Category
import com.neurobeat.neurobeats.api.models.Playlist
import com.neurobeat.neurobeats.api.models.PlaylistResponse
import com.neurobeat.neurobeats.api.models.TracksResponse
import com.neurobeat.neurobeats.authentication.SpotifyAuth
import com.neurobeat.neurobeats.authentication.viewmodel.AuthenticationState
import com.neurobeat.neurobeats.authentication.viewmodel.AuthenticationViewModel
import com.neurobeat.neurobeats.ui.theme.BackgroundColor
import com.neurobeat.neurobeats.ui.theme.BarColor
import com.neurobeat.neurobeats.ui.theme.txtColor
import kotlinx.coroutines.launch
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Path

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomePage(navController: NavController) {

    val authenticationViewModel: AuthenticationViewModel= viewModel()
    val authState=authenticationViewModel.authState.observeAsState()
    val scrollBehavior= TopAppBarDefaults.enterAlwaysScrollBehavior(rememberTopAppBarState())
    var accessToken by remember { mutableStateOf<String?>(null) }
    var categoryPlaylistsMap by remember { mutableStateOf<Map<Category, List<Playlist>>?>(null) }
    val coroutineScope = rememberCoroutineScope()
    val dboperation=DatabaseOperation()
    val drawerState = rememberDrawerState(DrawerValue.Closed)
    val scope= rememberCoroutineScope()
    val userScope= rememberCoroutineScope()

    var username by remember {
        mutableStateOf("")
    }


    LaunchedEffect(authState.value) {
        when(authState.value){
            is AuthenticationState.NotAuthenticated -> navController.navigate("LoginScreen")
            else ->userScope.launch {
                Log.d("AuthState","${authState.value}")
                dboperation.fetchDataFromFirebase(FirebaseAuth.getInstance(), FirebaseFirestore.getInstance()){user->
                    username = user?.usrName ?: "Guest"
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
                    .fillMaxHeight(),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                TextButton(onClick = {
                    authenticationViewModel.signOut()
                }) {
                    Text(text = "Sign out")
                }
            }

        }
    ) {
        Scaffold(
            modifier = Modifier.nestedScroll(scrollBehavior.nestedScrollConnection),

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
                                    contentDescription = "Menu Icon"
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
                verticalArrangement = Arrangement.Top,

                ) {
                LaunchedEffect(Unit) {
                    SpotifyAuth.getAccessToken { token ->
                        accessToken = token
                        token?.let {
                            coroutineScope.launch {
                                try {
                                    val categoryResponse = RetrofitInstance.api.getCategories("Bearer $it")
                                    val categories = categoryResponse.categories.items
                                    val categoryPlaylists = categories.associateWith { category ->
                                        RetrofitInstance.api.getPlaylists("Bearer $it", category.id).playlists.items
                                    }
                                    categoryPlaylistsMap = categoryPlaylists
                                    Log.d("categoryPlaylistsMap", "$categoryPlaylistsMap")
                                } catch (e: Exception) {
                                    Log.e("NeuroBeats error", "Error fetching data", e)
                                }
                            }
                        }
                    }
                }

                categoryPlaylistsMap?.let {
                    CategoryPlaylistsList(it, navController, accessToken)
                } ?: run {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center,
                        modifier = Modifier.fillMaxSize()
                    ) {
                        Text("Hang On!!!")
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
                        modifier = Modifier.padding(16.dp)
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
    Column ( modifier = Modifier.clickable { navController.navigate("TracksScreen/${playlist.id}/${accessToken}") } ) {
        if (playlist.images.isNotEmpty()){
            val imageUrl=playlist.images.first().url
            Image(
                painter = rememberAsyncImagePainter(model = imageUrl ),
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
                modifier = Modifier.width(150.dp)
            )
        }
    }
}

interface SpotifyApi {
    @GET("browse/categories")
    suspend fun getCategories(
        @Header("Authorization") token: String
    ): CategoriesResponse

    @GET("browse/categories/{category_id}/playlists")
    suspend fun getPlaylists(
        @Header("Authorization") token: String,
        @Path("category_id") categoryId: String
    ): PlaylistResponse

    @GET("playlists/{playlist_id}/tracks")
    suspend fun getPlaylistTracks(
        @Header("Authorization") token: String,
        @Path("playlist_id") playlistId: String
    ): TracksResponse
}

object RetrofitInstance {
    private const val BASE_URL = "https://api.spotify.com/v1/"

    val api: SpotifyApi by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(SpotifyApi::class.java)
    }
}
