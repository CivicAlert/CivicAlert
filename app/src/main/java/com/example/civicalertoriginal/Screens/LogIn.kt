package com.example.civicalertoriginal.Screens

import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Button
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
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.civicalertoriginal.Components.BottomButtons
import com.example.civicalertoriginal.Components.EmailTextFields
import com.example.civicalertoriginal.Components.InstructionText
import com.example.civicalertoriginal.Components.LogAndForgotHeader
import com.example.civicalertoriginal.Components.PasswordTextFields

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
            val context = LocalContext.current

            Spacer(modifier = Modifier.size(40.dp))
            var email by remember { mutableStateOf("") }
            var password by remember {mutableStateOf("")}


            Spacer(modifier = Modifier.padding(16.dp))
            EmailTextFields(value = email, onChange = {
                email = it },
                fieldLabel = "Enter Email Address" )

            Spacer(modifier = Modifier.size(10.dp))
            PasswordTextFields(value = password, onChange = {
                password=it
             },
                fieldLabel = "Enter your password")

            Spacer(modifier = Modifier.padding(6.dp))
            //ValidateEmail(email = email, password = password)

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
                    .clickable { navController.navigate("registration") }
                    .padding(start = 25.dp),
                    style = TextStyle( fontStyle = FontStyle.Normal)
                )
            }

            Spacer(modifier = Modifier.size(50.dp))
            InstructionText(value = "Sign with google")

                Image( modifier = Modifier
                    .size(50.dp,55.dp),painter = painterResource(id = R.drawable.googlepic),
                    contentDescription = "Google SignIn" )
            Spacer(modifier = Modifier.size(18.dp))
            if (email.length<=100 && email.all {it.isLetter() && password.length<=100&& email.all { it.isLetter() }}){
                BottomButtons(name = "Sign In") { navController.navigate("Dashboard")}
            }
            else
                Text(text = "You ave inserted incorrect details")
                Toast.makeText(context,"Check the entered details if are correct",Toast.LENGTH_SHORT)


        }
    }
}

@Preview
@Composable
fun LogInPreview(){
    val navController = rememberNavController()
    LogIn( navController)
}