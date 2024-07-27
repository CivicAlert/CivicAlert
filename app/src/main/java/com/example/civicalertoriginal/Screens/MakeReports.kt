package com.example.civicalertoriginal.Screens

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Point
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.tween
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import androidx.lifecycle.*
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.civicalertoriginal.Components.*
import com.mapbox.maps.MapView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import com.mapbox.maps.CameraOptions
import com.mapbox.maps.plugin.gestures.addOnMapClickListener
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

data class Reports(
    val incidentType: String = "",
    val location: String = "",
    val description: String = "",
    val dateTime: String = "",
    val refNumber: String = "",
    val status: String = "",
    val userID: String = ""
)

class SharedPrefs(context: Context) {
    private val prefs = context.getSharedPreferences("reference_number_prefs", Context.MODE_PRIVATE)

    var increment: Int
        get() = prefs.getInt("increment", 0)
        set(value) {
            prefs.edit().putInt("increment", value).apply()
        }
}

@SuppressLint("DefaultLocale")
@RequiresApi(Build.VERSION_CODES.O)
fun generateReferenceNumber(context: Context): String {
    val sharedPrefs = SharedPrefs(context)
    val currentDateTime = LocalDateTime.now()
    val dateFormatter = DateTimeFormatter.ofPattern("yyMMdd")
    val timeFormatter = DateTimeFormatter.ofPattern("HHmmss")
    val datePart = currentDateTime.format(dateFormatter)
    val timePart = currentDateTime.format(timeFormatter)

    // Retrieve and increment the stored increment value
    val incrementedPart = String.format("%04d", sharedPrefs.increment++)
    sharedPrefs.increment = sharedPrefs.increment // Save the updated increment value

    return "$datePart-$timePart-$incrementedPart"
}

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun MakeReports(navController: NavController) {
    var isVisible by remember { mutableStateOf(false) }
    val context = LocalContext.current
    val auth = FirebaseAuth.getInstance()

    LaunchedEffect(Unit) {
        isVisible = true
    }

    Surface(color = Color.White) {
        // Fetch the current user's UID and email
        val currentUser = auth.currentUser
        val email = currentUser?.email ?: ""

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
            AnimatedMakeReports(navController, email) {
                isVisible = false
                navController.navigate("Dashboard")
            }
        }
    }
}

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun AnimatedMakeReports(navController: NavController, userEmail: String, onClose: () -> Unit) {
    val database = Firebase.database
    val myRef = database.getReference("Make Report Instance") // Reference to "Make Report Instance"
    val auth = FirebaseAuth.getInstance()
    var location by remember { mutableStateOf("") }
    var description by remember { mutableStateOf("") }
    var picture by remember { mutableStateOf("") }
    val context = LocalContext.current
    val currentDateTime = LocalDateTime.now()
    val formattedDateTime = currentDateTime.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"))
    var showDialog by remember { mutableStateOf(false) }

    // Generate the reference number with the context
    val referenceNumber = generateReferenceNumber(context)

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
        LocationTextFields(value = location,
            onChange = { location = it },
            fieldLabel = " Enter location")

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
            status = "Submitted",
            userID = userEmail // Set userID to current user's email
        )

        fun saveReport(report: Reports) {
            // Associate the report with the user using UID
            val reportWithUser = mapOf(
                "incidentType" to report.incidentType,
                "location" to report.location,
                "description" to report.description,
                "dateTime" to report.dateTime,
                "refNumber" to report.refNumber,
                "status" to report.status,
                "userID" to report.userID
            )

            val currentUser = auth.currentUser
            val userUid = currentUser?.uid

            if (userUid != null) {
                myRef.child(userUid).push().setValue(reportWithUser)
            }
        }

        SubmitButton {
            saveReport(userReport)
            showDialog = true // Show success dialog when report is submitted
        }

        if (showDialog) {
            AlertDialog(
                onDismissRequest = { showDialog = false },
                title = { Text("Report Submitted", color = Color.Green) },
                text = {
                    Text(
                        "Your report has been successfully submitted!",
                        color = Color.Black
                    )
                },
                confirmButton = {
                    Button(
                        onClick = {
                            showDialog = false
                            onClose()
                        },
                        colors = ButtonDefaults.buttonColors(Color.Green)
                    ) {
                        Text("OK")
                    }
                }
            )
        }
    }
}

