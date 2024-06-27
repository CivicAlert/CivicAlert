package civicalertoriginal.Screen

import android.widget.Space
import android.widget.Toast
import androidx.compose.animation.*
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.tween
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.focusModifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.civicalertoriginal.Components.*
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase

@Composable
fun MakeReports(navController: NavController) {
    var isVisible by remember { mutableStateOf(false) }

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
            AnimatedMakeReports(navController){isVisible = false}
        }
    }
}

@Composable
fun AnimatedMakeReports(navController: NavController, onClose:()->Unit) {
    val database = Firebase.database
    var location by remember { mutableStateOf("Enter Location") }
    var description by remember { mutableStateOf("Brief details of the incident") }
    var picture by remember { mutableStateOf("") }
    val context = LocalContext.current

    Column(
        verticalArrangement = Arrangement.spacedBy(30.dp),
        modifier = Modifier
            .fillMaxSize()
            .padding(start = 16.dp, end = 16.dp, top = 50.dp)
            .verticalScroll(rememberScrollState())
    ) {
        Row ( modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.Start) {

            Icon(imageVector = Icons.Default.ArrowBack, contentDescription = "", modifier = Modifier.size(30.dp) .clickable { onClose() },
                tint = Color.Red)
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
        ExposedDropdownMenuBox()

        ReportDescriptionText(
            value1 = "Location(Optional)",
            value = "Share the location of the incident"
        )
        LocationTextFields(value = location, onChange = { location = it }, fieldLabel = "Location")

        ReportDescriptionText(
            value1 = "Photos*",
            value = "Take photos of the incident you are reporting"
        )
        PictureTextFields(value = picture, onChange = { picture = it }, fieldLabel = "Take or Upload Picture")

        ReportDescriptionText(
            value1 = "Report Description*",
            value = "Short Description of the incident"
        )
        DescriptionTextFields(value = description, onChange = { description = it }, fieldLabel = "Give a brief description of the incident")

        val report = hashMapOf(


            "Report Description" to description,
            "Report picture" to picture,
            "Report location" to location
        )

        BottomButtons(name = "SUBMIT REPORT") {
            Toast.makeText(
                context,
                "Your report has been submitted.",
                Toast.LENGTH_SHORT
            ).show()
        }
    }
}

@Preview
@Composable
fun MakeReportsPreview() {
    val navController = rememberNavController()
    MakeReports(navController = navController)
}
