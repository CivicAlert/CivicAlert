package com.example.civicalertoriginal.Screens

import android.util.Patterns
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
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
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.civicalertoriginal.Components.EmailTextFields
import com.example.civicalertoriginal.Components.InstructionText
import com.example.civicalertoriginal.Components.LogBottomButtons
import com.example.civicalertoriginal.Components.NumberTextFields
import com.example.civicalertoriginal.Components.PasswordTextFields
import com.example.civicalertoriginal.Components.SignUpText
import com.example.civicalertoriginal.Components.TextFields
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase

@Composable
fun Registration(navController: NavController) {

    val database = Firebase.database
    val myRef = database.getReference()
    val context = LocalContext.current
    val scrollable = rememberScrollState()

    // Variables needed for user registration
    var firstName by remember { mutableStateOf("") }
    var lastName by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var phoneNumber by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var confirmPassword by remember { mutableStateOf("") }
    var passwordsMatch by remember { mutableStateOf(true) }
    var isFormValid by remember { mutableStateOf(false) }
    var showDialog by remember { mutableStateOf(false) }
    var registrationMessage by remember { mutableStateOf("") }

    // Character limit in text fields
    val maxName = 50
    val maxEmail = 100
    val maxNumber = 10
    val maxPassword = 100

    // Validate entered details
    fun validateForm() {
        isFormValid = firstName.all { it.isLetter() } && firstName.isNotEmpty() && firstName.length <= maxName &&
                lastName.all { it.isLetter() } && lastName.isNotEmpty() && lastName.length <= maxName &&
                email.isNotEmpty() && email.length <= maxEmail &&
                phoneNumber.all { it.isDigit() } && phoneNumber.length == maxNumber &&
                password.isNotEmpty() && password.length <= maxPassword &&
                 confirmPassword.isNotEmpty() && confirmPassword == password
    }

    Surface {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(20.dp)
                .verticalScroll(scrollable),
            verticalArrangement = Arrangement.spacedBy(18.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            Spacer(modifier = Modifier.size(12.dp))

            InstructionText(value = "Sign Up")

            Spacer(modifier = Modifier.size(12.dp))

            TextFields(value = firstName,
                onChange = {
                    if (it.length <= maxName) {
                        firstName = it
                        validateForm()
                    }
                }, fieldLabel = "First name"
            )

            TextFields(value = lastName,
                onChange = {
                    if (it.length <= maxName) {
                        lastName = it
                        validateForm()
                    }
                }, fieldLabel = "Last name"
            )

            EmailTextFields(value = email,
                onChange = {
                    if (it.length <= maxEmail) {
                        email = it
                        validateForm()
                    }
                },
                fieldLabel = "Email Address"
            )

            NumberTextFields(value = phoneNumber,
                onChange = {
                    if (it.length <= maxNumber) {
                        phoneNumber = it
                        validateForm()
                    }
                }, fieldLabel = "Phone number"
            )

            PasswordTextFields(value = password,
                onChange = {
                    if (it.length <= maxPassword) {
                        password = it
                        passwordsMatch = confirmPassword == password
                        validateForm()
                    }
                }, fieldLabel = "Password"
            )

            PasswordTextFields(value = confirmPassword,
                onChange = {
                    if (it.length <= maxPassword) {
                        confirmPassword = it
                       passwordsMatch = confirmPassword == password
                        validateForm()
                    }
                }, fieldLabel = "Confirm password"
            )
            if (!passwordsMatch) {
                Text(
                    text = "Passwords do not match",
                    color = Color.Red
                )
            }

            Row {
                SignUpText(value = "Do you give us permission to use your details for marketing purposes")
            }
            Row {
                SignUpText(value = "Do you agree to Term & Conditions of the app")
            }

            LogBottomButtons(
                name = "Register",
                onClick = { showDialog = true },
                enabled = isFormValid
            )

        }
        if (showDialog) {
            AlertDialog(
                onDismissRequest = { showDialog = false },
                title = { Text("Confirm Registration") },
                text = { Text("Are you sure you want to register with these details?") },
                confirmButton = {
                    Button(modifier = Modifier.padding(start = 20.dp)
                        .width(100.dp),
                        colors = ButtonDefaults.buttonColors(Color.Green),
                        onClick = {
                            // method to save data to database
                            registrationMessage = "Successfully registered!"
                            showDialog = false
                        }
                    ) {
                        Text("Confirm",
                            color = Color.Black)
                    }
                },
                dismissButton = {
                    Button(modifier = Modifier.padding(end = 20.dp)
                        .width(100.dp),
                        colors = ButtonDefaults.buttonColors(Color.Green),
                        onClick = { showDialog = false }) {
                        Text("Cancel",
                            color = Color.Black)
                    }
                }
            )
        }

        if (registrationMessage.isNotEmpty()) {
            AlertDialog(
                onDismissRequest = { registrationMessage = "" },
                title = { Text("Registration") },
                text = { Text(registrationMessage) },
                confirmButton = {
                    Button(colors = ButtonDefaults.buttonColors(Color.Green),
                        onClick = {
                            registrationMessage = ""
                            // Navigate to login page
                        }
                    ) {
                        Text("OK",
                            color = Color.Black)
                    }
                }
            )
        }
    }
}

@Preview
@Composable
fun RegistrationPreview() {
    val navController = rememberNavController()
    Registration(navController)
}
