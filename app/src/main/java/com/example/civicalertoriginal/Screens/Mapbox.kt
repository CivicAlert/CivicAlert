package com.example.civicalertoriginal.Screens

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.content.pm.PackageManager
import android.location.Location
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.app.ActivityCompat
import androidx.navigation.NavController
import com.google.android.gms.location.LocationServices
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
    onAddressFetched: (String) -> Unit,
    context: Context,
) {
    val activity = context as? Activity
    var searchQuery by remember { mutableStateOf("") }
    var searchResults by remember { mutableStateOf(listOf<Pair<String, Point>>()) }
    var selectedLocation by remember { mutableStateOf<Point?>(null) }
    var mapView: MapView? by remember { mutableStateOf(null) }
    var pointAnnotationManager: PointAnnotationManager? by remember { mutableStateOf(null) }
    var currentMarker: PointAnnotation? by remember { mutableStateOf(null) }

    LaunchedEffect(Unit) {
        activity?.let {
            // Ensure location permissions are granted before accessing location
            if (ActivityCompat.checkSelfPermission(
                    it,
                    android.Manifest.permission.ACCESS_FINE_LOCATION
                ) == PackageManager.PERMISSION_GRANTED
            ) {
                val fusedLocationProviderClient =
                    LocationServices.getFusedLocationProviderClient(it)

                fusedLocationProviderClient.lastLocation.addOnSuccessListener { location: Location? ->
                    if (location != null) {
                        val userLocation = Point.fromLngLat(30.97033, -25.47512)
                        selectedLocation = userLocation

                        // Center the camera to the user's current location
                        mapView?.getMapboxMap()?.setCamera(
                            CameraOptions.Builder()
                                .center(userLocation)
                                .zoom(14.0)
                                .build()
                        )

                        // Optionally, add a marker at the user's location
                        addMarker(userLocation, pointAnnotationManager, currentMarker) { marker ->
                            currentMarker = marker
                        }
                    }
                }
            } else {
                // Request permissions if not granted
                ActivityCompat.requestPermissions(
                    it,
                    arrayOf(android.Manifest.permission.ACCESS_FINE_LOCATION),
                    1
                )
            }
        }
    }

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
                onLocationSelected(result.first, result.second.latitude(), result.second.longitude())

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
                        onLocationSelected(searchQuery, point.latitude(), point.longitude())

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

fun performReverseGeocoding(latitude: Double, longitude: Double, onResult: (String) -> Unit) {
    CoroutineScope(Dispatchers.IO).launch {
        val accessToken = "pk.eyJ1IjoiMDcyNjc1ODM4MCIsImEiOiJjbG83YzB4YWswNTRiMmlxbTJ0NDR1cDI1In0.crbdqQtB4DRYK5eIx37wvw"
        val url = "https://api.mapbox.com/geocoding/v5/mapbox.places/$longitude,$latitude.json?access_token=$accessToken"

        val request = Request.Builder()
            .url(url)
            .build()

        try {
            client.newCall(request).execute().use { response ->
                if (!response.isSuccessful) throw IOException("Unexpected code $response")

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
    currentMarker?.let {
        pointAnnotationManager?.delete(it)
    }

    val pointAnnotationOptions = PointAnnotationOptions()
        .withPoint(location)
        .withIconImage("marker-icon")

    val newMarker = pointAnnotationManager?.create(pointAnnotationOptions)
    newMarker?.let { onMarkerAdded(it) }
}

val client = OkHttpClient()

fun performGeocoding(query: String, onResult: (List<Pair<String, Point>>) -> Unit) {
    CoroutineScope(Dispatchers.IO).launch {
        val results = mutableListOf<Pair<String, Point>>()
        val accessToken = "pk.eyJ1IjoiMDcyNjc1ODM4MCIsImEiOiJjbG83YzB4YWswNTRiMmlxbTJ0NDR1cDI1In0.crbdqQtB4DRYK5eIx37wvw"
        val url = "https://api.mapbox.com/geocoding/v5/mapbox.places/$query.json?access_token=$accessToken"

        val request = Request.Builder()
            .url(url)
            .build()

        try {
            client.newCall(request).execute().use { response ->
                if (!response.isSuccessful) throw IOException("Unexpected code $response")

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

        withContext(Dispatchers.Main) {
            onResult(results)
        }
    }
}

@Composable
fun MapBoxScreen(navController: NavController, onLocationSelected: (String) -> Unit){
    val context = LocalContext.current
    MapBox(
        location = "",
        onAddressFetched = { address ->

            navController.previousBackStackEntry?.savedStateHandle?.
            set("incidentLocationAddress", address)
            val sharedP= context.getSharedPreferences("app_prefs", Context.MODE_PRIVATE)
            val editor = sharedP.edit()
            editor.putString("incidentAddress", address)
            editor.apply()
        },
        onLocationSelected = { name, lat, lng ->
            // Call the callback to return to the previous screen with the selected location
            onLocationSelected(name)
            navController.popBackStack()
        },
        context = context
    )
}
