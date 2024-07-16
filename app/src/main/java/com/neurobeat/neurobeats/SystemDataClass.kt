package com.neurobeat.neurobeats

data class User(val usrName:String="",val usrAge:Int=0,val usrEmail:String="")


data class CategoriesResponse(val categories: Categories)
data class Categories(val items: List<Category>)
data class Category(val id: String, val name: String)

data class PlaylistResponse(val playlists: Playlists)
data class Playlists(val items: List<Playlist>)
data class Playlist(val id: String, val name: String, val images: List<ImageData>)
data class ImageData(val url: String)



