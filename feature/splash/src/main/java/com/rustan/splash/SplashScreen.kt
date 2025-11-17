package com.rustan.splash

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.rustan.ui.theme.Orange
import kotlinx.coroutines.delay

@Composable
fun SplashScreen(
    showSplashScreen: Boolean,
    onSplashScreenComplete: (Boolean) -> Unit
) {
    val animationDuration = 3000L // 3 seconds in milliseconds
    var startAnimation by remember { mutableStateOf(false) }
    val durationMillis = (animationDuration - 1000).toInt()
    val animatedLogoScale by animateFloatAsState(
        targetValue = if (startAnimation) 1.0f else 0.8f,
        label = "scale",
        animationSpec = tween(durationMillis = durationMillis)
    )
    val animatedLogoOpacity by animateFloatAsState(
        targetValue = if (startAnimation) 1.0f else 0.5f,
        label = "alpha",
        animationSpec = tween(durationMillis = durationMillis)
    )

    LaunchedEffect(key1 = true) {
        startAnimation = true
        // Wait for total duration and then hide splash screen
        delay(animationDuration)
        onSplashScreenComplete(false)
    }

    if (showSplashScreen) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.White)
        ) {
            Column(
                modifier = Modifier.fillMaxSize(),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                Spacer(modifier = Modifier.weight(1f))
                Icon(
                    painter = painterResource(id = R.drawable.cloud_sun_fill),
                    contentDescription = "Weather Icon",
                    modifier = Modifier
                        .size(100.dp)
                        .graphicsLayer(
                            scaleX = animatedLogoScale,
                            scaleY = animatedLogoScale,
                            alpha = animatedLogoOpacity
                        ),
                    tint = Orange
                )
                Text(
                    text = "Open Weather",
                    fontSize = 32.sp, // equivalent to .largeTitle
                    fontWeight = FontWeight.Bold,
                    color = Orange,
                    modifier = Modifier.padding(top = 16.dp)
                )

                Spacer(modifier = Modifier.weight(1f))

                Text(
                    text = "Powered by OpenWeather",
                    fontSize = 12.sp, // equivalent to .footnote
                    color = Color.Gray,
                    modifier = Modifier.padding(bottom = 24.dp)
                )
            }
        }
    }
}

@Preview
@Composable
fun SplashScreenPreview() {
    SplashScreen(showSplashScreen = true, onSplashScreenComplete = {})
}
