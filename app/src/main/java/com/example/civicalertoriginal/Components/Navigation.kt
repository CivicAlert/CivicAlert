package com.example.civicalertoriginal.Components

import LogIn
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import civicalertoriginal.Screen.MakeReports
import com.example.civicalertoriginal.Screens.ContactUs
import com.example.civicalertoriginal.Screens.Dashboard
import com.example.civicalertoriginal.Screens.ForgotPassword
import com.example.civicalertoriginal.Screens.HelpAndSupport
import com.example.civicalertoriginal.Screens.MapBoxScreen
import com.example.civicalertoriginal.Screens.Registration
import com.example.civicalertoriginal.Screens.UpdateProfile
import com.example.civicalertoriginal.Screens.ViewReports

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun Navigation() {
    val navController = rememberNavController()

    // State for location details
    var locationName by remember { mutableStateOf("") }
    var latitude by remember { mutableStateOf(0.0) }
    var longitude by remember { mutableStateOf(0.0) }

    NavHost(navController = navController, startDestination = "makeReports") {
        composable("registration") {
            Registration(navController)
        }
        composable("forgotPassword") {
            ForgotPassword(navController)
        }
        composable("login") {
            LogIn(navController)
        }
        composable("Dashboard") {
            Dashboard(navController)
        }
        composable("makeReports") {
            MakeReports(navController)
        }
        composable("userProfile") {
            UpdateProfile(navController)
        }
        composable("emergencyContacts") {
            ContactUs(navController)
        }
        composable("Viewreports") {
            ViewReports(navController)
        }
        composable("helpSupport") {
            HelpAndSupport(navController)
        }
        composable("mapbox") {
            MapBoxScreen(navController = navController) { selectedLocation ->

                navController.previousBackStackEntry?.savedStateHandle?.set("selectedLocation", selectedLocation)
            }
        }

    }
}
