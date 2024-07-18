package com.neurobeat.neurobeats.api.models


//User api signature
data class User(val usrName:String="",val usrAge:Int=0,val usrEmail:String="")




// Category api signature
data class CategoriesResponse(val categories: Categories)
data class Categories(val items: List<Category>)
data class Category(val id: String, val name: String)



//Playlist api signature
data class PlaylistResponse(val playlists: Playlists)
data class Playlists(val items: List<Playlist>)
data class Playlist(val id: String = "",val name: String = "",val images: List<ImageData> = emptyList())
data class ImageData(val url: String)



//Track api signature
data class Tracks(
    val href: String
)

data class TracksResponse(
    val items: List<TrackItem>
)

data class TrackItem(
    val track: Track
)

data class Track(
    val id: String,
    val name: String,
    val artists: List<Artist>,
    val album: Album,
    val preview_url: String?,
    val duration_ms: Int
)

data class Artist(
    val id: String,
    val name: String
)

data class Album(
    val id: String,
    val name: String,
    val images: List<Image>
)

data class Image(
    val url: String,
    val height: Int?,
    val width: Int?
)


