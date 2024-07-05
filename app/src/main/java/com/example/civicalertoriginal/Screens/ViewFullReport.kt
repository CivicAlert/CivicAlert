package com.example.civicalertoriginal.Screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.Icon
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.civicalertoriginal.Components.ReportDescriptionText
import com.example.civicalertoriginal.R
import com.google.firebase.Firebase
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.database

data class getReport(
    var incidentType: String = "",
    var description: String = "",
    var reference_number: String = "",
    var location: String = ""

)
@Composable
fun ViewFullReport (navController: NavController, reportId:String){

    val report = remember { mutableStateOf(getReport()) }
    LaunchedEffect(reportId) {
        val databse = Firebase.database
        val myRef = databse.getReference("Make Report Instance").child(reportId)
        myRef.addListenerForSingleValueEvent(object :ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                val fetchReport = snapshot.getValue(getReport::class.java)
                fetchReport?.let { report.value =it }
            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }
        })

    }
    Surface(color = Color.White, modifier = Modifier.fillMaxSize() .padding(10.dp)) {
        Column ( modifier = Modifier.fillMaxWidth() .padding(10.dp), verticalArrangement = Arrangement.spacedBy(10.dp)){
            Text(text = "View Report", modifier = Modifier.fillMaxWidth(),
                fontSize = 50.sp, fontFamily = FontFamily.Default,
                fontStyle = FontStyle.Normal
            )

            //Spacer(modifier = Modifier.size(25.dp))
            Row {
                Icon(imageVector = Icons.Default.ArrowBack, contentDescription = "", modifier = Modifier.clickable { navController.navigate("Viewreports") })
                Spacer(modifier = Modifier.size(20.dp))
                Text(text = "Back")
                    }
                    Spacer(modifier = Modifier.size(25.dp))
                    Image(painter = painterResource(id = R.drawable.photo), contentDescription ="", modifier = Modifier.fillMaxWidth() )
                    Spacer(modifier = Modifier.size(25.dp))
                    ReportDescriptionText(value1 = "Details", value = report.value.incidentType)
                    Spacer(modifier = Modifier.size(10.dp))
                    ReportDescriptionText(value1 = "Report Description", value = report.value.description )
                    Spacer(modifier = Modifier.size(10.dp))
                    ReportDescriptionText(value1 = "Status", value = "Agent looking At it")
                    Spacer(modifier = Modifier.size(10.dp))
                    ReportDescriptionText(value1 = "Status", value = report.value.location)
                    Spacer(modifier = Modifier.size(10.dp))
                    ReportDescriptionText(value1 = "ReferenceId", value = report.value.reference_number)


        }

    }
}