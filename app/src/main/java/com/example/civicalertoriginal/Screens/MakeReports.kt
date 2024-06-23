package civicalertoriginal.Screen

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.civicalertoriginal.Components.BottomButtons
import com.example.civicalertoriginal.Components.DescriptionTextFields
import com.example.civicalertoriginal.Components.ExposedDropdownMenuBox
import com.example.civicalertoriginal.Components.LocationTextFields
import com.example.civicalertoriginal.Components.PictureTextFields
import com.example.civicalertoriginal.Components.ReportDescriptionText
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase

@Composable
fun MakeReports(navController: NavController) {
    val database = Firebase.database
    val myRef = database.getReference("Reports")
    val context = LocalContext.current
    var location by remember { mutableStateOf("Enter Location") }
    var description by remember { mutableStateOf("Brief details of the incident") }
    var picture by remember { mutableStateOf("") }

    Column(
        verticalArrangement = Arrangement.spacedBy(30.dp),
        modifier = Modifier
            .fillMaxSize()
            .padding(start = 16.dp, end = 16.dp, top = 50.dp)
            .verticalScroll(rememberScrollState())
    ) {
        Text(
            text = "Make A Report",
            modifier = Modifier.fillMaxWidth(),
            fontSize = 45.sp,
            color = Color.Green
        )
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

        BottomButtons(name = "SUBMIT REPORT", {
            myRef.push().setValue(report)
        })
    }
}

@Preview
@Composable
fun MakeReportsPreview() {
    val navController = rememberNavController()
    MakeReports(navController = navController)
}
