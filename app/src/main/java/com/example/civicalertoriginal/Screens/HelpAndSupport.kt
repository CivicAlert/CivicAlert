package com.example.civicalertoriginal.Screens

import android.annotation.SuppressLint
import android.widget.Space
import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.rounded.Call
import androidx.compose.material.icons.rounded.Edit
import androidx.compose.material.icons.rounded.Home
import androidx.compose.material.icons.rounded.List
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.Card
import androidx.compose.material3.CardColors
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.focusModifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.max
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController

data class FAQ(
    val question: String,
    val answer: String
)

val faqList = listOf(
    FAQ(
        question = "How do I access the incident reporting system?",
        answer = "You can access it through our dedicated app, but it can only be accessed by Android users for now."
    ),
    FAQ(
        question = "What type of incidents should be reported?",
        answer = "Potholes, water issues, electricity concerns, and potential hazards like a broken streetlight that can fall on someone else's property or harm people."
    ),
    FAQ(
        question = "Will my personal information be shared if I report an incident?",
        answer = "Your information will be kept confidential, unless you agree to share it for investigational purposes."
    ),
    FAQ(
        question = "Can I attach files when reporting?",
        answer = "Yes, our system allows you to upload images to support your report."
    ),
    FAQ(
        question = "How often should I check for updates on my submitted report?",
        answer = "The system will notify you of any updates, but you can log in to check the status."
    ),
    FAQ(
        question = "How long does it take for an incident to be fixed?",
        answer = "The duration varies depending on the complexity of the incident."
    ),
    FAQ(
        question = "Can I still report if I’m not sure if something qualifies as a reportable incident?",
        answer = "It is advisable to report. The safety team will assess and determine if further action is required."
    ),
    FAQ(
        question = "How do I track the status of my submitted report?",
        answer = "You will receive a reference number after reporting, which you will use to track the status of your reported incident."
    )
)
@Composable
fun FAQList(faqs: List<FAQ>) {
    LazyColumn {
        items(faqs){
            faq -> FAQItem(faq)
        }
    }

}
@Composable
fun FAQItem(faq:FAQ){
    var expandeed by remember { mutableStateOf(false) }
    Column( modifier = Modifier
        .padding(vertical = 8.dp, horizontal = 16.dp)
        .clickable { expandeed = !expandeed }
        .padding(20.dp)) {
        Text(text = faq.question, style = MaterialTheme.typography.titleMedium, maxLines = 2, overflow = TextOverflow.Ellipsis)
        if (expandeed){
            Text(
                text = faq.answer,
                style = MaterialTheme.typography.titleSmall ,
                modifier = Modifier.padding(16.dp)
            )
        }
    }
}


@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun HelpAndSupport(navController: NavController) {
    Surface(color = Color.White,
        modifier = Modifier.fillMaxWidth()) {

        Scaffold (bottomBar = {
            BottomAppBar {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceAround
                ) {
                    BottomNavItem(
                        icon = Icons.Rounded.Home,
                        label = "Home",
                        onClick = { navController.navigate("Dashboard") }
                    )
                    BottomNavItem(
                        icon = Icons.Rounded.Edit,
                        label = "Make report",
                        onClick = { navController.navigate("makeReports") }
                    )
                    BottomNavItem(
                        icon = Icons.Rounded.List,
                        label = "View reports",
                        onClick = { navController.navigate("Viewreports") }
                    )
                    BottomNavItem(
                        icon = Icons.Rounded.Call,
                        label = "Emergency\nContact",
                        onClick = { navController.navigate("emergencyContacts") }
                    )
                }
            }
        }){
            Column ( modifier = Modifier.fillMaxSize(), horizontalAlignment = Alignment.Start){
                Spacer(modifier = Modifier.size(20.dp))
                Text(text = "FAQ", fontSize = 50.sp, fontWeight = FontWeight.Bold, fontFamily = FontFamily.Monospace)
                Spacer(modifier = Modifier.size(15.dp))
                Text(text = "Find frequently asked question below.", fontSize = 20.sp)
                Spacer(modifier = Modifier.size(12.dp))
                Card ( modifier = Modifier
                    .fillMaxWidth()
                    .padding(12.dp),
                    colors = CardDefaults.cardColors(containerColor = Color.White),
                    elevation = CardDefaults.cardElevation(defaultElevation = 5.dp)) {
                   Column( modifier = Modifier.fillMaxWidth(), verticalArrangement = Arrangement.SpaceBetween) {
                       Column(horizontalAlignment = Alignment.Start) {
                           Text(text = "Click the question to get the answer.", fontSize = 15.sp, color = Color.Red)
                           Spacer(modifier = Modifier.size(12.dp))
                       }
                        FAQList(faqs = faqList)
                    }

                } } } } }
/*@Composable
fun ButtomNavItem(icon:ImageVector, label:String, onClick:() ->Unit){
    Column {
        Icon(imageVector = icon, contentDescription = null, modifier = Modifier.size(45.dp))
        Text(text = label, fontSize = 2.sp)
    }
}*/


@Preview
@Composable
fun HelpPreview(){
    //HelpAndSupport(navController)
}