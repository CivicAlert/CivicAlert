package civicalertoriginal.Screen

import android.content.Context
import android.os.Build
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.tween
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.Icon
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.civicalertoriginal.Components.DescriptionTextFields
import com.example.civicalertoriginal.Components.ExposedDropdownMenuBox
import com.example.civicalertoriginal.Components.LocationTextFields
import com.example.civicalertoriginal.Components.PictureTextFields
import com.example.civicalertoriginal.Components.ReportDescriptionText
import com.example.civicalertoriginal.Components.SubmitButton
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter


data class Reports(
    val incidentType: String = "",
    val location: String = "",
    val description: String ="",
    val dateTime: String ="",

)
@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun MakeReports(navController: NavController) {
    var isVisible by remember { mutableStateOf(false)
    }
    var locationText by remember { mutableStateOf("")}

    val context = LocalContext.current
    val sharedP = context.getSharedPreferences("app_prefs",Context.MODE_PRIVATE)
    locationText = sharedP.getString("incidentAddress", "").toString()

    LaunchedEffect(Unit) {
        isVisible = true
    }

    Surface(color = Color.White) {
        AnimatedVisibility(
            visible = isVisible,
            enter = slideInVertically(
                initialOffsetY = { it },
                animationSpec = tween(1000, easing = LinearEasing)
            ),
            exit = slideOutVertically(
                targetOffsetY = { it },
                animationSpec = tween(1000, easing = LinearEasing)
            )
        ) {
            // Passing locationText to AnimatedMakeReports
            AnimatedMakeReports(navController, locationText) {
                isVisible = false
                navController.navigate("Dashboard")
            }
        }
    }
}

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun AnimatedMakeReports(
    navController: NavController,
    locationText: String,
    onClose: () -> Unit
) {
    val database = Firebase.database
    val myRef = database.getReference("Make Report Instance")
    val auth = FirebaseAuth.getInstance()

    var description by remember { mutableStateOf("") }
    var picture by remember { mutableStateOf("") }
    var selectedIncident by remember { mutableStateOf("Water") }
    val context = LocalContext.current
    val currentDateTime = LocalDateTime.now()
    val formattedDateTime = currentDateTime.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"))

    Column(
        verticalArrangement = Arrangement.spacedBy(30.dp),
        modifier = Modifier
            .fillMaxSize()
            .padding(start = 16.dp, end = 16.dp, top = 50.dp)
            .verticalScroll(rememberScrollState())
    ) {
        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.Start) {
            Icon(
                imageVector = Icons.Default.ArrowBack,
                contentDescription = "",
                modifier = Modifier
                    .size(30.dp)
                    .clickable { onClose() },
                tint = Color.Red
            )
            Spacer(modifier = Modifier.size(25.dp))
            Text(
                text = "Make A Report",
                fontSize = 35.sp,
                fontWeight = FontWeight.Bold,
                color = Color.Black
            )
        }

        // Incident type dropdown
        ReportDescriptionText(
            value1 = "Incident",
            value = "Choose Incident type"
        )
        ExposedDropdownMenuBox(
            selectedIncident = selectedIncident,
            onIncidentSelected = { newIncident -> selectedIncident = newIncident }
        )

        // Location section, passing the locationText (which contains the address)
        ReportDescriptionText(
            value1 = "Location",
            value = "Share the location of the incident using pin location or enter manually"
        )
        LocationTextFields(
            value = locationText,
            onChange = { },
            fieldLabel = "Enter location",
            navController = navController
        )

        // Photos and description section
        ReportDescriptionText(
            value1 = "Photos (Optional)",
            value = "Take photos of the incident you are reporting"
        )
        PictureTextFields(value = picture, onChange = { picture = it })

        ReportDescriptionText(
            value1 = "Report Description *",
            value = "Short Description of the incident"
        )
        DescriptionTextFields(
            value = description,
            onChange = { description = it },
            fieldLabel = "describe the incident"
        )

        // Create a report object
        val userReport = Reports(
            incidentType = selectedIncident,
            location = locationText,
            description = description,
            dateTime = formattedDateTime
        )

        // Save the report to Firebase
        fun saveReport(report: Reports) {
            val userId = myRef.push().key ?: return
            myRef.child(userId).setValue(report).addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    // Handle success
                    Toast.makeText(
                        context,
                        "Your report has been submitted.",
                        Toast.LENGTH_SHORT
                    ).show()
                } else {
                    // Handle failure
                    task.exception?.let {
                        println("Error saving user: ${it.message}")
                    }
                }
            }
        }

        // Submit button
        Column(
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            SubmitButton(name = "Submit") {
                saveReport(userReport)
                navController.navigate("Dashboard")
            }
        }
        Spacer(modifier = Modifier.size(8.dp))
    }
}


@RequiresApi(Build.VERSION_CODES.O)
@Preview
@Composable
fun MakeReportsPreview() {
    val navController = rememberNavController()
    MakeReports(navController = navController)
}
