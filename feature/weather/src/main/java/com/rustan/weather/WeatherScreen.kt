package com.rustan.weather

import android.content.res.Configuration
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Bolt
import androidx.compose.material.icons.filled.Cloud
import androidx.compose.material.icons.filled.CloudQueue
import androidx.compose.material.icons.filled.NightsStay
import androidx.compose.material.icons.filled.Opacity
import androidx.compose.material.icons.filled.WbSunny
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.rustan.domain.entities.Weather
import com.rustan.weather.util.DateTimeFormater
import org.koin.androidx.compose.koinViewModel
import java.util.Locale

@Composable
fun WeatherScreen(
    weatherViewModel: WeatherViewModel = koinViewModel(),
    onNavigateToStories: (Boolean) -> Unit,
    onRetryFetch: () -> Unit,
    isPad: Boolean = false,
    latitude: Double,
    longitude: Double
) {
    LaunchedEffect(Unit) {
        weatherViewModel.fetchData(latitude, longitude)
    }
    val skyTop = Color(0xFF6FA8FF)
    val skyBottom = Color(0xFF1E3C72)

    // Orientation detection
    val configuration = LocalConfiguration.current
    val isLandscape = configuration.orientation == Configuration.ORIENTATION_LANDSCAPE

    val state by weatherViewModel.weatherScreenState.collectAsStateWithLifecycle()

    Surface(color = Color.Transparent) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(
                    brush = Brush.verticalGradient(
                        colors = listOf(skyTop, skyBottom)
                    )
                )
        ) {
            when (state) {
                is WeatherScreenState.Success -> {
                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(horizontal = 16.dp)
                    ) {
                        Spacer(Modifier.height(50.dp))

                        val weather = (state as WeatherScreenState.Success).weatherState.weather
                        weather?.let { weather ->
                            if (isLandscape) {
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.spacedBy(32.dp),
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    WeatherMainInfoView(weather = weather, isPad = isPad)
                                    WeatherMetricsView(weather = weather, isPad = isPad)
                                }
                            } else {
                                Column(verticalArrangement = Arrangement.spacedBy(24.dp)) {
                                    WeatherMainInfoView(weather = weather, isPad = isPad)
                                    WeatherMetricsView(weather = weather, isPad = isPad)
                                }
                            }
                        }

                        Spacer(Modifier.height(32.dp))

                        Button(
                            onClick = { onNavigateToStories(true) },
                            modifier = Modifier
                                .fillMaxWidth()
                                .clip(RoundedCornerShape(12.dp))
                                .background(Color.Blue.copy(alpha = 0.1f))
                        ) {
                            Text(
                                text = "View Stories",
                                fontWeight = FontWeight.SemiBold,
                                color = Color.White,
                                fontSize = if (isPad) 28.sp else 18.sp,
                                modifier = Modifier.padding(8.dp)
                            )
                        }

                        Spacer(Modifier.height(24.dp))
                    }
                }

                is WeatherScreenState.Error -> {
                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(16.dp),
                        verticalArrangement = Arrangement.Center,
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        val message = when ((state as WeatherScreenState.Error).weatherError) {
                            WeatherError.Api -> "API error"
                            WeatherError.NetworkFailure -> "Network failure"
                            WeatherError.Unknown -> "Unknown error"
                        }
                        Text(
                            text = message,
                            color = Color.Red
                        )
                        Spacer(Modifier.height(16.dp))
                        RetryButton(
                            title = "Retry",
                            isPad = isPad,
                            onClick = onRetryFetch
                        )
                    }
                }

                is WeatherScreenState.Empty -> {
                    WeatherSkeletonView()
                }

                WeatherScreenState.Loading -> {
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

@Composable
private fun WeatherMainInfoView(weather: Weather, isPad: Boolean) {
    // Icon mapping similar to weatherIconMap
    val icon = remember(weather.weatherConditions?.first()?.icon) {
        when (weather.weatherConditions?.first()?.icon) {
            //            "01d", "01n" -> Icons.Filled.Sunny
            "02d", "02n" -> Icons.Filled.Cloud
            "03d", "03n" -> Icons.Filled.CloudQueue
            "04d", "04n" -> Icons.Filled.CloudQueue
            //            "09d", "09n" -> Icons.Filled.Rainy
            "10d", "10n" -> Icons.Filled.Opacity
            "11d", "11n" -> Icons.Filled.Bolt
            //            "13d", "13n" -> Icons.Filled.CloudySnowing
            //            "50d", "50n" -> Icons.Filled.Foggy
            else -> Icons.Filled.Cloud
        }
    }

    val lastUpdatedText = remember(weather.timestamp) {
        weather.timestamp?.let {
            DateTimeFormater().dateFormatteryyyyMMdd(it)
        } ?: ""
    }

    val conditionDescription = remember(weather.weatherConditions) {
        weather.weatherConditions?.takeIf { it.isNotEmpty() }?.joinToString {
            it.description?.replaceFirstChar { char -> if (char.isLowerCase()) char.titlecase(Locale.getDefault()) else char.toString() }
                ?: ""
        } ?: "No description"
    }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        if (lastUpdatedText.isNotEmpty()) {
            Text(
                text = lastUpdatedText,
                color = Color.White.copy(alpha = 0.9f),
                fontSize = if (isPad) 44.sp else 34.sp,
            )
        }
        Spacer(Modifier.height(12.dp))
        Text(
            text = weather.name ?: "Unknown Location",
            color = Color.White,
            fontSize = if (isPad) 44.sp else 34.sp,
            fontWeight = FontWeight.SemiBold
        )
        Spacer(Modifier.height(12.dp))
        Icon(
            imageVector = icon,
            contentDescription = conditionDescription,
            tint = Color.White,
            modifier = Modifier
                .padding(vertical = 8.dp)
                .size(if (isPad) 100.dp else 64.dp)
        )
        Spacer(Modifier.height(12.dp))
        Text(
            text = "${weather.temp?.toInt() ?: "--"}°C",
            color = Color.White,
            fontSize = if (isPad) 80.sp else 56.sp,
            fontWeight = FontWeight.Bold
        )
        Spacer(Modifier.height(12.dp))
        Text(
            text = conditionDescription,
            color = Color.White.copy(alpha = 0.9f),
            fontSize = if (isPad) 28.sp else 20.sp,
        )
    }
}

