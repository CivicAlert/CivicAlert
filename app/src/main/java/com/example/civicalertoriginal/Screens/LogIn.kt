package com.example.civicalertoriginal.Screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
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
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.civicalertoriginal.Components.BottomButtons
import com.example.civicalertoriginal.Components.InstructionText
import com.example.civicalertoriginal.Components.LogAndForgotHeader
import com.example.civicalertoriginal.Components.TextFields
import com.example.civicalertoriginal.R

@Composable
fun LogIn(navController: NavController) {
    Surface(
        color = Color.White
    ) {
        Column (
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
                .padding(start = 16.dp, end = 16.dp, top = 80.dp)
                .fillMaxSize()

        ){
         LogAndForgotHeader(screenLabel = "Login")

            Spacer(modifier = Modifier.size(40.dp))
            var name by remember {
                mutableStateOf("")
            }
            var pass by remember {
                mutableStateOf("")
            }

            TextFields(value =name , onChange = { name = it}, fieldLabel = "Name or Email Address" )

            Spacer(modifier = Modifier.size(20.dp))

            TextFields(value = pass, onChange ={pass=it} , fieldLabel = "Password")

            Spacer(modifier = Modifier.padding(16.dp))

            BottomButtons(name = "LOG IN") {navController.navigate("makeReports")}

            Spacer(modifier = Modifier.padding(6.dp))

            Row( verticalAlignment = Alignment.Bottom,
                horizontalArrangement = Arrangement.SpaceBetween,) {

                Text(text = "Forgot Password?", modifier = Modifier
                    .clickable { navController.navigate("ForgotPassword") }
                    .padding(end = 25.dp),
                    style = TextStyle(
                        fontStyle = FontStyle.Normal
                    )
                )
                Spacer(modifier = Modifier.size(12.dp))

                Text(text = "Register", modifier = Modifier
                    .clickable {navController.navigate("signUp")}
                    .padding(start = 25.dp),
                    style = TextStyle( fontStyle = FontStyle.Normal)
                )
            }

            Spacer(modifier = Modifier.size(50.dp))
            InstructionText(value = "Sign with google")

                Image( modifier = Modifier
                    .size(50.dp,55.dp),painter = painterResource(id = R.drawable.googlepic),
                    contentDescription = "Google SignIn" )
        }
    }
}