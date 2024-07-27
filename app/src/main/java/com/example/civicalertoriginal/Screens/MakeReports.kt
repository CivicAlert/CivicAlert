package civicalertoriginal.Screen

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.tween
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
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
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.DialogProperties
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.civicalertoriginal.Components.DescriptionTextFields
import com.example.civicalertoriginal.Components.ExposedDropdownMenuBox
import com.example.civicalertoriginal.Components.LocationTextFields
import com.example.civicalertoriginal.Components.PictureTextFields
import com.example.civicalertoriginal.Components.ReportDescriptionText
import com.example.civicalertoriginal.Components.SubmitButton
import com.example.civicalertoriginal.R
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.concurrent.atomic.AtomicInteger

data class Reports(
    val incidentType: String = "",
    val location: String = "",
    val description: String = "",
    val dateTime: String = "",
    val refNumber: String = "",
    val status: String = "Submitted",
    val userUID: String = ""
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
            AnimatedMakeReports(navController) {
                isVisible = false
                navController.navigate("Dashboard")
            }
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
    var savedRefNumber by remember { mutableStateOf("") }

    val referenceNumber = ReferenceNumberGenerator.generateReferenceNumber()

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
            refNumber = referenceNumber,
            userUID = auth.currentUser?.email ?: "Unknown"
        )

        fun saveReport(report: Reports) {
            val userId = report.refNumber
            myRef.child(userId).setValue(report).addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    myRef.child(userId).addListenerForSingleValueEvent(object : ValueEventListener {
                        override fun onDataChange(snapshot: DataSnapshot) {
                            savedRefNumber = snapshot.child("refNumber").getValue(String::class.java) ?: ""
                            showDialog = true // Show the dialog upon successful retrieval
                        }

                        override fun onCancelled(error: DatabaseError) {
                            // Handle error
                            println("Error retrieving reference number: ${error.message}")
                        }
                    })
                } else {
                    // Handle failure
                    task.exception?.let {
                        println("Error saving report: ${it.message}")
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
            SuccessDialog(savedRefNumber, onDismiss = {
                showDialog = false
                onClose() // Close the current screen or perform other actions on dismiss
            })
        }
    }
}

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun SuccessDialog(referenceNumber: String, onDismiss: () -> Unit) {
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
                    text = "Report Successfully Submitted",
                    fontWeight = FontWeight.Bold,
                    fontSize = 20.sp
                )
                Text(
                    text = "Thank you for reporting to us. We\n  will take a look at the incident."
                )
                Text(
                    text = "Your Reference ID, keep it safe:\n     $referenceNumber",
                    fontWeight = FontWeight.Bold
                )
            }
        }
    )
}

@RequiresApi(Build.VERSION_CODES.O)
object ReferenceNumberGenerator {
    private val counter = AtomicInteger(0)

    fun generateReferenceNumber(): String {
        val currentDateTime = LocalDateTime.now()
        val dateTimeFormatter = DateTimeFormatter.ofPattern("yyMMdd-HHmmss")

        val dateTimePart = currentDateTime.format(dateTimeFormatter)
        val countPart = counter.getAndIncrement().toString().padStart(4, '0')

        return "$dateTimePart-$countPart"
    }
}

@RequiresApi(Build.VERSION_CODES.O)
@Preview
@Composable
fun MakeReportsPreview() {
    val navController = rememberNavController()
    MakeReports(navController = navController)
}
