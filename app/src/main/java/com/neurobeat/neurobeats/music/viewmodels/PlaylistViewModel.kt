package com.neurobeat.neurobeats.music.viewmodels

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.neurobeat.neurobeats.api.models.AlbumResponse
import com.neurobeat.neurobeats.api.models.Track
import com.neurobeat.neurobeats.api.models.TrackItem
import com.neurobeat.neurobeats.authentication.RetrofitInstance
import kotlinx.coroutines.launch

class PlaylistViewModel : ViewModel() {
    private val _tracks = MutableLiveData<List<TrackItem>>()
    val tracks: LiveData<List<TrackItem>> get() = _tracks

    private val _album = MutableLiveData<AlbumResponse>()
    val album: LiveData<AlbumResponse> get() = _album

    private  val _albumTracks = MutableLiveData<List<Track>>()
    val albumTracks: LiveData<List<Track>> get() = _albumTracks

    fun fetchPlaylistTracks(token: String, playlistId: String) {
        viewModelScope.launch {
            try {
                val response = RetrofitInstance.api.getPlaylistTracks("Bearer $token", playlistId)
                _tracks.postValue(response.items.filter { it.track.preview_url != null })
                // Log.d("PlaylistViewModel",response.toString())
            } catch (e: Exception) {
                Log.e("PlaylistViewModel", "Error fetching playlist tracks", e)
            }
        }
    }

    fun fetchAlbumTracks(token: String, albumId: String) {
        viewModelScope.launch {
            try {
                val albumResponse = RetrofitInstance.api.getAlbum("Bearer $token", albumId)
                _albumTracks.postValue(albumResponse.tracks.items.filter { it.preview_url != null })
                _album.postValue(albumResponse)
                Log.d("PlaylistViewModel", albumResponse.toString())
            } catch (e: Exception) {
                Log.e("PlaylistViewModel","Error fetching album tracks", e)
            }
        }
    }
}