@Composable
fun MapboxMapView(onLocationSelected: (Double, Double) -> Unit) {
    val context = LocalContext.current
    val mapView = rememberMapViewWithLifecycle()

    AndroidView(factory = { mapView }) { mapView ->
        val mapboxMap = mapView.getMapboxMap()
        mapboxMap.setCamera(
            CameraOptions.Builder()
                .center(Point.fromLngLat(30.0, -25.0)) // Centering the map at a default location
                .zoom(8.0)
                .build()
        )

        mapboxMap.addOnMapClickListener { point ->
            onLocationSelected(point.latitude(), point.longitude())
            true
        }
    }
}

@Composable
fun rememberMapViewWithLifecycle(): MapView {
    val context = LocalContext.current
    val mapView = remember { MapView(context) }

    DisposableEffect(mapView) {
        val lifecycle = LocalLifecycleOwner.current.lifecycle
        val lifecycleObserver = getMapViewLifecycleObserver(mapView)
        lifecycle.addObserver(lifecycleObserver)

        onDispose {
            lifecycle.removeObserver(lifecycleObserver)
        }
    }

    return mapView
}

@Composable
fun getMapViewLifecycleObserver(mapView: MapView): DefaultLifecycleObserver {
    return object : DefaultLifecycleObserver {
        override fun onStart(owner: LifecycleOwner) {
            mapView.onStart()
        }

        override fun onResume(owner: LifecycleOwner) {
            mapView.onResume()
        }

        override fun onPause(owner: LifecycleOwner) {
            mapView.onPause()
        }

        override fun onStop(owner: LifecycleOwner) {
            mapView.onStop()
        }

        override fun onDestroy(owner: LifecycleOwner) {
            mapView.onDestroy()
        }
    }
}


@Composable
fun MapboxPickerDialog(
    onDismiss: () -> Unit,
    onLocationSelected: (String) -> Unit
) {
    Dialog(
        onDismissRequest = onDismiss,
        properties = DialogProperties(
            usePlatformDefaultWidth = false
        )
    ) {
        Surface(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
        ) {
            MapboxMapView(onLocationSelected = { latitude, longitude ->
                val location = "$latitude, $longitude"
                onLocationSelected(location)
                onDismiss()
            })
        }
    }
}

@Composable
fun LocationTextFields(value: String, onChange: (String) -> Unit, fieldLabel: String) {
    var showMapDialog by remember { mutableStateOf(false) }

    Column(
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        OutlinedTextField(
            value = value,
            onValueChange = onChange,
            placeholder = { Text(text = fieldLabel, color = Color.Green) },
            trailingIcon = {
                Icon(
                    modifier = Modifier
                        .size(35.dp)
                        .clickable { showMapDialog = true },
                    imageVector = Icons.Default.LocationOn,
                    contentDescription = "Location Icon"
                )
            },
            keyboardOptions = KeyboardOptions.Default,
            textStyle = TextStyle(color = Color.Black),
            modifier = Modifier
                .height(50.dp)
                .fillMaxWidth()
                .background(Color.White)
        )
    }

    if (showMapDialog) {
        MapboxPickerDialog(
            onDismiss = { showMapDialog = false },
            onLocationSelected = { location ->
                onChange(location)
            }
        )
    }
}

@RequiresApi(Build.VERSION_CODES.O)
@Preview(showBackground = true)
@Composable
fun PreviewMakeReports() {
    val navController = rememberNavController()
    MakeReports(navController)
}
