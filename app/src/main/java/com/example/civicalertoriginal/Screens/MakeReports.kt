package civicalertoriginal.Screen

import android.health.connect.datatypes.ExerciseRoute.Location
import android.os.Build
import android.widget.Space
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.compose.animation.*
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.focusModifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.DialogProperties
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.civicalertoriginal.Components.*
import com.example.civicalertoriginal.R
import com.example.civicalertoriginal.Screens.User
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import kotlin.random.Random


data class Reports(
    val incidentType: String = "",
    val location: String = "",
    val description: String ="",
    val dateTime: String ="",
    val refNumber: String =""

)
@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun MakeReports(navController: NavController) {
    var isVisible by remember { mutableStateOf(false) }
    val context = LocalContext.current

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
            AnimatedMakeReports(navController){isVisible = false
            navController.navigate("Dashboard")}

        }
    }
}

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun AnimatedMakeReports(navController: NavController, onClose: () -> Unit) {
    val database = Firebase.database
    val myRef = database.getReference("Make Report Instance")
    val auth = FirebaseAuth.getInstance()
    var location by remember { mutableStateOf("") }
    var description by remember { mutableStateOf("") }
    var picture by remember { mutableStateOf("") }
    val context = LocalContext.current
    val currentDateTime = LocalDateTime.now()
    val formattedDateTime = currentDateTime.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"))
    var showDialog by remember { mutableStateOf(false) }

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
        ReportDescriptionText(
            value1 = "Incident",
            value = "Choose Incident type"
        )
        var selectedIncident by remember { mutableStateOf("Water") }
        ExposedDropdownMenuBox(
            selectedIncident = selectedIncident,
            onIncidentSelected = { newIncident -> selectedIncident = newIncident })

        ReportDescriptionText(
            value1 = "Location(Optional)",
            value = "Share the location of the incident"
        )
        LocationTextFields(value = location, onChange = { location = it }, fieldLabel = " Enter location")

        ReportDescriptionText(
            value1 = "Photos*",
            value = "Take photos of the incident you are reporting"
        )
        PictureTextFields(value = picture, onChange = { picture = it })

        ReportDescriptionText(
            value1 = "Report Description*",
            value = "Short Description of the incident"
        )
        DescriptionTextFields(
            value = description,
            onChange = { description = it },
            fieldLabel = "brief description of the incident"
        )

        val userReport = Reports(
            incidentType = selectedIncident,
            location = location,
            description = description,
            dateTime = formattedDateTime,
            refNumber = referenceNumber
        )

        fun saveReport(report: Reports) {
            val userId = myRef.push().key ?: return
            myRef.child(userId).setValue(report).addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    // Handle success
                    showDialog = true // Show the dialog upon successful submission
                } else {
                    // Handle failure
                    task.exception?.let {
                        println("Error saving user: ${it.message}")
                    }
                }
            }
        }

        Column(
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            SubmitButton(name = "Submit") {
                saveReport(userReport)
            }
        }
        Spacer(modifier = Modifier.size(8.dp))

        // Display the success dialog if showDialog is true
        if (showDialog) {
            SuccessDialog(onDismiss = {
                showDialog = false
                onClose() // Close the current screen or perform other actions on dismiss
            })
        }

    }
}

@Composable
fun SuccessDialog(onDismiss: () -> Unit) {
    AlertDialog(
        onDismissRequest = onDismiss,
        properties = DialogProperties(dismissOnClickOutside = false),
        confirmButton = {
            Button(
                onClick = onDismiss,
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFB2F4B8))
            ) {
                Text("DONE", color = Color.Black)
            }
        },
        title = null,
        text = {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                Box(
                    modifier = Modifier
                        .size(80.dp)
                        .clip(CircleShape)
                        .background(Color(0xFFE0F7EA)),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        painter = painterResource(id = R.drawable.check),
                        contentDescription = null,
                        tint = Color(0xFF00C853),
                        modifier = Modifier.size(60.dp)
                    )
                }
                Text(
                    text = "Report Successful Submitted",
                    fontWeight = FontWeight.Bold,
                    fontSize = 20.sp
                )
                Text(
                    text = "Thank you for reporting to us. We\n  will take a look at the incident."
                )
                Text(
                    text = "Your Reference ID:\n  $referenceNumber",
                    fontWeight = FontWeight.Bold
                )

            }
        }
    )
}
@RequiresApi(Build.VERSION_CODES.O)
fun generateReferenceNumber(): String {
    val currentDateTime = LocalDateTime.now()
    val dateFormatter = DateTimeFormatter.ofPattern("yyMM")
    val timeFormatter = DateTimeFormatter.ofPattern("HH")

    val datePart = currentDateTime.format(dateFormatter)
    val timePart = currentDateTime.format(timeFormatter)

    // Generate a random alphanumeric string of length 4
    val randomPart = (1..4)
        .map { ('A'..'Z') + ('0'..'9') }
        .flatten()
        .shuffled()
        .take(4)
        .joinToString("")

    return "$datePart/$timePart-$randomPart"
}
@RequiresApi(Build.VERSION_CODES.O)
val referenceNumber = generateReferenceNumber()

@RequiresApi(Build.VERSION_CODES.O)
@Preview
@Composable
fun MakeReportsPreview() {
    val navController = rememberNavController()
    MakeReports(navController = navController)
}
