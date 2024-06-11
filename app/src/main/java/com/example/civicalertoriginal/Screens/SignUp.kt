package com.example.civicalertoriginal.Screens

import android.widget.Toast
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
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
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.civicalertoriginal.Components.BottomButtons
import com.example.civicalertoriginal.Components.InstructionText
import com.example.civicalertoriginal.Components.SignUpText
import com.example.civicalertoriginal.Components.TextFields
import com.google.firebase.Firebase
import com.google.firebase.database.database

@Composable
fun SignUp (navController: NavController){

    val database = Firebase.database
    val myRef = database.getReference()
    val context = LocalContext.current
    val scrollable = rememberScrollState()


    Surface(color = Color.White){
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(20.dp)
                .verticalScroll(scrollable),
            verticalArrangement = Arrangement.spacedBy(18.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            var name1 by remember { mutableStateOf("") }
            var surname by remember { mutableStateOf("") }
            var email by remember { mutableStateOf("") }
            var contact by remember { mutableStateOf("") }
            var pass by remember { mutableStateOf("") }
            var confirmPass by remember { mutableStateOf("") }

            Spacer(modifier = Modifier.size(12.dp))

            InstructionText(value = "Sign Up")

            Spacer(modifier = Modifier.size(12.dp))

            TextFields(value = name1, onChange = { name1 = it }, fieldLabel = "Name")
            TextFields(value = surname, onChange = { surname = it }, fieldLabel = "Surname")
            TextFields(value = email, onChange = { email = it }, fieldLabel = "Email")
            TextFields(value = contact, onChange = { contact = it }, fieldLabel = "Contact Details")
            TextFields(value = pass, onChange = { pass = it }, fieldLabel = "Password")
            TextFields(
                value = confirmPass,
                onChange = { confirmPass = it },
                fieldLabel = "Confirm Password"
            )

            Row {
                SignUpText(value = "Do you give us permission to use your details for marketing purposes")
            }
            Row {
                SignUpText(value = "Do you agree to Term & Conditions of the app")
            }
            val newUser = hashMapOf(
                "name" to name1,
                "surname" to surname,
                "email Address" to email,
                " Contact Details" to contact,
                "password" to pass
            )
            val match = pass == confirmPass

            BottomButtons(name = "SIGN UP", onClick = {
                if (match) {
                    myRef.push().setValue(newUser).addOnCompleteListener { task ->
                        if (task.isSuccessful) {
                            Toast.makeText(context, "User registered successfully", Toast.LENGTH_SHORT).show()
                            // Navigate to another screen if needed
                            // navController.navigate("NextScreen")
                        } else {
                            Toast.makeText(context, "Registration failed: ${task.exception?.message}", Toast.LENGTH_SHORT).show()
                        }
                    }
                } else {
                    Toast.makeText(context, "Passwords do not match", Toast.LENGTH_SHORT).show()
                }
            navController.navigate("makeReports")} )
        }
    }

}