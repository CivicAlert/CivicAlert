package com.example.civicalertoriginal.Screens

import android.annotation.SuppressLint
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.rounded.Call
import androidx.compose.material.icons.rounded.Edit
import androidx.compose.material.icons.rounded.Home
import androidx.compose.material.icons.rounded.List
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun HelpAndSupport(navController: NavController) {
    val questions = remember { mutableStateListOf<String>() } // To store the questions
    val database = FirebaseDatabase.getInstance().getReference("Question")

    LaunchedEffect(Unit) {
        database.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                questions.clear()
                for (data in snapshot.children) {
                    val question = data.child("Question").getValue(String::class.java)
                    if (question != null) {
                        questions.add(question)
                    }
                }
            }

            override fun onCancelled(error: DatabaseError) {
                // Handle error
            }
        })
    }

    Surface(
        color = Color.White, modifier = Modifier
            .fillMaxWidth()
            .padding(10.dp)
    ) {
        Scaffold(
            bottomBar = {
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
                            label = "Emergency\nContact",
                            onClick = { navController.navigate("emergencyContacts") }
                        )
                    }
                }
            }
        ) {
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = 16.dp),
                verticalArrangement = Arrangement.Top,
                horizontalAlignment = Alignment.Start
            ) {
                item {
                    Spacer(modifier = Modifier.size(20.dp))
                    Text(
                        text = "What do you need help with?",
                        fontWeight = FontWeight.Black,
                        fontSize = 20.sp
                    )
                    Spacer(modifier = Modifier.size(20.dp))
                    OutlinedTextField(
                        value = "",
                        onValueChange = {},
                        trailingIcon = {
                            Icon(
                                modifier = Modifier.size(35.dp, 35.dp),
                                imageVector = Icons.Default.Search,
                                contentDescription = "Search"
                            )
                        },
                        keyboardOptions = KeyboardOptions.Default,
                        textStyle = TextStyle(color = Color.Black),
                        modifier = Modifier
                            .height(50.dp)
                            .fillMaxWidth()
                            .background(Color.White)
                    )
                    Spacer(modifier = Modifier.size(40.dp))
                }

                // FAQs Section
                item {
                    Text(text = "FAQs", fontSize = 35.sp, fontWeight = FontWeight.Bold, fontFamily = FontFamily.Monospace)
                    Spacer(modifier = Modifier.size(15.dp))
                }

                items(questions.size) { index ->
                    val question = questions[index]
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 8.dp),
                        colors = CardDefaults.cardColors(containerColor = Color.White),
                        elevation = CardDefaults.cardElevation(8.dp)
                    ) {
                        Row(
                            modifier = Modifier
                                .padding(16.dp)
                                .fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text(text = question, fontWeight = FontWeight.Bold, modifier = Modifier.weight(1f))

                        }
                    }
                }

                item {
                    Spacer(modifier = Modifier.size(45.dp))
                    Text(text = "Support", fontSize = 20.sp)
                    Spacer(modifier = Modifier.size(20.dp))
                }

                item {
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .fillMaxHeight(),
                        colors = CardDefaults.cardColors(containerColor = Color.White),
                        elevation = CardDefaults.cardElevation(12.dp)
                    ) {
                        Column(
                            modifier = Modifier.padding(16.dp)
                        ) {
                            Text(
                                text = "Get help with managing your Account",
                                color = Color.Blue,
                                textDecoration = TextDecoration.Underline
                            )
                            Spacer(modifier = Modifier.size(15.dp))
                            Text(
                                text = "Having Technical issues",
                                color = Color.Blue,
                                textDecoration = TextDecoration.Underline
                            )
                        }
                    }
                }
            }
        }
    }
}