@Composable
private fun WeatherMetricsView(weather: Weather, isPad: Boolean) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        val windDirection = weather.windDeg?.run {
            val directions = listOf("N", "NE", "E", "SE", "S", "SW", "W", "NW")
            val index = ((this + 22.5) / 45.0).toInt() % 8
            directions[index]
        } ?: "Unknown Wind Direction"
        val metrics = remember(weather) {
            listOf(
                "FEELS LIKE" to "${weather.feelsLike ?: "--"}°C",
                "MIN" to "${weather.tempMin ?: "--"}°C",
                "MAX" to "${weather.tempMax ?: "--"}°C",
                "PRESSURE" to "${weather.pressure ?: "--"}hPa",
                "HUMIDITY" to "${weather.humidity ?: "--"}%",
                "WIND" to "${weather.windSpeed ?: "--"}% ${windDirection}",
                "CLOUDS" to "${weather.cloudsAll ?: "--"}",
                "COUNTRY" to (weather.sunCountry ?: "--")
            )
        }

        val columns = if (isPad) 2 else 2
        LazyVerticalGrid(
            columns = GridCells.Fixed(columns),
            modifier = Modifier
                .fillMaxWidth()
                .heightIn(min = 120.dp)
                .padding(8.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            items(metrics) { (title, value) ->
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(8.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = title,
                        color = Color.White.copy(alpha = 0.7f),
                        fontSize = if (isPad) 28.sp else 18.sp
                    )
                    Spacer(Modifier.height(4.dp))
                    Text(
                        text = value,
                        color = Color.White,
                        fontWeight = FontWeight.SemiBold,
                        fontSize = if (isPad) 28.sp else 18.sp
                    )
                }
            }
        }
        Spacer(Modifier.height(8.dp))
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.Center
        ) {
            // Sunrise
            val sunriseTime = weather.sunSunrise?.run { DateTimeFormater().timeFormatterHHmm(this) } ?: "--"
            IconAndText(
                text = sunriseTime,
                icon = Icons.Filled.WbSunny, // or use a custom sunrise icon
                fontSize = if (isPad) 28.sp else 18.sp,
                fontWeight = FontWeight.SemiBold,
                color = Color.White
            )

            Spacer(modifier = Modifier.width(32.dp))

            // Sunset
            val sunsetTime = weather.sunSet?.run { DateTimeFormater().timeFormatterHHmm(this) } ?: "--"
            IconAndText(
                text = sunsetTime,
                icon = Icons.Filled.NightsStay, // or use a custom sunset icon
                fontSize = if (isPad) 28.sp else 18.sp,
                fontWeight = FontWeight.SemiBold,
                color = Color.White
            )
        }
    }
}

@Composable
fun IconAndText(
    text: String,
    icon: ImageVector,
    fontSize: TextUnit,
    fontWeight: FontWeight = FontWeight.SemiBold,
    color: Color = Color.White
) {
    Row(
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            imageVector = icon,
            contentDescription = null,
            tint = color,
            modifier = Modifier.size(fontSize.value.dp)
        )
        Spacer(modifier = Modifier.width(4.dp))
        Text(
            text = text,
            fontSize = fontSize,
            fontWeight = fontWeight,
            color = color
        )
    }
}

@Composable
private fun RetryButton(title: String, isPad: Boolean, onClick: () -> Unit) {
    Button(
        onClick = onClick,
        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF2196F3)),
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(12.dp))
    ) {
        Text(
            text = title,
            color = Color.White,
            fontWeight = FontWeight.SemiBold,
            fontSize = if (isPad) 22.sp else 16.sp,
            modifier = Modifier.padding(vertical = 8.dp)
        )
    }
}

@Composable
private fun WeatherSkeletonView() {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // Simple placeholders
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(240.dp)
                .clip(RoundedCornerShape(20.dp))
                .background(Color.White.copy(alpha = 0.12f))
        )
        Spacer(Modifier.height(16.dp))
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(140.dp)
                .clip(RoundedCornerShape(20.dp))
                .background(Color.White.copy(alpha = 0.12f))
        )
    }
}
