package com.example.civicalertoriginal.Screens

import android.annotation.SuppressLint
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
import androidx.compose.material3.BottomAppBar
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
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.civicalertoriginal.Components.BottomButtonsMyProfile
import com.example.civicalertoriginal.Components.ProfileText
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


@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
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
        var name by remember {
            mutableStateOf("")
        }
        var sirname by remember {
            mutableStateOf("")
        }
        var phoneNumber by remember {
            mutableStateOf("")
        }
        var email by remember {
            mutableStateOf("")
        }

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
                    fun getUserDetails() {
                        val database = FirebaseDatabase.getInstance()
                        val myRef = database.getReference("Community members")
                        myRef.addValueEventListener(object : ValueEventListener {
                            override fun onDataChange(snapshot: DataSnapshot) {
                               // user.clear()
                                for (userSnapshot in snapshot.children) {
                                    val users = userSnapshot.getValue(getUser::class.java)
                                    if (users != null) {
                                       // user.add(users)
                                        name = users.firstName
                                        sirname = users.firstName
                                        email = users.email
                                        phoneNumber = users.phoneNumber
                                    }
                                }
                            }
                            fun updateDetails() {
                                val userId = myRef.push().key
                                val updateUser = User(
                                    firstName = name,
                                    lastName = sirname,
                                    email = email,
                                    phoneNumber = phoneNumber
                                )
                                if (userId != null) {
                                    myRef.child(userId).setValue(updateUser)
                                }

                            }

                            override fun onCancelled(error: DatabaseError) {
                                TODO("Not yet implemented")
                            }
                        })
                    }
                    LaunchedEffect(Unit) {
                        getUserDetails()
                    }
                    Column {
                        var user by remember {
                            mutableStateOf(User())
                        }

                       // getUserDetails(user)
                        ProfileText(description = "First Name", value = name, onSave = {
                        })
                        //Text(text = "Halo $name")
                        Spacer(modifier = Modifier.size(10.dp))
                        ProfileText(description = "Last Name", value = sirname, onSave = {updateLastName -> sirname = updateLastName})
                        Spacer(modifier = Modifier.size(10.dp))
                        ProfileText(description = "Email address", value = email, onSave = {updatePhone -> email = updatePhone})
                        Spacer(modifier = Modifier.size(10.dp))
                        ProfileText(description = "Phone number", value = phoneNumber, onSave = {updatePhone -> phoneNumber = updatePhone})

                    } }

                Spacer(modifier = Modifier.size(20.dp))
                BottomButtonsMyProfile(name = "UPDATE") {}
                Spacer(modifier = Modifier.size(20.dp))
                BottomButtonsMyProfile(name = "Log Out") {}
            }
        }



    }
}

