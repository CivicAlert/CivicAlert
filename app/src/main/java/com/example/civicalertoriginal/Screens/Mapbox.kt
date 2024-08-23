package com.example.civicalertoriginal.Screens

import androidx.compose.runtime.Composable
import androidx.compose.ui.viewinterop.AndroidView
import androidx.navigation.NavController
import com.mapbox.maps.CameraOptions
import com.mapbox.maps.MapView
import com.mapbox.maps.Style
import com.mapbox.maps.plugin.animation.MapAnimationOptions
import com.mapbox.maps.plugin.animation.flyTo

@Composable
fun MapBox(navController: NavController) {
    // Target coordinates for Mbombela
    val targetLatitude = -25.461651
    val targetLongitude = 30.929186

    AndroidView(
        factory = { context ->
            MapView(context).apply {
                mapboxMap.loadStyle(Style.MAPBOX_STREETS) {
                    // Set the camera position to the target coordinates
                    val cameraOptions = CameraOptions.Builder()
                        .center(com.mapbox.geojson.Point.fromLngLat(targetLongitude, targetLatitude))
                        .zoom(14.0) // Adjust the zoom level as needed
                        .build()

                    // Animate the camera movement to the target position
                    mapboxMap.flyTo(cameraOptions, MapAnimationOptions.Builder().build())
                }
            }
        },
        update = { mapView ->
            // Handle updates to MapView if needed
        },
    )
}
