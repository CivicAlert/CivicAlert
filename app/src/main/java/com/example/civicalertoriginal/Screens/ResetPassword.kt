package com.example.civicalertoriginal.Screens

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
import com.example.civicalertoriginal.Components.BottomButtons
import com.example.civicalertoriginal.Components.EmailTextFields
import com.example.civicalertoriginal.Components.InstructionText
import com.example.civicalertoriginal.Components.LogAndForgotHeader
import com.google.firebase.auth.FirebaseAuth
import java.util.regex.Pattern

@Composable
fun ForgotPassword(navController: NavController) {
    val context = LocalContext.current
    var email by remember { mutableStateOf("") }
    var isEmailValid by remember { mutableStateOf(true) }
    val auth = FirebaseAuth.getInstance()

    Surface(color = Color.White) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(45.dp),
            modifier = Modifier
                .fillMaxSize()
                .padding(25.dp)
        ) {

            Spacer(modifier = Modifier.size(20.dp))

            LogAndForgotHeader(screenLabel = "Forgot Password?")

            InstructionText(value = "Enter your email to recover your password")

            // Email validation function
            fun validateEmail(email: String): Boolean {
                val emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+"
                return Pattern.matches(emailPattern, email)
            }

            // Email text field
            EmailTextFields(value = email, onChange = {
                email = it
                isEmailValid = validateEmail(email)
            }, fieldLabel = "Enter Email Address")

            if (!isEmailValid && email.isNotEmpty()) {
                InstructionText(value = "Please enter a valid email", )
            }

            Spacer(modifier = Modifier.size(10.dp))

            // Reset button
            BottomButtons(name = "Reset") {
                if (isEmailValid && email.isNotEmpty()) {
                    auth.sendPasswordResetEmail(email)
                        .addOnCompleteListener { task ->
                            if (task.isSuccessful) {
                                Toast.makeText(
                                    context,
                                    "Email has been sent to $email",
                                    Toast.LENGTH_SHORT
                                ).show()
                                navController.navigate("logIn") // Navigate back to login screen
                            } else {
                                Toast.makeText(
                                    context,
                                    "Error: ${task.exception?.message}",
                                    Toast.LENGTH_SHORT
                                ).show()
                            }
                        }
                } else {
                    Toast.makeText(
                        context,
                        "Please enter a valid email address",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }

        }
    }
}

@Preview
@Composable
fun ResetPasswordPreview() {
    val navController = rememberNavController()
    ForgotPassword(navController)
}
