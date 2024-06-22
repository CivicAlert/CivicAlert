package com.example.Civicalert.Screens

import android.widget.Toast
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Surface
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
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.Civicalert.Components.BottomButtons
import com.example.Civicalert.Components.EmailTextFields
import com.example.Civicalert.Components.InstructionText
import com.example.Civicalert.Components.LogAndForgotHeader
import java.util.regex.Pattern

@Composable
fun ForgotPassword (navController: NavController){
    val context = LocalContext.current
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
            fun validateEmail(email: String): Boolean {
                val emailPattern = "[a-zA-Z0-9_]+@+\\.[a-zA-Z]"
                return Pattern.matches(emailPattern, email)
            }

           EmailTextFields(value = email, onChange = {if (email.length<100){
               validateEmail(email)
           } }, fieldLabel = "Enter your email ")

            
            Spacer(modifier = Modifier.size(10.dp))
            
            BottomButtons(name = "Reset") { Toast.makeText(context,"Email has been sent to $email",Toast.LENGTH_SHORT).show()
            navController.navigate("logIn")}
            

        }
    }
    
}
@Preview
@Composable
fun ResetPasswordPreview(){
    val navController = rememberNavController()
    ForgotPassword(navController)
}