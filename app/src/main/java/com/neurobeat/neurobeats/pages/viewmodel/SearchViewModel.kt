package com.neurobeat.neurobeats.pages.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.neurobeat.neurobeats.api.models.TrackList
import com.neurobeat.neurobeats.authentication.RetrofitInstance
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import java.net.URLEncoder
import java.nio.charset.StandardCharsets

class SearchViewModel : ViewModel() {
    private val _searchResults = MutableLiveData<TrackList>()
    val searchResults: LiveData<TrackList> get() = _searchResults

    fun search(query: String, accessToken: String) {
        val encodedQuery = URLEncoder.encode(query, StandardCharsets.UTF_8.toString())
        viewModelScope.launch {
            try {
                val response = RetrofitInstance.api.search(
                    token = "Bearer $accessToken",
                    query = encodedQuery,
                    type = "track"
                )

                _searchResults.postValue(response.tracks)
                println("Search results:${response.tracks}")

            } catch (e: Exception) {
                println("Error fetching search data: ${e.message}")
            }
        }
    }
}
