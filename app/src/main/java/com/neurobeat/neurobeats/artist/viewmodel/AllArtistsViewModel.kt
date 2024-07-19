package com.neurobeat.neurobeats.artist.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.neurobeat.neurobeats.api.models.ArtistResponse
import com.neurobeat.neurobeats.pages.RetrofitInstance
import kotlinx.coroutines.launch

class AllArtistsViewModel : ViewModel() {
    private val _artists = MutableLiveData<List<ArtistResponse>>()
    val artists: LiveData<List<ArtistResponse>> get() = _artists

    fun fetchArtists(token: String, artistIds: String) {
        viewModelScope.launch {
            try {
                val allArtists = mutableListOf<ArtistResponse>()
                val response = RetrofitInstance.api.getArtists(
                    token = "Bearer $token",
                    ids = artistIds
                )
                allArtists.addAll(response.artists)
                _artists.postValue(allArtists)
            } catch (e: Exception) {
                Log.e("ArtistsViewModel", "Error fetching artists", e)
            }
        }
    }
}
