package com.neurobeat.neurobeats.authentication.view

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
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
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.neurobeat.neurobeats.ui.theme.gColor
import com.neurobeat.neurobeats.ui.theme.txtColor

@Composable
fun LoginScreen(navController: NavController) {
    var newUser by remember {
        mutableStateOf(false)
    }
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
                    text = if (!newUser) "Login" else "Sign Up",
                    color = txtColor,
                    modifier = Modifier.padding(20.dp),
                    fontSize = 32.sp,
                    textAlign = TextAlign.Start
                )

                var username by remember {
                    mutableStateOf("")
                }
                OutlinedTextField(
                    value = username,
                    onValueChange = { username = it },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 15.dp),
                    label = { Text("Email ID or Mobile Number", color = txtColor) },
                    maxLines = 1,
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedTextColor = txtColor,
                        focusedBorderColor = Color.White,
                        unfocusedBorderColor = Color.White,
                        unfocusedTextColor = txtColor
                    )
                )
                Spacer(modifier = Modifier.height(15.dp))
                var showPassword by remember {
                    mutableStateOf(false)
                }
                var password by remember {
                    mutableStateOf("")
                }
                val passwordVisualTransformation = remember { PasswordVisualTransformation() }
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
                                Icons.Filled.Visibility
                            } else {
                                Icons.Filled.VisibilityOff
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
                if (!newUser) {
                    Row ( modifier = Modifier
                        .fillMaxWidth()
                        .padding(15.dp), horizontalArrangement = Arrangement.End ) {
                        Text(
                            text = "Forgot Password?",
                            color = txtColor,
                            fontSize = 15.sp,
                            modifier = Modifier.clickable { }
                        )
                    }
                }

                Spacer(modifier = Modifier.height(10.dp))
                Button(
                    onClick = { newUser = !newUser }, // use Authentication.signIN() here
                    colors = ButtonDefaults.buttonColors(txtColor),
                    modifier = Modifier
                        .width(120.dp)
                        .height(55.dp)
                ) {
                    Text(
                        text = if (!newUser) "Login" else "Sign Up",
                        fontSize = 18.sp,
                        color = Color(4, 2, 29, 255)
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(20.dp))

        Column (
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.fillMaxWidth()
        ) {
            Row {
                Text(
                    text = if (!newUser) "Don't have an account?" else "Already have an account?",
                    fontSize = 16.sp,
                    color = txtColor
                )
                Spacer(modifier = Modifier.width(4.dp))
                Text(
                    text = if (!newUser) "Sign Up" else "Login",
                    color = Color(230, 124, 255, 255),
                    modifier = Modifier.clickable {},
                    fontSize = 16.sp
                )
            }
        }
    }
}