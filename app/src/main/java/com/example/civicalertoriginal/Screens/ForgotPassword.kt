package com.example.civicalertoriginal.Screens

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
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
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.civicalertoriginal.Components.BottomButtons
import com.example.civicalertoriginal.Components.InstructionText
import com.example.civicalertoriginal.Components.LogAndForgotHeader
import com.example.civicalertoriginal.Components.TextFields

@Composable
fun ForgotPassword (navController: NavController){
    var email by remember {
        mutableStateOf("")
    }
    Surface (color = Color.White) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(45.dp),
            modifier = Modifier
                .fillMaxSize()
                .padding(25.dp)
        ) {

            Spacer(modifier = Modifier.size(20.dp))

            LogAndForgotHeader(screenLabel = "Forgot Password?")

            InstructionText(value = "Enter your email to recover your password" )

            TextFields(
                value = email,
                onChange = {email= it },
                fieldLabel = "Enter Email"
            )

            // Reset button that navigates to the login screen
            BottomButtons(name = "RESET") { navController.navigate("logIn")}
            Text(text = "Go back to LogIn", modifier = Modifier
                .clickable { navController.navigate("logIn")}
                .align(Alignment.CenterHorizontally))

        }
    }


}