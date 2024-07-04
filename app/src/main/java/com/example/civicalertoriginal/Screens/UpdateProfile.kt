package com.example.civicalertoriginal.Screens

import android.annotation.SuppressLint
import android.widget.Toast
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Call
import androidx.compose.material.icons.rounded.Edit
import androidx.compose.material.icons.rounded.Face
import androidx.compose.material.icons.rounded.Home
import androidx.compose.material.icons.rounded.List
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshots.SnapshotStateList

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
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

data class getUser(
    var email: String = "",
    var firstName: String = "",
    var lastName: String = "",
    var phoneNumber: String = ""
)


@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter", "UnrememberedMutableState")
@Composable
fun UpdateProfile (navController: NavController){
    Surface( color = Color.White) {
        Scaffold (bottomBar = {
            BottomAppBar {
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
                        label = "Emergency\nContact",
                        onClick = { navController.navigate("emergencyContacts") }
                    )
                }
            }
        }){innerPadding ->
        }
        var name by remember { mutableStateOf("") }
        var sirname by remember { mutableStateOf("") }
        var phoneNumber by remember { mutableStateOf("") }
        var email by remember { mutableStateOf("") }
        val showDialog = mutableStateOf(false)
        val context = LocalContext.current
            Column {
                Spacer(modifier = Modifier.size(10.dp))
                Column ( modifier = Modifier.fillMaxWidth() ,
                    verticalArrangement = Arrangement.Center, horizontalAlignment = Alignment.CenterHorizontally){
                    Icon(imageVector = Icons.Rounded.Face, contentDescription = "", modifier = Modifier.size(80.dp
                    ))
                    Text(text = "USER NAME", fontSize = 25.sp)
                    Spacer(modifier = Modifier.size(10.dp))
                Card (   modifier = Modifier
                    .fillMaxWidth(),
                    colors = CardDefaults.cardColors(containerColor = Color.White),
                    elevation = CardDefaults.cardElevation(defaultElevation = 12.dp) ){

                    fun sanitizeEmail(email: String): String {
                        return email.replace(".", "_dot_")
                    }

                    fun getUserDetails(email:String) {
                        val modifiedEmail = sanitizeEmail(email)
                        val database = FirebaseDatabase.getInstance()
                        val myRef = database.getReference("Community members").child(modifiedEmail)
                        myRef.addValueEventListener(object : ValueEventListener {
                            @SuppressLint("SuspiciousIndentation")
                            override fun onDataChange(dataSnapshot: DataSnapshot) {
                            val user = dataSnapshot.getValue(User::class.java)
                                user?.let {
                                    name = it.firstName
                                    sirname = it.lastName
                                    phoneNumber = it.phoneNumber
                                }
                        }

                            override fun onCancelled(databaseError: DatabaseError) {
                                // Handle possible errors
                              //  callback(null)
                            }
                        })

                    }
                    LaunchedEffect(Unit) {
                        val currentUser = FirebaseAuth.getInstance().currentUser
                        val currentUserEmail = currentUser?.email

                        if (currentUserEmail != null) {
                            email = currentUserEmail
                            getUserDetails(currentUserEmail)
                        }
                    }
                    fun showToast(message: String) {
                        Toast.makeText(context, message, Toast.LENGTH_LONG).show()
                    }
                    fun updateDetails() {
                        val database = FirebaseDatabase.getInstance()
                        val currentUser = FirebaseAuth.getInstance().currentUser
                        val currentUserEmail = currentUser?.email

                        if (currentUserEmail != null) {
                            val sanitizedEmail = sanitizeEmail(currentUserEmail)
                            val myRef = database.getReference("Community members").child(sanitizedEmail)
                            val updateUser = User(
                                firstName = name,
                                lastName = sirname,
                                email = currentUserEmail,
                                phoneNumber = phoneNumber
                            )
                            myRef.setValue(updateUser).addOnSuccessListener {
                                showToast("Details have been updated")
                            }
                                .addOnFailureListener{e ->
                                    showToast("Failed to update")
                                }
                        }
                    }

                    if (showDialog.value) {
                        AlertDialog(
                            onDismissRequest = {
                                showDialog.value = false
                            },
                            title = { Text("Confirm Update") },
                            text = { Text("Are you sure you want to update your details?") },
                            confirmButton = {
                                Button(
                                    onClick = {
                                        showDialog.value = false
                                        updateDetails()

                                    }
                                ) {
                                    Text("Update")
                                }
                            },
                            dismissButton = {
                                Button(
                                    onClick = {
                                        showDialog.value = false
                                    }
                                ) {
                                    Text("Cancel")
                                }
                            }
                        )
                    }
                    Column {
                        var user by remember {
                            mutableStateOf(User())
                        }

                       // getUserDetails(user)
                        ProfileText(description = "First Name", value = name, onSave = {updateLastName -> name = updateLastName})
                        //Text(text = "Halo $name")
                        Spacer(modifier = Modifier.size(10.dp))
                        ProfileText(description = "Last Name", value = sirname, onSave = {updateLastName -> sirname = updateLastName})
                        Spacer(modifier = Modifier.size(10.dp))
                        ProfileText(description = "Email address", value = email, onSave = {updatePhone -> email = updatePhone})
                        Spacer(modifier = Modifier.size(10.dp))
                        ProfileText(description = "Phone number", value = phoneNumber, onSave = {updatePhone -> phoneNumber = updatePhone})

                    } }


                Spacer(modifier = Modifier.size(20.dp))
                BottomButtonsMyProfile(name = "UPDATE") {/*showDialog.value=  true*/}
                Spacer(modifier = Modifier.size(20.dp))
                BottomButtonsMyProfile(name = "Log Out") {}
            }
        }



    }
}

