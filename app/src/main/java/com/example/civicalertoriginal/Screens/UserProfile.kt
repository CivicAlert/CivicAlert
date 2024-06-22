package com.example.civicalertoriginal.Screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.outlined.Info
import androidx.compose.material.icons.rounded.Call
import androidx.compose.material.icons.rounded.List
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun UserProfile (navController: NavController){
    Surface(color = Color.White) {
        Scaffold(topBar = {
            TopAppBar(title = {
                Row(
                    verticalAlignment = Alignment.Top,
                    horizontalArrangement = Arrangement.Center,
                    modifier = Modifier.fillMaxWidth()) {
                    Text(text = "MY PROFILE", fontSize = 50.sp , fontWeight = FontWeight.Bold)
                    
                }
            })
        }, bottomBar = {
            Row (verticalAlignment = Alignment.Bottom,
                horizontalArrangement = Arrangement.SpaceEvenly){
                Spacer(modifier = Modifier.size(25.dp))

                Box( modifier = Modifier.size(50.dp)) {
                    Column () {
                        Icon(imageVector = Icons.Filled.Home, contentDescription = "", modifier = Modifier.size(29.dp))
                        Text(text = "Home", fontSize = 10.sp)
                    }
                }
                Spacer(modifier = Modifier.size(50.dp))
                
                Box (modifier = Modifier.size(50.dp)){
                    Column () {
                        Icon(imageVector = Icons.Outlined.Info, contentDescription = "", modifier = Modifier.size(29.dp))
                        Text(text = "Make Report", fontSize = 10.sp)
                    }

                }
                Spacer(modifier = Modifier.size(50.dp))
                Box(modifier = Modifier.size(50.dp)){
                     Column {
                        Icon(imageVector = Icons.Rounded.List, contentDescription = "", modifier = Modifier.size(29.dp))
                         Text(text = "View Reports", fontSize = 10.sp)
                    }
                }
                Spacer(modifier = Modifier.size(50.dp))
                Box(modifier = Modifier.size(50.dp)){
                    Column () {
                        Icon(imageVector = Icons.Rounded.Call, contentDescription = "", modifier = Modifier.size(29.dp))
                        Text(text = "Emergency call", fontSize = 10.sp)
                    }
                }
               
            }
        }, content = { innerPadding ->

                Text(text = "Personal Information")
                Button(onClick = { /*TODO*/ }) {
                    
                }
                
            
        }
        )
    }

}
@Preview
@Composable
fun MyProfilePreview(){
    val navController = rememberNavController()
    UserProfile( navController)
    
}