package com.neurobeat.neurobeats.pages

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material3.Divider
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.neurobeat.neurobeats.R
import com.neurobeat.neurobeats.ui.theme.BackgroundColor
import com.neurobeat.neurobeats.ui.theme.txtBgColor
import com.neurobeat.neurobeats.ui.theme.txtColor

@Composable
fun ArtistLibrary(navController: NavHostController) {
    Column (modifier = Modifier
        .fillMaxSize()
        .background(BackgroundColor),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Box(modifier = Modifier
            .fillMaxWidth()
            .height(300.dp)
        ) {
            Image(
                painter = painterResource(id = R.drawable.perturbator),
                contentDescription = "Unable to load.",
                contentScale = ContentScale.Crop,
                modifier = Modifier.fillMaxSize()
            )
            Text(
                text = "Perturbator",
                color = Color.White,
                fontSize = 36.sp,
                modifier = Modifier
                    .fillMaxWidth()
                    .align(Alignment.BottomStart)
                    .background(brush = Brush.verticalGradient(txtBgColor))
                    .padding(horizontal = 20.dp)
            )
        }
        Row (
            modifier = Modifier
                .padding(20.dp)
                .align(Alignment.End),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(text = "764,564 monthly listeners", color = txtColor, fontSize = 15.sp)
            Spacer(modifier = Modifier.width(30.dp))
            Box(
                modifier = Modifier
                    .size(64.dp)
                    .clip(CircleShape)
                    .clickable { }
                    .border(2.dp, Color.White, CircleShape),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    Icons.Filled.PlayArrow,
                    contentDescription = null,
                    modifier = Modifier.size(43.dp),
                    tint = Color.White
                )
            }
        }

        Text(text = "Popular", color = Color.White, fontSize = 30.sp, modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 40.dp))

        Column (
            modifier = Modifier
                .fillMaxSize()
                .padding(30.dp, 20.dp)
        ) {
            Row (
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 5.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Image(
                    painter = painterResource(id = R.drawable.prison_perturbator),
                    contentDescription = "Prison",
                    contentScale = ContentScale.Crop,
                    modifier = Modifier.size(50.dp)
                )
                Spacer(modifier = Modifier.width(10.dp))
                Column {
                    Text(text = "BODY/PRISON", color = Color.White, fontSize = 18.sp)
                    Text(text = "9,87,456", color = Color.LightGray, fontSize = 15.sp)
                }
            }
            Spacer(modifier = Modifier.height(8.dp))
            Divider(color = Color.Gray)
            Spacer(modifier = Modifier.height(8.dp))
            Row (
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 5.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Image(
                    painter = painterResource(id = R.drawable.prison_perturbator),
                    contentDescription = "Prison",
                    contentScale = ContentScale.Crop,
                    modifier = Modifier.size(50.dp)
                )
                Spacer(modifier = Modifier.width(10.dp))
                Column {
                    Text(text = "BODY/PRISON", color = Color.White, fontSize = 18.sp)
                    Text(text = "9,87,456", color = Color.LightGray, fontSize = 15.sp)
                }
            }
            Spacer(modifier = Modifier.height(8.dp))
            Divider(color = Color.Gray)
            Spacer(modifier = Modifier.height(8.dp))
            Row (
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 5.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Image(
                    painter = painterResource(id = R.drawable.prison_perturbator),
                    contentDescription = "Prison",
                    contentScale = ContentScale.Crop,
                    modifier = Modifier.size(50.dp)
                )
                Spacer(modifier = Modifier.width(10.dp))
                Column {
                    Text(text = "BODY/PRISON", color = Color.White, fontSize = 18.sp)
                    Text(text = "9,87,456", color = Color.LightGray, fontSize = 15.sp)
                }
            }
            Spacer(modifier = Modifier.height(8.dp))
            Divider(color = Color.Gray)
            Spacer(modifier = Modifier.height(8.dp))
            Row (
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 5.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Image(
                    painter = painterResource(id = R.drawable.prison_perturbator),
                    contentDescription = "Prison",
                    contentScale = ContentScale.Crop,
                    modifier = Modifier.size(50.dp)
                )
                Spacer(modifier = Modifier.width(10.dp))
                Column {
                    Text(text = "BODY/PRISON", color = Color.White, fontSize = 18.sp)
                    Text(text = "9,87,456", color = Color.LightGray, fontSize = 15.sp)
                }
            }
            Spacer(modifier = Modifier.height(8.dp))
            Divider(color = Color.Gray)
            Spacer(modifier = Modifier.height(8.dp))
            Row (
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 5.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Image(
                    painter = painterResource(id = R.drawable.prison_perturbator),
                    contentDescription = "Prison",
                    contentScale = ContentScale.Crop,
                    modifier = Modifier.size(50.dp)
                )
                Spacer(modifier = Modifier.width(10.dp))
                Column {
                    Text(text = "BODY/PRISON", color = Color.White, fontSize = 18.sp)
                    Text(text = "9,87,456", color = Color.LightGray, fontSize = 15.sp)
                }
            }
        }
    }
}