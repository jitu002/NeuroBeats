package com.neurobeat.neurobeats.music.viewmodels

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.neurobeat.neurobeats.api.models.TrackItem
import com.neurobeat.neurobeats.pages.RetrofitInstance
import kotlinx.coroutines.launch

class PlaylistViewModel : ViewModel() {
    private val _tracks = MutableLiveData<List<TrackItem>>()
    val tracks: LiveData<List<TrackItem>> get() = _tracks

    fun fetchPlaylistTracks(token: String, playlistId: String) {
        viewModelScope.launch {
            try {
                val response = RetrofitInstance.api.getPlaylistTracks("Bearer $token", playlistId)
                _tracks.postValue(response.items)
                response.items.forEach { trackItem ->
                    Log.d("PlaylistViewModel", "Track: ${trackItem.track.name} by ${trackItem.track.artists.joinToString { it.name }}")
                }

            } catch (e: Exception) {
                Log.d("PlaylistViewModel", "Error fetching playlist tracks", e)
            }
        }
    }
}
