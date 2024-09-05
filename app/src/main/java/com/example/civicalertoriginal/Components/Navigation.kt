package com.example.civicalertoriginal.Components


import LogIn
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import civicalertoriginal.Screen.MakeReports
import com.example.civicalertoriginal.Screens.ContactUs
import com.example.civicalertoriginal.Screens.Dashboard
import com.example.civicalertoriginal.Screens.ForgotPassword
import com.example.civicalertoriginal.Screens.HelpAndSupport
import com.example.civicalertoriginal.Screens.Registration
import com.example.civicalertoriginal.Screens.UpdateProfile
import com.example.civicalertoriginal.Screens.ViewFullReport
import com.example.civicalertoriginal.Screens.ViewReports


@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun Navigation (){
    val navController = rememberNavController()
    NavHost(navController = navController, startDestination = "helpSupport") {
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
        composable("helpSupport"){
            HelpAndSupport(navController)
        }
        composable("viewReport/{reportId}") { backStackEntry ->
            val reportId = backStackEntry.arguments?.getString("reportId") ?: ""
            ViewFullReport(navController, reportId)
        }


    }
}