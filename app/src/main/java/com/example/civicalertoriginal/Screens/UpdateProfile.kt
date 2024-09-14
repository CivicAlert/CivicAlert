package com.example.civicalertoriginal.Screens

import android.annotation.SuppressLint
import android.content.Context
import android.widget.Toast
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.*
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
import com.example.civicalertoriginal.Components.BottomButtonsMyProfile
import com.example.civicalertoriginal.Components.ProfileText
import com.example.civicalertoriginal.Components.UpdateProfileButton
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*


data class getUser(
    var email: String = "",
    var firstName: String = "",
    var lastName: String = "",
    var phoneNumber: String = ""
)

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun UpdateProfile(navController: NavController) {
    val context = LocalContext.current

    Surface(color = Color.White) {
        Scaffold(bottomBar = {
            BottomAppBar(
                containerColor = Color.White,
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceAround
                ) {
                    BottomNavItem(
                        icon = Icons.Rounded.Home,
                        label = "Home",
                        onClick = { navController.navigate("Dashboard") }
                    )
                    BottomNavItem(
                        icon = Icons.Rounded.Edit,
                        label = "Make report",
                        onClick = { navController.navigate("makeReports") }
                    )
                    BottomNavItem(
                        icon = Icons.Rounded.List,
                        label = "View reports",
                        onClick = { navController.navigate("Viewreports") }
                    )
                    BottomNavItem(
                        icon = Icons.Rounded.Call,
                        label = "Emergency\n Contact",
                        onClick = { navController.navigate("emergencyContacts") }
                    )
                }
            }
        }) { innerPadding ->
            var user by remember { mutableStateOf(User()) }
            var email by remember { mutableStateOf("") }

            LaunchedEffect(Unit) {
                val currentUser = FirebaseAuth.getInstance().currentUser
                val currentUserEmail = currentUser?.email

                if (currentUserEmail != null) {
                    email = currentUserEmail
                    getUserDetails(currentUserEmail) { fetchedUser ->
                        fetchedUser?.let {
                            user = it
                        }
                    }
                }
            }

            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(start = 16.dp, end = 16.dp)
            ) {
                Spacer(modifier = Modifier.size(10.dp))
                Column(
                    modifier = Modifier.fillMaxWidth(),
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Icon(
                        imageVector = Icons.Rounded.Face,
                        contentDescription = "",
                        modifier = Modifier.size(80.dp)
                    )
                    Text(text = "Hi ${user.firstName}", fontSize = 25.sp)
                    Spacer(modifier = Modifier.size(10.dp))

                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        colors = CardDefaults.cardColors(containerColor = Color.White),
                        elevation = CardDefaults.cardElevation(defaultElevation = 12.dp)
                    ) {
                        Column {
                            ProfileText(description = "First Name", value = user.firstName) { updatedValue ->
                                user = user.copy(firstName = updatedValue)
                            }
                            Spacer(modifier = Modifier.size(10.dp))
                            ProfileText(description = "Last Name", value = user.lastName) { updatedValue ->
                                user = user.copy(lastName = updatedValue)
                            }
                            Spacer(modifier = Modifier.size(10.dp))
                            ProfileText(description = "Email address", value = email, onSave = {})
                            Spacer(modifier = Modifier.size(10.dp))
                            ProfileText(description = "Phone number", value = user.phoneNumber) { updatedValue ->
                                user = user.copy(phoneNumber = updatedValue)
                            }
                        }
                    }
                }

                Spacer(modifier = Modifier.size(20.dp))
                UpdateProfileButton(name = "UPDATE") {
                    updateDetails(user) {
                        Toast.makeText(context, "Update successful", Toast.LENGTH_SHORT).show()
                    }
                }
                Spacer(modifier = Modifier.size(20.dp))
                BottomButtonsMyProfile(name = "Log Out") {
                    FirebaseAuth.getInstance().signOut()
                    navController.navigate("Login") {
                        popUpTo(navController.graph.startDestinationId) {
                            inclusive = true
                        }
                    }
                }
            }
        }
    }
}

fun sanitizeEmail(email: String): String {
    return email.replace(".", "_dot_")
}

fun getUserDetails(email: String, callback: (User?) -> Unit) {
    val modifiedEmail = sanitizeEmail(email)
    val database = FirebaseDatabase.getInstance()
    val myRef = database.getReference("Community members").child(modifiedEmail)
    myRef.addValueEventListener(object : ValueEventListener {
        override fun onDataChange(dataSnapshot: DataSnapshot) {
            val user = dataSnapshot.getValue(User::class.java)
            callback(user)
        }

        override fun onCancelled(databaseError: DatabaseError) {
            // Handle possible errors
            callback(null)
        }
    })
}

fun updateDetails(user: User, onSuccess: () -> Unit) {
    val modifiedEmail = sanitizeEmail(user.email)
    val database = FirebaseDatabase.getInstance()
    val myRef = database.getReference("Community members").child(modifiedEmail)
    myRef.setValue(user).addOnSuccessListener {
        onSuccess()
    }
}