package com.neurobeat.neurobeats.api.models


data class PlaylistResponse(val playlists: Playlists)
data class Playlists(val items: List<Playlist>)
data class Playlist(val id: String, val name: String, val images: List<ImageData>)
data class ImageData(val url: String)