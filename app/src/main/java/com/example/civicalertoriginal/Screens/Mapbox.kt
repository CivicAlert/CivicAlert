package com.example.civicalertoriginal.Screens

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.navigation.NavController
import com.mapbox.geojson.Point
import com.mapbox.maps.CameraOptions
import com.mapbox.maps.MapView
import com.mapbox.maps.Style
import com.mapbox.maps.plugin.animation.flyTo
import com.mapbox.maps.plugin.annotation.annotations
import com.mapbox.maps.plugin.annotation.generated.PointAnnotation
import com.mapbox.maps.plugin.annotation.generated.PointAnnotationManager
import com.mapbox.maps.plugin.annotation.generated.PointAnnotationOptions
import com.mapbox.maps.plugin.annotation.generated.createPointAnnotationManager
import com.mapbox.maps.plugin.gestures.gestures
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import okhttp3.OkHttpClient
import okhttp3.Request
import org.json.JSONObject
import java.io.IOException

@Composable
fun MapBox(
    location: String,
    onLocationSelected: (String, Double, Double) -> Unit,
    onAddressFetched: (String) -> Unit // Add a callback for the reverse geocoded address
) {
    var searchQuery by remember { mutableStateOf("") }
    var searchResults by remember { mutableStateOf(listOf<Pair<String, Point>>()) }
    var selectedLocation by remember { mutableStateOf<Point?>(null) }
    var mapView: MapView? by remember { mutableStateOf(null) }
    var pointAnnotationManager: PointAnnotationManager? by remember { mutableStateOf(null) }
    var currentMarker: PointAnnotation? by remember { mutableStateOf(null) }

    Column {
        // Search bar
        TextField(
            value = searchQuery,
            onValueChange = {
                searchQuery = it
                if (searchQuery.isNotEmpty()) {
                    performGeocoding(searchQuery) { results ->
                        searchResults = results
                    }
                }
            },
            label = { Text("Search location") },
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp),
            singleLine = true
        )

        // Show search results
        searchResults.forEach { result ->
            TextButton(onClick = {
                selectedLocation = result.second
                searchQuery = result.first
                onLocationSelected(result.first, result.second.latitude(), result.second.longitude())  // Update location

                // Reverse geocode the coordinates to get the address
                performReverseGeocoding(result.second.latitude(), result.second.longitude()) { address ->
                    onAddressFetched(address)
                }

                // Update the map's camera position
                selectedLocation?.let { locationPoint ->
                    val cameraOptions = CameraOptions.Builder()
                        .center(locationPoint)
                        .zoom(14.0)
                        .build()
                    mapView?.getMapboxMap()?.flyTo(cameraOptions)

                    // Place a marker at the selected location
                    addMarker(locationPoint, pointAnnotationManager, currentMarker) {
                        currentMarker = it
                    }
                }
            }) {
                Text(result.first)
            }
        }

        // MapView with a click listener
        AndroidView(factory = { context ->
            MapView(context).apply {
                mapView = this // Store a reference to the MapView

                // Initialize the point annotation manager
                pointAnnotationManager = annotations.createPointAnnotationManager()

                // Load the map style and set up the gesture plugin
                getMapboxMap().loadStyleUri(Style.SATELLITE_STREETS) {

                    // Set up a click listener to pick a location on the map
                    gestures.addOnMapClickListener { point ->
                        selectedLocation = point
                        searchQuery = "${point.latitude()}, ${point.longitude()}"
                        onLocationSelected(searchQuery, point.latitude(), point.longitude())  // Update location

                        // Reverse geocode the coordinates to get the address
                        performReverseGeocoding(point.latitude(), point.longitude()) { address ->
                            onAddressFetched(address)
                        }

                        // Place a marker at the clicked location
                        addMarker(point, pointAnnotationManager, currentMarker) {
                            currentMarker = it
                        }
                        true
                    }

                    // Optionally, set initial camera position
                    selectedLocation?.let { locationPoint ->
                        val cameraOptions = CameraOptions.Builder()
                            .center(locationPoint)
                            .zoom(12.0)
                            .build()
                        getMapboxMap().flyTo(cameraOptions)
                    }
                }
            }
        }, modifier = Modifier.fillMaxSize())
    }
}

