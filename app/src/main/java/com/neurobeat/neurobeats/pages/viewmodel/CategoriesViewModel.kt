package com.neurobeat.neurobeats.pages.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import com.neurobeat.neurobeats.api.models.Category
import com.neurobeat.neurobeats.api.models.Playlist
import com.neurobeat.neurobeats.authentication.RetrofitInstance
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

class CategoriesViewModel : ViewModel() {

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()

    private val _categoryPlaylistsMap = MutableStateFlow<Map<Category, List<Playlist>>?>(null)
    val categoryPlaylistsMap: StateFlow<Map<Category, List<Playlist>>?> = _categoryPlaylistsMap

    suspend fun category(token: String) {
        try {
            val categoryResponse = RetrofitInstance.api.getCategories("Bearer $token")
            val categories = categoryResponse.categories.items

            // Fetch playlists for each category
            val categoryPlaylists = categories.associateWith { category ->
                RetrofitInstance.api.getPlaylists("Bearer $token", category.id).playlists.items
            }

            _categoryPlaylistsMap.value = categoryPlaylists
            _isLoading.value=true

        } catch (e: Exception) {
            Log.e("NeuroBeats error", "Error fetching data", e)
        }
    }

    companion object
}
