package com.example.civicalertoriginal.Screens

import android.annotation.SuppressLint
import android.widget.Toast
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Call
import androidx.compose.material.icons.rounded.Edit
import androidx.compose.material.icons.rounded.Face
import androidx.compose.material.icons.rounded.Home
import androidx.compose.material.icons.rounded.List
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.civicalertoriginal.Components.BottomButtonsMyProfile
import com.example.civicalertoriginal.Components.ProfileText
import com.google.firebase.database.core.Context

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun UpdateProfile(navController: NavController) {
    var showDialog by remember { mutableStateOf(false) }
    var registrationMessage by remember { mutableStateOf("") }

    Surface(color = Color.White) {
        Scaffold(bottomBar = {
            BottomAppBar {
                Row {
                    Column( modifier = Modifier.clickable { navController.navigate("Dashboard") }) {
                        Icon(
                            imageVector = Icons.Rounded.Home, contentDescription = "", modifier = Modifier
                                .size(30.dp)
                                .align(Alignment.CenterHorizontally)
                        )
                        Text(text = "HOME", fontSize = 12.sp)
                    }
                    Spacer(modifier = Modifier.size(20.dp))
                    Column(modifier = Modifier.clickable {  }) {
                        Icon(
                            imageVector = Icons.Rounded.Edit, contentDescription = "", modifier = Modifier
                                .size(30.dp)
                                .align(Alignment.CenterHorizontally)
                        )
                        Text(text = "MAKE REPORTS", fontSize = 12.sp)
                    }
                    Spacer(modifier = Modifier.size(20.dp))
                    Column( modifier = Modifier.clickable {  }) {
                        Icon(
                            imageVector = Icons.Rounded.List, contentDescription = "", modifier = Modifier
                                .size(30.dp)
                                .align(Alignment.CenterHorizontally)
                        )
                        Text(text = "VIEW REPORTS", fontSize = 12.sp)
                    }
                    Spacer(modifier = Modifier.size(20.dp))
                    Column(modifier = Modifier.clickable {  }) {
                        Icon(
                            imageVector = Icons.Rounded.Call, contentDescription = "", modifier = Modifier
                                .size(30.dp)
                                .align(Alignment.CenterHorizontally)
                        )
                        Text(text = "EMERGENCY CALL", fontSize = 12.sp)
                    }
                }
            }
        }) { innerPadding ->
            Column {
                Spacer(modifier = Modifier.size(10.dp))
                var current = LocalContext.current

                Column(
                    modifier = Modifier.fillMaxWidth(),
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Icon(
                        imageVector = Icons.Rounded.Face, contentDescription = "", modifier = Modifier.size(80.dp)
                    )
                    Text(text = "USER NAME", fontSize = 25.sp)
                }
                Spacer(modifier = Modifier.size(20.dp))

                Column (horizontalAlignment = Alignment.CenterHorizontally, verticalArrangement = Arrangement.Center) {
                    Card(
                        modifier = Modifier
                            .size(400.dp,350.dp),
                        colors = CardDefaults.cardColors(containerColor = Color.White),
                        border = BorderStroke(2.dp, Color.LightGray),
                        shape = RoundedCornerShape(15.dp),
                    ) {
                        Column {
                            ProfileText(description = "First Name", value = "User name")
                            ProfileText(description = "Last Name", value = "Sirname")
                            ProfileText(description = "Email address", value = "emailInForm@gmail.com")
                            ProfileText(description = "Phone number", value = "+27 68801025")
                        }
                    }

                    Spacer(modifier = Modifier.size(50.dp))
                    BottomButtonsMyProfile(name = "UPDATE")
                    { showDialog = true }
                    Spacer(modifier = Modifier.size(20.dp))
                    BottomButtonsMyProfile(name = "Log Out") {
                        navController.navigate("Login") }
                }
            }
        }
    }

    if (showDialog) {
        ConfirmationDialog(
            title = "Confirm Update",
            message = "Are you sure you want to update your profile?",
            onConfirm = {
                registrationMessage = "Profile updated successfully!"
                showDialog = false
            },
            onDismiss = {
                showDialog = false
            }
        )
    }

    if (registrationMessage.isNotEmpty()) {
        Toast.makeText(LocalContext.current, registrationMessage, Toast.LENGTH_LONG).show()
        registrationMessage = ""
    }
}

@Composable
fun ConfirmationDialog(
    title: String,
    message: String,
    onConfirm: () -> Unit,
    onDismiss: () -> Unit
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text(text = title) },
        text = { Text(text = message) },
        confirmButton = {
            Button(
                onClick = onConfirm,
                colors = ButtonDefaults.buttonColors(Color.Green)
            ) {
                Text("Confirm", color = Color.Black)
            }
        },
        dismissButton = {
            Button(
                onClick = onDismiss,
                colors = ButtonDefaults.buttonColors(Color.Green)
            ) {
                Text("Cancel", color = Color.Black)
            }
        }
    )
}

@Preview
@Composable
fun SignUpPreview() {
    UpdateProfile(navController = NavController(LocalContext.current))
}
