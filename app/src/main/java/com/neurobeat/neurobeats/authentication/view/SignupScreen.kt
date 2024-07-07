package com.neurobeat.neurobeats.authentication.view

import android.widget.Toast
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedCard
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
import androidx.compose.ui.graphics.Brush
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
import com.neurobeat.neurobeats.ui.theme.gColor
import com.neurobeat.neurobeats.ui.theme.txtColor

@Composable
fun SignupScreen(navController: NavController) {

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



    Column (
        modifier = Modifier
            .fillMaxSize()
            .background(Brush.linearGradient(gColor)),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        OutlinedCard(
            modifier = Modifier
                .height(500.dp)
                .padding(25.dp)
                .fillMaxWidth(),
            colors = CardDefaults.outlinedCardColors(Color.Transparent),
            border = BorderStroke(2.dp,color = Color.White)
        ) {
            Column (
                modifier = Modifier.fillMaxSize(),
                horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = "NeuroBeats",
                        color = txtColor,
                        modifier = Modifier.padding(top = 20.dp),
                        fontSize = 35.sp
                    )
                    Spacer(modifier = Modifier.height(25.dp))
                    Text(
                        text = "Sign Up",
                        color = txtColor,
                        modifier = Modifier.padding(20.dp),
                        fontSize = 32.sp,
                        textAlign = TextAlign.Start
                    )

                    OutlinedTextField(
                        value = email,
                        onValueChange = { email = it },
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 15.dp),
                        label = { Text("Email ID ", color = txtColor) },
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
                        value = password,
                        onValueChange = { password = it },
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 15.dp),
                        label = { Text(text = "Password", color = txtColor) },
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

                    Spacer(modifier = Modifier.height( 60.dp))
                    Button(
                        onClick = {
                            authenticationViewModel.signUp(email,password)
                        },
                        colors = ButtonDefaults.buttonColors(txtColor),
                        modifier = Modifier
                            .width(130.dp)
                            .height(70.dp)

                    ) {
                        Text(
                            text = "Create Account",
                            fontSize = 18.sp,
                            textAlign = TextAlign.Center,
                            color = Color(4, 2, 29, 255)
                        )
                    }
                }
        }

        Spacer(modifier = Modifier.height(20.dp))

        TextButton(onClick = {
            navController.navigate("LoginScreen")
        }) {
            Text(text = "Login")
        }
    }
}