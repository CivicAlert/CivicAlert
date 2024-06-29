package com.example.civicalertoriginal.Screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Call
import androidx.compose.material.icons.rounded.Edit
import androidx.compose.material.icons.rounded.Home
import androidx.compose.material.icons.rounded.List
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.civicalertoriginal.Components.*
import com.example.civicalertoriginal.R

@Composable
fun ContactUs(navController: NavController) {
    Surface(color = Color.White) {
        Scaffold(
            bottomBar = {
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
                            onClick = {navController.navigate("makeReports")  }
                        )
                        BottomNavItem(
                            icon = Icons.Rounded.List,
                            label = "View reports",
                            onClick = { navController.navigate("Viewreports") }
                        )
                        BottomNavItem(
                            icon = Icons.Rounded.Call,
                            label = "Emergency\n  Contact",
                            onClick = { navController.navigate("emergencyContacts") }
                        )
                    }
                }
            }
        ) { innerPadding ->
            Column(
                modifier = Modifier
                    .padding(innerPadding)
                    .padding(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Image(
                    painter = painterResource(id = R.drawable.contact),
                    contentDescription = "",
                    modifier = Modifier
                        .size(120.dp)
                        .offset(x = -100.dp)
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = "Contact Us",
                    modifier = Modifier.fillMaxWidth(),
                    fontSize = 30.sp,
                    fontFamily = FontFamily.Default,
                    fontStyle = FontStyle.Normal,
                    fontWeight = FontWeight.Bold,
                    color = Color.Black
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = "If you have inquiries, get in touch with us. We will be more than happy to help you.",
                    fontSize = 16.sp,
                    fontFamily = FontFamily.Default,
                    fontStyle = FontStyle.Normal,
                    fontWeight = FontWeight.Normal,
                    color = Color.DarkGray
                )
                Spacer(modifier = Modifier.height(16.dp))
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceEvenly
                ) {
                    ContactUsContactButton(value = "Call")
                    ContactUSEmailButton(value = "Email")
                }
                Spacer(modifier = Modifier.height(20.dp))
                Text(
                    text = "Social Media",
                    fontSize = 24.sp,
                    fontFamily = FontFamily.Default,
                    fontStyle = FontStyle.Normal,
                    fontWeight = FontWeight.SemiBold,
                    color = Color.Black
                )
                Spacer(modifier = Modifier.height(16.dp))
                Column(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceEvenly
                    ) {
                        ContactUsWhatsApp(value = "WhatsApp")
                        ContactUsWMessanger(value = "Facebook")
                    }
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceEvenly
                    ) {
                        ContactUsInsta(value = "Instagram")
                        ContactUsTwitter(value = "Twitter")
                    }
                }
            }
        }
    }
}

@Composable
fun BottomNavItem(icon: ImageVector, label: String, onClick: () -> Unit) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.clickable { onClick() }
    ) {
        Icon(imageVector = icon, contentDescription = label, modifier = Modifier.size(30.dp))
        Text(text = label, fontSize = 15.sp)
    }
}

@Preview
@Composable
fun ContactUsPreview() {
    val navController = rememberNavController()
    ContactUs(navController)
}