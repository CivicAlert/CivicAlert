package com.example.civicalertoriginal.Screens

import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
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
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.civicalertoriginal.Components.CardButton
import com.example.civicalertoriginal.Components.Logo
import com.example.civicalertoriginal.R
import com.google.firebase.database.FirebaseDatabase
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Dashboard(navController: NavController) {
    // Coroutine scope to launch async operations
    val coroutineScope = rememberCoroutineScope()

    // State to hold the recent report
    var recentReport by remember { mutableStateOf<IncidentReport?>(null) }

    // Launch a coroutine to fetch the recent report
    LaunchedEffect(Unit) {
        coroutineScope.launch {
            recentReport = fetchRecentReport() // Fetching the recent report asynchronously
        }
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

            // Display recent report if available
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
                                    text = "Recently Reported Incident",
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
                                Text(text = report.incidentType, color = Color.Blue)
                            }
                        }
                    }
                }
            } ?: item {
                // Display a placeholder or message if there's no recent report
                Text("No recent report available", fontWeight = FontWeight.Bold, fontSize = 20.sp)
            }

            // Other UI items...
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
                        onClick =  { navController.navigate("helpSupport") }
                    )
                    CardButton(
                        iconRes = R.drawable.emergency_contacts,
                        label = "Emergency\n Contacts",
                        onClick = { navController.navigate("emergencyContacts") }
                    )
                }
            }

            // Spacer for additional layout
            item {
                Row(
                    modifier = Modifier.height(20.dp)
                ) {
                    Spacer(modifier = Modifier.height(20.dp))
                }
            }
        }
    }
}
data class IncidentReport(
    val description: String,
    val location: String,
    val dateTime: String,
    val incidentType: String
)

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