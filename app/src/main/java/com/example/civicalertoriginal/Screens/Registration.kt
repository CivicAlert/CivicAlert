package com.example.civicalertoriginal.Screens

import android.annotation.SuppressLint
import android.util.Patterns
import android.widget.Toast
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
import androidx.compose.material3.Checkbox
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
import androidx.navigation.compose.rememberNavController
import com.example.civicalertoriginal.Components.EmailTextFields
import com.example.civicalertoriginal.Components.InstructionText
import com.example.civicalertoriginal.Components.LogBottomButtons
import com.example.civicalertoriginal.Components.NumberTextFields
import com.example.civicalertoriginal.Components.PasswordTextFields
import com.example.civicalertoriginal.Components.SignUpText
import com.example.civicalertoriginal.Components.TextFields
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase


data class User(
    val firstName: String = "",
    val lastName: String = "",
    val email: String ="",
    val phoneNumber: String="",
    val password: String=""
)

@SuppressLint("SuspiciousIndentation")
@Composable
fun Registration(navController: NavController) {

    val context = LocalContext.current
    val scrollable = rememberScrollState()
    val database = Firebase.database
    val myRef = database.getReference("Community members")
    val auth = FirebaseAuth.getInstance();

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
    val passwordPattern = Regex("^(?=.*[A-Za-z])(?=.*\\d)(?=.*[@\$!%*#@?&])[A-Za-z\\d@\$!%*#?@&]{8,}$")

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
   fun encodeEmail(email: String): String {
        return email.replace(".", "_dot_") // Replace '.' with '_dot_' or use any suitable encoding method
    }
    fun checkEmailExists(email:String, onResult:(Boolean)-> Unit){
        val formattedEmail = encodeEmail(email)
        val myRef = Firebase.database.getReference("Community members").child(formattedEmail)

        myRef.addListenerForSingleValueEvent(object : ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                if (snapshot.exists()){
                    onResult(true)
                }else{
                    onResult(false)
                }
            }

            override fun onCancelled(error: DatabaseError) {
                onResult(false)
                println("Failed to check user")
            }
        })
    }

    fun saveUser(user: User) {
       //val userId = user.email
        val userId = encodeEmail(user.email)
       // val userId = user.firstName
        myRef.child(userId).setValue(user).addOnCompleteListener { task ->
            if (task.isSuccessful) {
                // Handle success
                registrationMessage = "Successfully registered!"
                println("User saved successfully")
            } else {
                // Handle failure
                task.exception?.let {
                    registrationMessage = "Error saving user: ${it.message}"
                    println("Error saving user: ${it.message}")
                }
            }
        }
    }
    fun saveByEmail() {
        auth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    // Send verification email
                    auth.currentUser?.sendEmailVerification()
                        ?.addOnCompleteListener { emailTask ->
                            if (emailTask.isSuccessful) {
                                Toast.makeText(
                                    context,
                                    "Please check your email inbox to verify your email address.",
                                    Toast.LENGTH_LONG
                                ).show()
                            } else {
                                Toast.makeText(
                                    context,
                                    "Error: Failed to send verification email: ${emailTask.exception?.message}",
                                    Toast.LENGTH_LONG
                                ).show()
                            }
                        }
                } else {
                    // Handle registration failure
                    Toast.makeText(
                        context,
                        "Registration failed: ${task.exception?.message}",
                        Toast.LENGTH_LONG
                    ).show()
                }
            }
    }

    fun registerUser() {
        checkEmailExists(email) { exists ->
            if (exists) {
                registrationMessage = "Email already exists in the system"
            } else {
                val user = User(
                    email = email,
                    firstName = firstName,
                    lastName = lastName,
                    phoneNumber = phoneNumber,
                    password = password
                )
                saveUser(user)
                saveByEmail()
            }
        }
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

            Row(
                modifier = Modifier.padding(end = 20.dp)
            ) {
                SignUpText(value = "Do you give us permission to use your details for marketing purposes")
            }
            Row(modifier = Modifier.padding(end = 20.dp)) {
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
                        onClick = { showDialog = false
                        registerUser()
                        }
                    ) {
                        Text("Confirm", color = Color.Black)
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
                            navController.navigate("Login")
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
