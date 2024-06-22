package com.example.civicalertoriginal

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import com.example.civicalertoriginal.Components.Navigation
import com.example.civicalertoriginal.ui.theme.CivicAlertOriginalTheme
import com.google.firebase.FirebaseApp

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        FirebaseApp.initializeApp(this)
        setContent {
            CivicAlertOriginalTheme {
                Navigation()

            }
        }
    }
}


