package com.example.civicalertoriginal.Screens

import android.annotation.SuppressLint
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Call
import androidx.compose.material.icons.rounded.Edit
import androidx.compose.material.icons.rounded.Face
import androidx.compose.material.icons.rounded.Home
import androidx.compose.material.icons.rounded.List
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.Card
import androidx.compose.material3.CardColors
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember

import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.civicalertoriginal.Components.BottomButtonsMyProfile
import com.example.civicalertoriginal.Components.ProfileText

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun UpdateProfile (navController: NavController){
    Surface( color = Color.White) {
        Scaffold (bottomBar = {
            BottomAppBar {

                Row {

                    Column ( modifier = Modifier.clickable { navController.navigate("Dashboard") }) {
                        Icon(imageVector = Icons.Rounded.Home, contentDescription = "", modifier = Modifier
                            .size(30.dp)
                            .align(Alignment.CenterHorizontally))
                        Text(text = "HOME")
                    }
                    Spacer(modifier = Modifier.size(20.dp))
                    Column (modifier = Modifier.clickable { navController.navigate("makeReports") }){
                        Icon(imageVector = Icons.Rounded.Edit, contentDescription = "", modifier = Modifier
                            .size(30.dp)
                            .align(Alignment.CenterHorizontally)
                            .clickable { navController.navigate("makeReports") })
                        Text(text = "MAKE REPORTS")
                    }
                    Spacer(modifier = Modifier.size(20.dp))
                    Column ( modifier = Modifier.clickable { navController.navigate("") }) {
                        Icon(imageVector = Icons.Rounded.List, contentDescription = "", modifier = Modifier
                            .size(30.dp)
                            .align(Alignment.CenterHorizontally))
                        Text(text = "VIEW REPORTS")
                    }
                    Spacer(modifier = Modifier.size(20.dp))
                    Column (modifier = Modifier.clickable { navController.navigate("emergencyCall") }) {
                        Icon(imageVector = Icons.Rounded.Call, contentDescription = "", modifier = Modifier
                            .size(30.dp)
                            .align(Alignment.CenterHorizontally))
                        Text(text = "CALL")
                    }

                }
            }
        }){innerPadding ->
            Column {
                Spacer(modifier = Modifier.size(10.dp))
                Column ( modifier = Modifier.fillMaxWidth() ,
                    verticalArrangement = Arrangement.Center, horizontalAlignment = Alignment.CenterHorizontally){
                    Icon(imageVector = Icons.Rounded.Face, contentDescription = "", modifier = Modifier.size(80.dp
                    ))
                    Text(text = "USER NAME", fontSize = 25.sp)
                }
                val cardColor by remember {
                    mutableStateOf(Color.Green)
                }

                Card (  modifier = Modifier
                    .fillMaxWidth()
                    .background(cardColor) ){
                    Column {
                        ProfileText(description = "First Name", value = "User name")
                        Spacer(modifier = Modifier.size(10.dp))
                        ProfileText(description = "Last Name", value = "Sirname")
                        Spacer(modifier = Modifier.size(10.dp))
                        ProfileText(description = "Email address", value = "emailInForm@gmail.com")
                        Spacer(modifier = Modifier.size(10.dp))
                        ProfileText(description = "Phone number", value = "+27 68801025")

                    } }

                Spacer(modifier = Modifier.size(20.dp))
                BottomButtonsMyProfile(name = "UPDATE") {}
                Spacer(modifier = Modifier.size(20.dp))
                BottomButtonsMyProfile(name = "Log Out") {}
            }
        }

        
        
    }
}
@Preview
@Composable
fun SignUpPreview(){

}