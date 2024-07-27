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


// Search data class
data class SearchResponse(
    val tracks: SearchTrackList
)
data class SearchTrackList(
    val items: List<Track>
)

// Tracks Data class
data class TracksResponse(
    val items: List<TrackItem>
)

data class TrackItem(
    val track: Track
)

// Track data class
data class Track(
    val id: String,
    val name: String,
    val artists: List<Artist>,
    val album: Album,
    val preview_url: String?,
    val duration_ms: Int
)

// Artist data class
data class Artist(
    val id: String,
    val name: String
)

// Album data class
data class Album(
    val id: String,
    val name: String,
    val images: List<Image>
)

// Image data class
data class Image(
    val url: String,
    val height: Int?,
    val width: Int?
)

// Artist Data class

data class ArtistList(
    val artists: List<ArtistResponse>
)

data class ArtistResponse(
    val id: String,
    val name: String,
    val genres: List<String>,
    val images: List<Image>,
    val followers: Followers,
    val popularity: Int
)

data class Followers(
    val total: Int
)

// Top-Track data classes

data class TopTracksResponse(
    val tracks: List<Track>
)
