package com.neurobeat.neurobeats.artist.view

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.rememberAsyncImagePainter
import com.neurobeat.neurobeats.api.models.ArtistResponse

@Composable
fun ArtistItemView(artist: ArtistResponse, onClick: () -> Unit) {
    Row(
        modifier = Modifier
            .padding(10.dp)
            .fillMaxWidth()
            .clickable { onClick() }
    ) {
        if (artist.images.isNotEmpty()) {
            val imageUrl = artist.images.firstOrNull()?.url
            Image(
                painter = rememberAsyncImagePainter(model = imageUrl),
                contentDescription = "Artist image",
                modifier = Modifier
                    .size(50.dp)
            )
        }
        Spacer(modifier = Modifier.width(15.dp))
        Column {
            Text(text = artist.name, fontSize = 16.sp)
            Text(text = "Followers: ${artist.followers.total}", fontSize = 14.sp)
        }
    }
}
