package com.example.civicalertoriginal.Components


import civicalertoriginal.Screen.*
import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.civicalertoriginal.Screens.*


@Composable
fun Navigation (){
    val navController = rememberNavController()
    NavHost(navController = navController, startDestination = "Login") {
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
            Dashboard(navController)
        }
        composable("makeReports"){
            MakeReports(navController)
        }
        composable("userProfile"){
            UpdateProfile(navController)
        }
        composable("emergencyContacts"){
            ContactUs(navController)
        }
        composable("Viewreports"){
            ViewReports(navController)
        }
    }
}