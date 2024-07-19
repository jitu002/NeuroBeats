package com.neurobeat.neurobeats.artist.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.neurobeat.neurobeats.api.models.TopTrack
import com.neurobeat.neurobeats.pages.RetrofitInstance
import kotlinx.coroutines.launch

class ArtistLibraryViewModel : ViewModel() {
    private val _tracks = MutableLiveData<List<TopTrack>>()
    val tracks: LiveData<List<TopTrack>> get() = _tracks

    fun fetchArtistTracks(token: String, artistId: String) {
        viewModelScope.launch {
            try {
                Log.d("ArtistLibraryViewModel", "$token, $artistId")

                val response = RetrofitInstance.api.getArtistTracks("Bearer $token", artistId)
                _tracks.postValue(response.tracks.filter { it.preview_url != null })

                Log.d("ArtistLibraryViewModel", "Tracks: ${response.tracks}")
            } catch (e: Exception) {
                Log.e("ArtistLibraryViewModel", "Error fetching artist tracks", e)
            }
        }
    }
}
