package com.example.civicalertoriginal.Screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Call
import androidx.compose.material.icons.rounded.Edit
import androidx.compose.material.icons.rounded.Home
import androidx.compose.material.icons.rounded.List
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.civicalertoriginal.Components.ContactUSEmailButton
import com.example.civicalertoriginal.Components.ContactUsContactButton
import com.example.civicalertoriginal.Components.ContactUsInsta
import com.example.civicalertoriginal.Components.ContactUsTwitter
import com.example.civicalertoriginal.Components.ContactUsWMessanger
import com.example.civicalertoriginal.Components.ContactUsWhatsApp
import com.example.civicalertoriginal.R

@Composable
fun ContactUs (navController: NavController){
    Surface(color = Color.White) {
        Scaffold (bottomBar = {
            BottomAppBar {

                Row {

                    Column (
                    ) {
                        Icon(imageVector = Icons.Rounded.Home, contentDescription = "", modifier = Modifier
                            .size(30.dp)
                            .align(Alignment.CenterHorizontally)
                            .clickable { navController.navigate("dashBoard") })
                        Text(text = "HOME", fontSize = 15.sp)
                    }
                    Spacer(modifier = Modifier.size(20.dp))
                    Column (){
                        Icon(imageVector = Icons.Rounded.Edit, contentDescription = "", modifier = Modifier
                            .size(30.dp)
                            .align(Alignment.CenterHorizontally))
                        Text(text = "MAKE REPORTS", fontSize = 15.sp)
                    }
                    Spacer(modifier = Modifier.size(20.dp))
                    Column () {
                        Icon(imageVector = Icons.Rounded.List, contentDescription = "", modifier = Modifier
                            .size(30.dp)
                            .align(Alignment.CenterHorizontally))
                        Text(text = "VIEW REPORTS", fontSize = 15.sp)
                    }
                    Spacer(modifier = Modifier.size(20.dp))
                    Column () {
                        Icon(imageVector = Icons.Rounded.Call, contentDescription = "", modifier = Modifier
                            .size(30.dp)
                            .align(Alignment.CenterHorizontally)
                            .clickable { navController.navigate("contactUs") })
                        Text(text = "CALL", fontSize = 15.sp)
                    }
                } } }){innerPadding ->
            Column {
                Image(contentDescription ="", painter = painterResource(id = R.drawable.contact), modifier = Modifier.size(120.dp,150.dp) )
                Spacer(modifier = Modifier.size(5.dp))
                Text(text = "Contact Us", modifier = Modifier
                    .fillMaxWidth(), fontSize = 50.sp, fontFamily = FontFamily.Default, fontStyle = FontStyle.Normal, fontWeight = FontWeight.Bold)
                Text(text = "If you have enquires get in touch with us. We will be more than happy to help you.")
                Spacer(modifier = Modifier.size(20.dp))
                Row (modifier = Modifier.fillMaxWidth() , verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.SpaceEvenly){
                    ContactUsContactButton(value = "Call")
                    ContactUSEmailButton(value = "Email")
                }
                Spacer(modifier = Modifier
                    .size(20.dp)
                    .align(Alignment.Start))
                Text(text = "Social Media", fontSize = 25.sp)
                Column (modifier = Modifier.fillMaxWidth(), horizontalAlignment = Alignment.CenterHorizontally, verticalArrangement = Arrangement.SpaceAround){
                    Spacer(modifier = Modifier.size(15.dp))
                    Row {
                        ContactUsWhatsApp(value = "WhatsApp")
                        Spacer(modifier = Modifier.size(35.dp))
                        ContactUsInsta(value = "Instagram")
                    }
                    Row {
                        ContactUsWMessanger(value = "Facebook")
                        Spacer(modifier = Modifier.size(35.dp))
                        ContactUsTwitter(value = "Twitter")
                    }

                }


            }
        }

    }
}