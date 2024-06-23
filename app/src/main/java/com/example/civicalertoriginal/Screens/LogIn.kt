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
import com.example.civicalertoriginal.Components.EmailTextFields
import com.example.civicalertoriginal.Components.InstructionText
import com.example.civicalertoriginal.Components.LogAndForgotHeader
import com.example.civicalertoriginal.Components.PasswordTextFields
import com.example.civicalertoriginal.Components.SignUpBottomButtons
import com.example.civicalertoriginal.R

import java.util.regex.Pattern


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

            Spacer(modifier = Modifier.padding(10.dp))

            fun validatePassword(password:String):Boolean{
                val passwordPattern = "[a-zA-Z0-9]"
               return  Pattern.matches(passwordPattern,password)
            }
            fun validateEmail(email: String): Boolean {
                val emailPattern = "[a-zA-Z0-9_]+@+\\.[a-zA-Z]"
                return Pattern.matches(emailPattern, email)
            }
            //matches the alphas numerics, matches the @, matches alpha numeric, matches atleast two charecters




            EmailTextFields(value = email, onChange = {
                if (email.length<=100){
                    email=it
                    validateEmail(email)
                }                                     },
                fieldLabel = "Enter Email Address" )


            Spacer(modifier = Modifier.size(10.dp))
            PasswordTextFields(value = password, onChange = {

                if (password.length<9){
                    password=it
                    validatePassword(password)
                }

             },
                fieldLabel = "Enter your password")

            Spacer(modifier = Modifier.padding(6.dp))

            Row( verticalAlignment = Alignment.Bottom,
                horizontalArrangement = Arrangement.SpaceBetween, modifier = Modifier
                    .padding(20.dp)
                    .align(Alignment.CenterHorizontally)

            ) {

              Text(text = "Forgot Password?", modifier = Modifier
                  .clickable { navController.navigate("ForgotPassword") }
                  .padding(end = 25.dp),
                    style = TextStyle(
                        fontStyle = FontStyle.Normal,
                        color = Color.Blue
                    )
                )
                Spacer(modifier = Modifier.size(15.dp))

                Text(text = "Register", modifier = Modifier
                    .clickable { navController.navigate("registration") }
                    .padding(start = 25.dp),
                    style = TextStyle( fontStyle = FontStyle.Normal,
                        color = Color.Blue)
                )

            }

            Spacer(modifier = Modifier.size(50.dp))
            InstructionText(value = "Sign with google")

                Image( modifier = Modifier
                    .size(50.dp,55.dp)
                    .clickable {  },painter = painterResource(id = R.drawable.googlepic),
                    contentDescription = "Google SignIn" )

            Spacer(modifier = Modifier.size(18.dp))

            SignUpBottomButtons(
                name = "Sign in",
                onClick = {
                    Toast.makeText(context, "Sign in Successful", Toast.LENGTH_SHORT).show()
                    navController.navigate("dashboard")
                }
            )

        }
    }
}

@Preview
@Composable
fun LogInPreview(){
    val navController = rememberNavController()
    LogIn( navController)
}