package com.example.civicalertoriginal.Screens

import android.util.Patterns
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.civicalertoriginal.Components.*
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
    var isEmailValid by remember { mutableStateOf(true) }
    var isPasswordValid by remember { mutableStateOf(true) }
    var hasUpperCase by remember { mutableStateOf(false) }
    var hasDigit by remember { mutableStateOf(false) }
    var hasSymbol by remember { mutableStateOf(false) }
    var hasMinLength by remember { mutableStateOf(false) }

    // Character limit in text fields
    val maxName = 50
    val maxEmail = 100
    val maxNumber = 10
    val maxPassword = 100

    // Regex patterns
    val emailPattern = Patterns.EMAIL_ADDRESS.toRegex()
    val passwordPattern = Regex("^(?=.*[A-Za-z])(?=.*\\d)(?=.*[@\$!%*#?&])[A-Za-z\\d@\$!%*#?&]{8,}$")

    // Validate entered details
    fun validateForm() {
        isEmailValid = emailPattern.matches(email)
        isPasswordValid = passwordPattern.matches(password)
        hasUpperCase = password.any { it.isUpperCase() }
        hasDigit = password.any { it.isDigit() }
        hasSymbol = password.any { !it.isLetterOrDigit() }
        hasMinLength = password.length >= 8

        isFormValid = firstName.all { it.isLetter() } && firstName.isNotEmpty() && firstName.length <= maxName &&
                lastName.all { it.isLetter() } && lastName.isNotEmpty() && lastName.length <= maxName &&
                email.isNotEmpty() && email.length <= maxEmail && isEmailValid &&
                phoneNumber.all { it.isDigit() } && phoneNumber.length == maxNumber &&
                password.isNotEmpty() && password.length <= maxPassword && isPasswordValid &&
                confirmPassword.isNotEmpty() && confirmPassword == password
    }
    fun saveUser(){
        DatabaseConnection().getUserDetail(firstName,lastName,email,password,phoneNumber);
        DatabaseConnection().saveUserByEmail(context)
    }

    Surface(color = Color.White) {
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

            if (!isEmailValid && email.isNotEmpty()) {
                Text(
                    text = "Please enter a valid email address",
                    color = Color.Red
                )
            }

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

            if (password.isNotEmpty())Column (

            ){
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Checkbox(checked = hasUpperCase, onCheckedChange = null)
                    Text(text = "Must have at least one capital letter"
                    , fontSize = 12.sp)
                }
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Checkbox(checked = hasDigit, onCheckedChange = null)
                    Text(text = "Must have at least one digit",fontSize = 12.sp)
                }
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Checkbox(checked = hasSymbol, onCheckedChange = null)
                    Text(text = "Must have at least one symbol",fontSize = 12.sp)
                }
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Checkbox(checked = hasMinLength, onCheckedChange = null)
                    Text(text = "Must have at least 8 characters",fontSize = 12.sp)
                }
            }

            PasswordTextFields(value = confirmPassword,
                onChange = {
                    if (it.length <= maxPassword) {
                        confirmPassword = it
                        passwordsMatch = confirmPassword == password
                        validateForm()
                    }
                }, fieldLabel = "Confirm password"
            )

            if (!isPasswordValid && password.isNotEmpty()) {
                Text(
                    text = "Enter a valid password",
                    color = Color.Red
                )
            }

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
                SignUpText(value = "Do you agree to Terms & Conditions of the app")
            }

            Spacer(modifier = Modifier.size(16.dp))

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
                    Button(modifier = Modifier
                        .padding(start = 20.dp)
                        .width(100.dp),
                        colors = ButtonDefaults.buttonColors(Color.Green),
                        onClick = {
                            saveUser()
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
                    Button(modifier = Modifier
                        .padding(end = 20.dp)
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
