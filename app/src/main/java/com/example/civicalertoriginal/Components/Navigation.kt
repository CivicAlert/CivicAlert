package com.example.civicalertoriginal.Components

import LogIn
import android.os.Build
import androidx.annotation.RequiresApi
import civicalertoriginal.Screen.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.internal.composableLambda
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.civicalertoriginal.Screens.*


@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun Navigation (){
    val navController = rememberNavController()
    NavHost(navController = navController, startDestination = "Dashboard") {
        composable("registration"){
            Registration(navController)
        }
        /*composable("Login"){
           LogIn(navController)
        }*/
        composable("forgotPassword"){
            ForgotPassword(navController)
        }
        composable("login"){
            LogIn(navController)
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
        composable("helpSupport"){
            HelpAndSupport(navController)
        }
        composable("Camera"){
            CameraScreen(navController)
        }
    }
}