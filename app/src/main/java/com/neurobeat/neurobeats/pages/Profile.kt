package com.neurobeat.neurobeats.pages

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ChevronLeft
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.neurobeat.neurobeats.DatabaseOperation
import com.neurobeat.neurobeats.api.models.User
import com.neurobeat.neurobeats.ui.theme.BackgroundColor
import com.neurobeat.neurobeats.ui.theme.profileColor
import com.neurobeat.neurobeats.ui.theme.txtColor

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Profile(navController: NavController){

    val dboperation= DatabaseOperation()
    var username by remember { mutableStateOf("") }
    var age by remember { mutableStateOf(0) }
    var email by remember { mutableStateOf("") }
    var profileTxt by remember { mutableStateOf("") }
    var readOnly by remember { mutableStateOf(true) }
    val user= User(username,age,email)
    val dbOperation=DatabaseOperation()

    val backgroundColor = remember { profileColor.random() }



    LaunchedEffect(Unit) {
        dboperation.fetchDataFromFirebase(
            FirebaseAuth.getInstance(),
            FirebaseFirestore.getInstance()
        ) { user ->
            if (user != null) {
                username = user.usrName
                age = user.usrAge
                email = user.usrEmail
                profileTxt=username.first().uppercaseChar().toString()
            }
        }
    }

    Column(
        verticalArrangement = Arrangement.spacedBy(60.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .fillMaxSize()
            .background(BackgroundColor)
            .padding(top = 50.dp)
    ) {
        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier
                .background(backgroundColor, CircleShape)
                .size(250.dp)
                .fillMaxSize()
        ) {
            Text(text = profileTxt, color = txtColor, fontSize = 200.sp)
        }
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(20.dp),
            modifier = Modifier.padding(horizontal = 15.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(text = "Name",color= txtColor, style = MaterialTheme.typography.bodyLarge)
                TextField(
                    value = username,
                    maxLines = 1,
                    onValueChange = { username = it },
                    readOnly = readOnly,
                    colors = TextFieldDefaults.colors(
                        unfocusedContainerColor = Color.Transparent,
                        focusedIndicatorColor = Color.Cyan,
                        focusedContainerColor = Color.Transparent
                    ),
                    textStyle = MaterialTheme.typography.bodyLarge.copy(fontSize = 16.sp, textAlign = TextAlign.Center),
                    modifier = Modifier
                        .background(Color.Transparent),
                )
            }
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(text = "Age",color= txtColor, style = MaterialTheme.typography.bodyLarge)
                TextField(
                    value = age.toString(),
                    maxLines = 1,
                    onValueChange =
                    {
                        val newAge = it.toIntOrNull()
                        if (newAge != null) {
                            age = newAge
                        }
                        else{
                            age=0
                        }
                    },
                    readOnly = readOnly,
                    colors = TextFieldDefaults.colors(
                        unfocusedContainerColor = Color.Transparent,
                        focusedIndicatorColor = Color.Cyan,
                        focusedContainerColor = Color.Transparent
                    ),
                    textStyle = MaterialTheme.typography.bodyLarge.copy(fontSize = 16.sp, textAlign = TextAlign.Center),
                    modifier = Modifier
                        .background(Color.Transparent),
                )
            }
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(text = "Email",color= txtColor, style = MaterialTheme.typography.bodyLarge)
                TextField(
                    value = email,
                    maxLines = 1,
                    onValueChange = { email = it },
                    readOnly = true,
                    colors = TextFieldDefaults.colors(
                        unfocusedContainerColor = Color.Transparent,
                        focusedIndicatorColor = Color.Cyan,
                        focusedContainerColor = Color.Transparent
                    ),
                    textStyle = MaterialTheme.typography.bodyLarge.copy(fontSize = 16.sp, textAlign = TextAlign.Center),
                    modifier = Modifier
                        .background(Color.Transparent),
                )
            }
            OutlinedButton(
                onClick = {
                    readOnly=!readOnly
                    dbOperation.updateData(user, FirebaseAuth.getInstance(), FirebaseFirestore.getInstance())
                },
                modifier = Modifier
                    .padding(top = 20.dp)
                    .width(120.dp)
                    .height(50.dp)
            ) {
                Text(text = if (readOnly)"Edit" else "Update", style = MaterialTheme.typography.bodyLarge,fontSize=16.sp)
            }
            TextButton(onClick = {
                navController.navigate("Homepage")
            }) {
                Row(
                    horizontalArrangement = Arrangement.spacedBy(7.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(imageVector = Icons.Default.ChevronLeft, contentDescription = "Right Arrow", modifier = Modifier.size(50.dp,50.dp))
                    Text(text = "Back To music")
                }
            }
        }
    }


}