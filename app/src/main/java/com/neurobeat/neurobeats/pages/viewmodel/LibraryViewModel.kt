package com.neurobeat.neurobeats.pages.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.neurobeat.neurobeats.api.models.Track
import com.neurobeat.neurobeats.api.models.TrackItem
import com.neurobeat.neurobeats.authentication.RetrofitInstance
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class LibraryViewModel: ViewModel() {

    private val _favTracks = MutableStateFlow<List<Track>>(emptyList())
    val favTracks: StateFlow<List<Track>> get() = _favTracks

    fun getTrack(accessToken:String,trackId:String) {
        viewModelScope.launch {
            println("Library:$accessToken")
            try {
                println("Step 1 executed")
                val response = RetrofitInstance.api.getTrack("Bearer $accessToken", trackId)
                println("Step 2 executed")
                _favTracks.value += response
                Log.d("LibraryViewModel", response.toString())
            } catch (e: Exception) {
                Log.e("LibraryViewModel", "Error fetching artist details", e)
            }
        }
    }


}