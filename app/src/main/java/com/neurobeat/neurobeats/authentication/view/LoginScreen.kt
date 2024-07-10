package com.neurobeat.neurobeats.authentication.view

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
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
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ChevronRight
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.FilledTonalButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.neurobeat.neurobeats.authentication.viewmodel.AuthenticationState
import com.neurobeat.neurobeats.authentication.viewmodel.AuthenticationViewModel
import com.neurobeat.neurobeats.ui.theme.BackgroundColor
import com.neurobeat.neurobeats.ui.theme.txtColor



@Composable
fun LoginScreen(navController: NavController) {

    val authenticationViewModel: AuthenticationViewModel= viewModel()
    val authState=authenticationViewModel.authState.observeAsState()

    val context = LocalContext.current

    LaunchedEffect(authState.value) {
        when (authState.value) {
            is AuthenticationState.Authenticated -> navController.navigate("Homepage")
            is AuthenticationState.Error -> Toast.makeText(
                context,
                (authState.value as AuthenticationState.Error).errMessage, Toast.LENGTH_SHORT
            ).show()

            else -> Unit
        }
    }

    var email by remember {
        mutableStateOf("")
    }
    var showPassword by remember {
        mutableStateOf(false)
    }
    var password by remember {
        mutableStateOf("")
    }
    val passwordVisualTransformation = remember { PasswordVisualTransformation() }


    Box(modifier = Modifier
        .fillMaxSize()
        .background(BackgroundColor),
    ){
        Column (
            modifier = Modifier
                .fillMaxSize(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Card(
                modifier = Modifier
                    .height(500.dp)
                    .padding(25.dp)
                    .fillMaxWidth(),
                colors = CardDefaults.outlinedCardColors(Color.Transparent),
                elevation = CardDefaults.cardElevation(0.dp)

            ) {
                Column (
                    modifier = Modifier.fillMaxSize(),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = "NeuroBeats",
                        style=MaterialTheme.typography.titleLarge,
                        color = txtColor,
                        modifier = Modifier.padding(top = 20.dp),
                        fontSize =50.sp
                    )
                    Spacer(modifier = Modifier.height(25.dp))
                    Text(
                        text = "Login",
                        style = MaterialTheme.typography.bodyLarge,
                        color = txtColor,
                        modifier = Modifier.padding(20.dp),
                        fontSize = 32.sp,
                        textAlign = TextAlign.Start
                    )

                    OutlinedTextField(
                        leadingIcon = {
                            Icon(
                                imageVector = Icons.Default.Email,
                                contentDescription = "Email icon",
                                tint = txtColor
                            )
                                      },
                        value = email,
                        onValueChange = { email = it },
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 13.dp),
                        label = { Text("Email ID ", color = txtColor,fontSize = 14.sp) },
                        maxLines = 1,
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedTextColor = txtColor,
                            focusedBorderColor = Color.White,
                            unfocusedBorderColor = Color.White,
                            unfocusedTextColor = txtColor
                        )
                    )
                    Spacer(modifier = Modifier.height(15.dp))

                    OutlinedTextField(
                        leadingIcon = {
                            Icon(imageVector = Icons.Default.Lock, contentDescription ="password icon", tint = txtColor)
                        },
                        value = password,
                        onValueChange = { password = it },
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 13.dp),
                        label = { Text(text = "Password", color = txtColor, fontSize = 14.sp) },
                        maxLines = 1,
                        visualTransformation = if (showPassword) {
                            VisualTransformation.None
                        } else {
                            passwordVisualTransformation
                        },
                        trailingIcon = {
                            Icon(
                                if (showPassword) {
                                    Icons.Filled.VisibilityOff
                                } else {
                                    Icons.Filled.Visibility
                                },
                                contentDescription = "Visibility_Icon",
                                modifier = Modifier.clickable { showPassword = !showPassword },
                                tint = txtColor
                            )
                        },
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedTextColor = txtColor,
                            focusedBorderColor = Color.White,
                            unfocusedBorderColor = Color.White,
                            unfocusedTextColor = txtColor
                        )
                    )

                }

            }
            FilledTonalButton(
                onClick = {
                    authenticationViewModel.login(email,password)
                }, // use Authentication.signIN() here
                colors = ButtonDefaults.buttonColors(txtColor),
                modifier = Modifier
                    .width(120.dp)
                    .height(50.dp)
            ) {
                Text(
                    text = "Login",
                    style=MaterialTheme.typography.bodyLarge,
                    fontSize = 16.sp,
                    color = Color(4, 2, 29, 255)
                )
            }

            Spacer(modifier = Modifier.height(20.dp))

            TextButton(onClick = {
                navController.navigate("SignupScreen")
            }) {
                Row(
                    horizontalArrangement = Arrangement.spacedBy(10.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(text = "Don't have an account? Signup")
                    Icon(imageVector = Icons.Default.ChevronRight, contentDescription = "Right Arrow", modifier = Modifier.size(50.dp,50.dp))
                }

            }
        }
    }

}