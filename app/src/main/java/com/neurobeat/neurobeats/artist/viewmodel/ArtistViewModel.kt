package com.neurobeat.neurobeats.artist.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.neurobeat.neurobeats.api.models.ArtistResponse
import com.neurobeat.neurobeats.authentication.RetrofitInstance
import kotlinx.coroutines.launch

class ArtistViewModel: ViewModel() {
    private val _artistDetails = MutableLiveData<ArtistResponse>()
    val artistDetails: LiveData<ArtistResponse> get() = _artistDetails

    fun fetchArtistDetails(token: String, artistId: String) {
        viewModelScope.launch {
            try {
                val response = RetrofitInstance.api.getArtist("Bearer $token", artistId)
                _artistDetails.postValue(response)
                Log.d("ArtistViewModel", response.toString())
            } catch (e: Exception) {
                Log.e("ArtistViewModel", "Error fetching artist details", e)
            }
        }
    }
}