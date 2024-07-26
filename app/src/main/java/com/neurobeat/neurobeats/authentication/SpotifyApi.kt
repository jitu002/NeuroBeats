package com.neurobeat.neurobeats.authentication

import com.neurobeat.neurobeats.api.models.CategoriesResponse
import com.neurobeat.neurobeats.api.models.PlaylistResponse
import com.neurobeat.neurobeats.api.models.SpotifySearchResponse
import com.neurobeat.neurobeats.api.models.TrackItem
import com.neurobeat.neurobeats.api.models.TracksResponse
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Path
import retrofit2.http.Query


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

    @GET("search?")
    suspend fun search(
        @Header("Authorization") token: String,
        @Query("q") query: String,
        @Query("type") type: String,
        @Query("market") market: String? = null,
        @Query("limit") limit: Int? = null,
        @Query("offset") offset: Int? = null
    ): SpotifySearchResponse
}


