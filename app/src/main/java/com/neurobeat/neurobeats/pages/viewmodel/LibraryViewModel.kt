package com.neurobeat.neurobeats.pages.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.neurobeat.neurobeats.api.models.Track
import com.neurobeat.neurobeats.authentication.RetrofitInstance
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class LibraryViewModel: ViewModel() {

    private val _favTracks = MutableStateFlow<List<Track>>(emptyList())
    val favTracks: StateFlow<List<Track>> get() = _favTracks

    private val _refreshTrigger = MutableStateFlow(0)
    val refreshTrigger: StateFlow<Int> = _refreshTrigger.asStateFlow()

    fun triggerRefresh() {
        _refreshTrigger.value += 1
    }

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

    fun clearTracks() {
        _favTracks.value = emptyList()
    }


}