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
import androidx.compose.foundation.layout.size
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
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.focusModifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun HelpAndSupport(navController: NavController) {
    Surface(color = Color.White,
        modifier = Modifier.fillMaxWidth()) {

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
                    Column (modifier = Modifier.clickable { navController.navigate("emergencyContacts") }) {
                        Icon(imageVector = Icons.Rounded.Call, contentDescription = "", modifier = Modifier
                            .size(30.dp)
                            .align(Alignment.CenterHorizontally))
                        Text(text = "CALL")
                    }

                }
            }
        }){
            Column ( modifier = Modifier.fillMaxSize(), horizontalAlignment = Alignment.Start){
                val searching by remember { mutableStateOf("") }
                Spacer(modifier = Modifier.size(20.dp))
                Text(text = "What do you need help with?", fontWeight = FontWeight.Black, fontSize = 20.sp)
                Spacer(modifier =Modifier.size(20.dp))
                Column ( verticalArrangement = Arrangement.Center) {
                    OutlinedTextField(value = searching , onValueChange = {},
                        trailingIcon = {
                            Icon(
                                modifier = Modifier
                                    .size(35.dp, 35.dp)
                                    .clickable { },
                                imageVector = Icons.Default.Search,
                                contentDescription = "Search"
                            ) },
                        keyboardOptions = KeyboardOptions.Default,
                        textStyle = TextStyle(color = Color.Black ), modifier = Modifier
                            .height(50.dp)
                            .fillMaxWidth()
                            .background(Color.White)
                    )
                }
                Spacer(modifier = Modifier.size(40.dp))
                Text(text = "FAQ", fontSize = 50.sp, fontWeight = FontWeight.Bold, fontFamily = FontFamily.Monospace)
                Spacer(modifier = Modifier.size(15.dp))
                Text(text = "Find frequently asked question below.", fontSize = 20.sp)
                Spacer(modifier = Modifier.size(12.dp))
                val cardColor by remember {
                    mutableStateOf(Color.White)
                }
                Card (modifier = Modifier
                        .fillMaxWidth()
                    .background(cardColor)
                    .border(5.dp, color = Color.White, shape = RectangleShape),
                colors = CardDefaults.cardColors(Color.White)) {
                        Row (horizontalArrangement = Arrangement.spacedBy(50.dp), modifier = Modifier.border(0.dp, color = Color.Transparent)
                        ){
                            Text(text = "How does incident reporting system work?" )
                            //Spacer(modifier = Modifier.size(45.dp))
                            Text(text = "+")
                        }

                            Spacer(modifier = Modifier.size(20.dp))
                       // Spacer(modifier = Modifier.size(10.dp))
                        Row (horizontalArrangement = Arrangement.spacedBy(50.dp), modifier = Modifier.border(0.dp, color = Color.LightGray)){
                            Text(text = "How does incident reporting system work?")
                            Text(text = "+")
                        }
                }
                Spacer(modifier =Modifier.size(45.dp))
                Text(text ="Support", fontSize = 20.sp)
                Spacer(modifier = Modifier.size(20.dp))
                Card (modifier = Modifier
                    .fillMaxWidth()
                    .background(cardColor)
                    .border(1.dp, color = Color.LightGray, shape = RectangleShape),
                    colors = CardDefaults.cardColors(Color.White)){
                    Text(text = "Get help with managing your Account", color = Color.Blue, textDecoration = TextDecoration.Underline)
                    Spacer(modifier = Modifier.size(15.dp))
                    Text(text ="Having Technical issues" , color = Color.Blue, textDecoration = TextDecoration.Underline)
                }
            }

        }
        }



    }


@Preview
@Composable
fun HelpPreview(){
    //HelpAndSupport(navController)
}