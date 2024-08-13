package com.example.civicalertoriginal.Screens

import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
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
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import kotlinx.coroutines.tasks.await

data class IncidentReport(
    val description: String = "",
    val location: String = "",
    val dateTime: String = "",
    val incidentType: String = ""
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Dashboard(navController: NavController) {
    var recentReport by remember { mutableStateOf<IncidentReport?>(null) }
    val auth = FirebaseAuth.getInstance()
    val currentUser = auth.currentUser

    LaunchedEffect(Unit) {
        recentReport = fetchRecentReport()
        Log.d("Dashboard", "Fetched recent report: $recentReport") // Debug statement
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
            } ?: item {
                // Display a placeholder or message if there's no recent report
                Text("No recent report available", fontWeight = FontWeight.Bold, fontSize = 20.sp)
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

suspend fun fetchRecentReport(): IncidentReport? {
    val database = FirebaseDatabase.getInstance()
    val reportsRef = database.getReference("Make Report Instance")

    return try {
        val dataSnapshot = reportsRef.get().await()
        val reports = dataSnapshot.children.mapNotNull { snapshot ->
            val description = snapshot.child("description").getValue(String::class.java) ?: return@mapNotNull null
            val location = snapshot.child("location").getValue(String::class.java) ?: return@mapNotNull null
            val dateTime = snapshot.child("dateTime").getValue(String::class.java) ?: return@mapNotNull null
            val incidentType = snapshot.child("incidentType").getValue(String::class.java) ?: return@mapNotNull null

            IncidentReport(description, location, dateTime, incidentType)
        }

        reports.maxByOrNull { it.dateTime } // Find the report with the latest dateTime
            .also {
                Log.d("fetchRecentReport", "Fetched most recent report: $it") // Debug statement
            }
    } catch (e: Exception) {
        e.printStackTrace()
        Log.e("fetchRecentReport", "Error fetching report", e) // Error log
        null
    }
}


@Preview
@Composable
fun DashboardPreview() {
    val navController = rememberNavController()
    Dashboard(navController)
}
