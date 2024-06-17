package com.example.civicalertoriginal.Screens

import android.annotation.SuppressLint
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.materialIcon
import androidx.compose.material.icons.outlined.Info
import androidx.compose.material.icons.rounded.AccountCircle
import androidx.compose.material.icons.rounded.Person
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.civicalertoriginal.Components.Cards
import com.example.civicalertoriginal.R



@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DashBoard(navController: NavController) {
    Surface(color = Color.White) {
        Scaffold(topBar = {
            TopAppBar(title = {
                Row(
                    verticalAlignment = Alignment.Top,
                    horizontalArrangement = Arrangement.SpaceBetween,
                    modifier = Modifier.fillMaxWidth()) {
                    Image(
                        painter = painterResource(id = R.drawable.logo), contentDescription = "",
                         modifier = Modifier.size(100.dp)
                    )
                    Icon(
                        imageVector = Icons.Rounded.AccountCircle, contentDescription = "",
                        modifier = Modifier.size(100.dp)
                    )
                }
            })
        }, bottomBar = {
           Row (verticalAlignment = Alignment.Bottom,
               horizontalArrangement = Arrangement.SpaceAround){
               Icon(imageVector = Icons.Filled.Home, contentDescription = "", modifier = Modifier.size(50.dp))
               Spacer(modifier = Modifier.size(140.dp))
               Icon(imageVector = Icons.Outlined.Info, contentDescription = "", modifier = Modifier.size(50.dp))
               Spacer(modifier = Modifier.size(140.dp))
               Icon(imageVector = Icons.Rounded.Person, contentDescription = "", modifier = Modifier.size(50.dp))
           }
        }, content = { innerPadding ->
            Column {
                Cards(value = "View all new reports")
                Row {
                    Cards(value = "REPORT AN INCIDENT")
                    Cards(value = "EMERGENCY CONTACTS")
                }
                Cards(value = "HELP & SUPPORT")
            }
        }
        )
    }
}


