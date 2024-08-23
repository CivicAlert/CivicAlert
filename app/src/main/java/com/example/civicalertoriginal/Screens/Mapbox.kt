package com.example.civicalertoriginal.Screens

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
import com.mapbox.geojson.Point
import com.mapbox.maps.CameraOptions
import com.mapbox.maps.MapView
import com.mapbox.maps.Style
import com.mapbox.maps.plugin.animation.flyTo
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import okhttp3.OkHttpClient
import okhttp3.Request
import org.json.JSONObject
import java.io.IOException

@Composable
fun MapBox(onLocationSelected: (String, Double, Double) -> Unit) {
    var searchQuery by remember { mutableStateOf("") }
    var searchResults by remember { mutableStateOf(listOf<Pair<String, Point>>()) }
    var selectedLocation by remember { mutableStateOf<Point?>(null) }
    var mapView: MapView? by remember { mutableStateOf(null) }

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

                // Update the map's camera position
                selectedLocation?.let { location ->
                    val cameraOptions = CameraOptions.Builder()
                        .center(location)
                        .zoom(14.0)
                        .build()
                    mapView?.getMapboxMap()?.flyTo(cameraOptions)
                }
            }) {
                Text(result.first)
            }
        }

        // MapView
        AndroidView(factory = { context ->
            MapView(context).apply {
                mapView = this // Store a reference to the MapView
                getMapboxMap().loadStyleUri(Style.SATELLITE_STREETS) {
                    // Optionally, set initial camera position
                    selectedLocation?.let { location ->
                        val cameraOptions = CameraOptions.Builder()
                            .center(location)
                            .zoom(12.0)
                            .build()
                        getMapboxMap().flyTo(cameraOptions)
                    }
                }
            }
        }, modifier = Modifier.fillMaxSize())
    }
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

