package com.example.civicalertoriginal.Components

import CivicAlert.Screen.MakeReports
import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.civicalertoriginal.Screens.DashBoard
import com.example.civicalertoriginal.Screens.ForgotPassword
import com.example.civicalertoriginal.Screens.LogIn
import com.example.civicalertoriginal.Screens.Registration


@Composable
fun Navigation (){
    val navController = rememberNavController()
    NavHost(navController = navController, startDestination = "logIn" +
            "" +
            "") {
        composable("registration"){
            Registration(navController)
        }
        composable("Login"){
            LogIn(navController)
        }
        composable("forgotPassword"){
            ForgotPassword(navController)
        }
        composable("Dashboard"){
            DashBoard(navController)
        }
        composable("makeReports"){
            MakeReports(navController)
        }

    }
}