package com.example.civicalertoriginal.Screens

import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController


@Composable
fun DashBoard(navController: NavController){
    Surface (color = Color.White) {
    }
}

@Preview
@Composable
fun DashboardPreview(){
    DashBoard(navController = rememberNavController())
}
