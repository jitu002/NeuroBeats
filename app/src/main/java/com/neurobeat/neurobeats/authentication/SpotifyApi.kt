package com.neurobeat.neurobeats.authentication

import com.neurobeat.neurobeats.api.models.AlbumResponse
import com.neurobeat.neurobeats.api.models.ArtistList
import com.neurobeat.neurobeats.api.models.ArtistResponse
import com.neurobeat.neurobeats.api.models.CategoriesResponse
import com.neurobeat.neurobeats.api.models.PlaylistResponse
import com.neurobeat.neurobeats.api.models.SearchResponse
import com.neurobeat.neurobeats.api.models.TopTracksResponse
import com.neurobeat.neurobeats.api.models.Track
import com.neurobeat.neurobeats.api.models.TrackItem
import com.neurobeat.neurobeats.api.models.TracksResponse
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Path
import retrofit2.http.Query


interface SpotifyApi {

    //Fetching different categories
    @GET("browse/categories")
    suspend fun getCategories(
        @Header("Authorization") token: String
    ): CategoriesResponse

    //Fetching playlists under different categories
    @GET("browse/categories/{category_id}/playlists")
    suspend fun getPlaylists(
        @Header("Authorization") token: String,
        @Path("category_id") categoryId: String
    ): PlaylistResponse

    //Fetching different tracks under playlists
    @GET("playlists/{playlist_id}/tracks")
    suspend fun getPlaylistTracks(
        @Header("Authorization") token: String,
        @Path("playlist_id") playlistId: String
    ): TracksResponse

    //Fetching  albums
    @GET("albums/{id}")
    suspend fun getAlbum(
        @Header("Authorization") token: String,
        @Path("id") albumId: String
    ): AlbumResponse

    //Fetching artist
    @GET("artists")
    suspend fun getArtists(
        @Header("Authorization") token: String,
        @Query("ids") ids: String
    ): ArtistList

    //Fetching top tracks of artists
    @GET("artists/{id}/top-tracks")
    suspend fun getArtistTracks(
        @Header("Authorization") token: String,
        @Path("id") artistId: String,
        @Query("market") market: String = "IN"
    ): TopTracksResponse

    @GET("artists/{id}")
    suspend fun getArtist(
        @Header("Authorization") token: String,
        @Path("id") artistId: String
    ): ArtistResponse

    //Fetching search results
    @GET("search?")
    suspend fun search(
        @Header("Authorization") token: String,
        @Query("q") query: String,
        @Query("type") type: String,
        @Query("market") market: String? = null,
        @Query("limit") limit: Int? = null,
        @Query("offset") offset: Int? = null
    ): SearchResponse

    //Fetching recently played songs
    @GET("me/player/recently-played")
    suspend fun recent(
        @Header("Authorization")token: String
    ):TracksResponse

    //Fetching tracks using id
    @GET("tracks/{id}")
    suspend fun getTrack(
        @Header("Authorization") token: String,
        @Path("id") trackId: String
    ): Track
}


