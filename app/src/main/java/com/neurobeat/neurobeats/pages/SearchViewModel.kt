package com.neurobeat.neurobeats.pages

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.neurobeat.neurobeats.api.models.Track
import com.neurobeat.neurobeats.authentication.RetrofitInstance
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import java.net.URLEncoder
import java.nio.charset.StandardCharsets

class SearchViewModel : ViewModel() {

    private val _searchResults = MutableStateFlow<List<Track>>(emptyList())
    val searchResults: StateFlow<List<Track>> = _searchResults

    fun search(query: String, accessToken: String) {
        val encodedQuery = URLEncoder.encode(query, StandardCharsets.UTF_8.toString())
        viewModelScope.launch {
            try {
                val response = RetrofitInstance.api.search(
                    token = "Bearer $accessToken",
                    query = encodedQuery,
                    type = "track"
                )
                println("Response: $response")

                _searchResults.value = response.tracks.items
                println("Search output: ${_searchResults.value}")
            } catch (e: Exception) {
                println("Error fetching search data: ${e.message}")
            }
        }
    }
}
