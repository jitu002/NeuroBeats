package com.neurobeat.neurobeats.api.models

data class Tracks(
    val href: String
)

data class TracksResponse(
    val items: List<TrackItem>
)

data class TrackItem(
    val track: Track
)

data class Track(
    val id: String,
    val name: String,
    val artists: List<Artist>,
    val album: Album,
    val previewUrl: String?
)

data class Artist(
    val id: String,
    val name: String
)

data class Album(
    val id: String,
    val name: String,
    val images: List<Image>
)

data class Image(
    val url: String,
    val height: Int?,
    val width: Int?
)