// Function to perform reverse geocoding
fun performReverseGeocoding(latitude: Double, longitude: Double, onResult: (String) -> Unit) {
    CoroutineScope(Dispatchers.IO).launch {
        // Replace with your actual Mapbox access token
        val accessToken = "your-mapbox-access-token-here"
        val url = "https://api.mapbox.com/geocoding/v5/mapbox.places/$longitude,$latitude.json?access_token=$accessToken"

        val request = Request.Builder()
            .url(url)
            .build()

        try {
            client.newCall(request).execute().use { response ->
                if (!response.isSuccessful) throw IOException("Unexpected code $response")

                // Parse JSON response
                val responseBody = response.body?.string()
                if (!responseBody.isNullOrEmpty()) {
                    val json = JSONObject(responseBody)
                    val features = json.getJSONArray("features")
                    if (features.length() > 0) {
                        val address = features.getJSONObject(0).getString("place_name")
                        withContext(Dispatchers.Main) {
                            onResult(address)
                        }
                    }
                }
            }
        } catch (e: IOException) {
            e.printStackTrace()
        }
    }
}


@SuppressLint("MissingPermission")
fun addMarker(
    location: Point,
    pointAnnotationManager: PointAnnotationManager?,
    currentMarker: PointAnnotation?,
    onMarkerAdded: (PointAnnotation) -> Unit
) {
    // Remove the previous marker if it exists
    currentMarker?.let {
        pointAnnotationManager?.delete(it)
    }

    // Create a new marker at the specified location
    val pointAnnotationOptions = PointAnnotationOptions()
        .withPoint(location)
        .withIconImage("marker-icon") // Replace with your custom marker icon

    val newMarker = pointAnnotationManager?.create(pointAnnotationOptions)
    newMarker?.let { onMarkerAdded(it) }
}

val client = OkHttpClient()

fun performGeocoding(query: String, onResult: (List<Pair<String, Point>>) -> Unit) {
    CoroutineScope(Dispatchers.IO).launch {
        val results = mutableListOf<Pair<String, Point>>()

        // Replace with your actual Mapbox access token
        val accessToken = "sk.eyJ1Ijoibnlpa29kZWFydGtpZCIsImEiOiJjbTA2ZjBkemowdDBsMmtzYnppMnl3Mno5In0._msAzoio9GC2Abrzshe05w"
        val url =
            "https://api.mapbox.com/geocoding/v5/mapbox.places/$query.json?access_token=$accessToken"

        val request = Request.Builder()
            .url(url)
            .build()

        try {
            client.newCall(request).execute().use { response ->
                if (!response.isSuccessful) throw IOException("Unexpected code $response")

                // Parse JSON response
                val responseBody = response.body?.string()
                if (!responseBody.isNullOrEmpty()) {
                    val json = JSONObject(responseBody)
                    val features = json.getJSONArray("features")

                    for (i in 0 until features.length()) {
                        val feature = features.getJSONObject(i)
                        val placeName = feature.getString("place_name")
                        val coordinates =
                            feature.getJSONObject("geometry").getJSONArray("coordinates")
                        val longitude = coordinates.getDouble(0)
                        val latitude = coordinates.getDouble(1)
                        results.add(placeName to Point.fromLngLat(longitude, latitude))
                    }
                }
            }
        } catch (e: IOException) {
            e.printStackTrace()
        }

        // Switch to the main thread to update the UI
        withContext(Dispatchers.Main) {
            onResult(results)
        }
    }
}
@Composable
fun MapBoxScreen(navController: NavController, onLocationSelected: (String) -> Unit) {
    MapBox(
        location = "",
        onLocationSelected = { name, lat, lng ->
            // Call the callback to return to the previous screen with the selected location
            onLocationSelected(name)
            navController.popBackStack() // Go back to the previous screen
        },
        onAddressFetched = { address ->
            // Optionally, you can handle address fetching here
        }
    )
}