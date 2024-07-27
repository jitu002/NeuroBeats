package com.neurobeat.neurobeats.music.viewmodels

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.neurobeat.neurobeats.api.models.TrackList
import com.neurobeat.neurobeats.authentication.RetrofitInstance
import kotlinx.coroutines.launch

class PlaylistViewModel : ViewModel() {
    private val _tracks = MutableLiveData<TrackList>()
    val tracks: LiveData<TrackList> get() = _tracks

    fun fetchPlaylistTracks(token: String, playlistId: String) {
        viewModelScope.launch {
            try {
                val response = RetrofitInstance.api.getPlaylistTracks("Bearer $token", playlistId)

                println("Response: $response")
                _tracks.postValue(response.tracks)

                println("Playlist results:${response.tracks}")

                //items.filter { it.track.preview_url != null })
            } catch (e: Exception) {
                Log.e("PlaylistViewModel", "Error fetching playlist tracks", e)
            }
        }
    }
}
