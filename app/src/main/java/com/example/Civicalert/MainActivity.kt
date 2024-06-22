package com.example.Civicalert

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import com.example.Civicalert.Components.Navigation
import com.example.Civicalert.ui.theme.CivicAlertOriginalTheme
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


