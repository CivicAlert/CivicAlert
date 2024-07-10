package com.example.civicalertoriginal.Screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.civicalertoriginal.Components.CardButton
import com.example.civicalertoriginal.Components.Logo
import com.example.civicalertoriginal.R
import com.google.firebase.database.*
import kotlinx.coroutines.tasks.await

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Dashboard(navController: NavController) {
    var recentReport by remember { mutableStateOf<Report?>(null) }

    LaunchedEffect(Unit) {
        recentReport = fetchRecentReport()
    }

    Surface(color = Color.White) {
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(start = 20.dp, end = 20.dp, bottom = 20.dp, top = 12.dp),
            verticalArrangement = Arrangement.spacedBy(18.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            item {
                Row(
                    horizontalArrangement = Arrangement.SpaceBetween,
                    modifier = Modifier
                        .height(80.dp)
                        .fillMaxWidth()
                ) {
                    Logo()
                    Image(
                        painter = painterResource(id = R.drawable.profie),
                        contentDescription = "",
                        modifier = Modifier
                            .size(50.dp, 70.dp)
                            .clickable { navController.navigate("userProfile") }
                    )
                }
            }

            recentReport?.let { report ->
                item {
                    Column(
                        modifier = Modifier.height(300.dp)
                    ) {
                        Card(
                            modifier = Modifier
                                .fillMaxWidth()
                                .fillMaxHeight(),
                            colors = CardDefaults.cardColors(containerColor = Color.White),
                            elevation = CardDefaults.cardElevation(defaultElevation = 12.dp)
                        ) {
                            Column(
                                modifier = Modifier.padding(16.dp)
                            ) {
                                Text(
                                    text = "Recent report",
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 20.sp
                                )
                                // Replace with actual image resource if available
                                Image(
                                    painter = painterResource(id = R.drawable.photo),
                                    contentDescription = "Picture of reported incident",
                                    modifier = Modifier.height(150.dp)
                                )
                                Text(text = report.description, fontWeight = FontWeight.Bold, fontSize = 20.sp)
                                Text(text = report.location)
                                Text(text = report.dateTime)
                                Text(text = report.incidentType)
                            }
                        }
                    }
                }
            }

            item {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 20.dp),
                    horizontalArrangement = Arrangement.SpaceAround
                ) {
                    CardButton(
                        iconRes = R.drawable.info,
                        label = "Report Incident",
                        onClick = { navController.navigate("makeReports") }
                    )
                    CardButton(
                        iconRes = R.drawable.clipboard,
                        label = "View Reports",
                        onClick = { navController.navigate("Viewreports") }
                    )
                }
            }

            item {
                Row(
                    modifier = Modifier
                        .fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceAround
                ) {
                    CardButton(
                        iconRes = R.drawable.headphones,
                        label = "Help & Support",
                        onClick = { navController.navigate("helpSupport") }
                    )
                    CardButton(
                        iconRes = R.drawable.emergency_contacts,
                        label = "Emergency\n Contacts",
                        onClick = { navController.navigate("emergencyContacts") }
                    )
                }
            }

            item {
                Row(
                    modifier = Modifier.height(20.dp)
                ) {
                    // Placeholder for spacing
                }
            }
        }
    }
}


suspend fun fetchRecentReport(): Report? {
    val database = FirebaseDatabase.getInstance()
    val reportsRef = database.getReference("Make Report Instance")

    return try {
        val dataSnapshot = reportsRef.limitToLast(1).get().await()
        dataSnapshot.children.firstOrNull()?.let { snapshot ->
            val description = snapshot.child("description").getValue(String::class.java) ?: ""
            val location = snapshot.child("location").getValue(String::class.java) ?: ""
            val dateTime = snapshot.child("dateTime").getValue(String::class.java) ?: ""
            val incidentType = snapshot.child("incidentType").getValue(String::class.java) ?: ""

            Report(description, location, dateTime, incidentType)
        }
    } catch (e: Exception) {
        e.printStackTrace()
        null
    }
}

@Preview
@Composable
fun DashboardPreview() {
    val navController = rememberNavController()
    Dashboard(navController)
}
