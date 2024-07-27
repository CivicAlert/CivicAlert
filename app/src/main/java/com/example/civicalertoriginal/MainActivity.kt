package com.example.civicalertoriginal

import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.annotation.RequiresApi
import com.example.civicalertoriginal.Components.Navigation
import com.example.civicalertoriginal.ui.theme.CivicAlertOriginalTheme
import com.google.firebase.FirebaseApp
import com.mapbox.maps.MapInitOptions
import com.mapbox.maps.Mapbox
import com.mapbox.maps.MapView

class MainActivity : ComponentActivity() {
    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Initialize Firebase
        FirebaseApp.initializeApp(this)

        // Initialize Mapbox
        Mapbox.getInstance(this, getString(R.string.mapbox_access_token))

        // Alternatively, if you want to provide custom initialization options:
        val mapInitOptions = MapInitOptions(this, accessToken = getString(R.string.mapbox_access_token))
        val mapView = MapView(this, mapInitOptions)

        setContent {
            CivicAlertOriginalTheme {
                Navigation()
            }
        }
    }
}
