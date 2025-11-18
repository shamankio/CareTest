package com.rustan.kompanioncaretest

import android.Manifest
import android.content.pm.PackageManager
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.consumeWindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawing
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.core.app.ActivityCompat
import androidx.navigation.compose.rememberNavController
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.rustan.splash.SplashScreen
import com.rustan.ui.theme.KompanionCareTestTheme
import com.rustan.weather.WeatherScreen
import com.rustan.weather.navigation.navigateToWeather

class MainActivity : ComponentActivity() {

    private lateinit var fusedLocationClient: FusedLocationProviderClient
    private var latitude by mutableStateOf<Double?>(null)
    private var longitude by mutableStateOf<Double?>(null)

    private val requestPermissionLauncher =
        registerForActivityResult(
            ActivityResultContracts.RequestMultiplePermissions()
        ) { permissions ->
            when {
                permissions.getOrDefault(Manifest.permission.ACCESS_FINE_LOCATION, false) -> {
                    // Precise location access granted.
                    getCurrentLocation()
                }
                permissions.getOrDefault(Manifest.permission.ACCESS_COARSE_LOCATION, false) -> {
                    // Only approximate location access granted.
                    getCurrentLocation()
                }
                else -> {
                    // No location access granted.
                    Log.d("MainActivity", "Location permission denied by user.")
                }
            }
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)

        requestPermissionLauncher.launch(arrayOf(
            Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission.ACCESS_COARSE_LOCATION
        ))

        setContent {
            KompanionCareTestTheme {
                var showSplash by remember { mutableStateOf(true) }

                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    val navController = rememberNavController()
                    if (showSplash) {
                        SplashScreen(
                            showSplashScreen = showSplash,
                            onSplashScreenComplete = { shouldShow ->
                                showSplash = shouldShow
                            }
                        )
                    } else {
                        val lat = latitude
                        val lon = longitude
                        if (lat != null && lon != null) {
                            AppNavHost(
                                navController = navController,
                                isPad = false,
                                modifier = Modifier
                                    .fillMaxSize()
                                    .consumeWindowInsets(innerPadding)
                                    .windowInsetsPadding(
                                        WindowInsets.safeDrawing,
                                    ),
                            )
                            navController.navigateToWeather(lat, lon)
                        } else {
                            Box(
                                modifier = Modifier.fillMaxSize(),
                                contentAlignment = Alignment.Center
                            ) {
                                CircularProgressIndicator()
                            }
                        }
                    }
                }
            }
        }
    }

    private fun getCurrentLocation() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED ||
            ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            fusedLocationClient.lastLocation
                .addOnSuccessListener { location ->
                    if (location != null) {
                        // Got the location
                        this.latitude = location.latitude
                        this.longitude = location.longitude
                        Log.d("MainActivity", "Current location: ${location.latitude}, ${location.longitude}")
                        // You can now use the location object
                    } else {
                        Log.d("MainActivity", "Last location is null")
                        this.latitude = 37.773972
                        this.longitude = -122.431297
                        // Handle case where location is null (e.g., location is turned off in settings)
                    }
                }
                .addOnFailureListener { e ->
                    Log.e("MainActivity", "Error getting location", e)
                    // Handle failure
                }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    KompanionCareTestTheme {
//        Greeting("Android")
    }
}
