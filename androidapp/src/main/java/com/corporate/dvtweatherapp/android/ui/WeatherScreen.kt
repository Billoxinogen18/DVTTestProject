package com.corporate.dvtweatherapp.android.ui

import android.Manifest
import androidx.compose.animation.Crossfade
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Divider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.corporate.dvtweatherapp.android.viewmodel.WeatherViewModel
import com.corporate.dvtweatherapp.android.utils.WeatherCondition
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.rememberMultiplePermissionsState

import com.corporate.dvtweatherapp.android.utils.Utils.Companion.getDayOfWeek
import com.corporate.dvtweatherapp.android.utils.Utils.Companion.getCurrentLocation
import com.corporate.dvtweatherapp.android.model.ForecastdayUI
import com.corporate.dvtweatherapp.android.ui.theme.color.CardBackgroundColor
import com.corporate.dvtweatherapp.android.ui.theme.color.TextColor
import com.corporate.dvtweatherapp.android.viewmodel.WeatherState
import com.corporate.dvtweatherapp.android.R


@OptIn(ExperimentalPermissionsApi::class)
@Composable
fun WeatherScreen(viewModel: WeatherViewModel) {
    val permissionsState = rememberMultiplePermissionsState(
        permissions = listOf(
            Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission.ACCESS_COARSE_LOCATION
        )
    )

    LaunchedEffect(Unit) {
        if (permissionsState.allPermissionsGranted.not()) {
            permissionsState.launchMultiplePermissionRequest()
        }
    }

    LaunchedEffect(permissionsState.allPermissionsGranted) {
        if (permissionsState.allPermissionsGranted) {
            val location = getCurrentLocation()
            if (location != null) {
                viewModel.fetchWeatherData("${location.latitude},${location.longitude}")
            } else {
                viewModel.weatherState.value = WeatherState(error = "Could not retrieve location.")
            }
        }
    }

    val weatherState = viewModel.weatherState.value
    val currentCondition = viewModel.currentCondition.value

    val backgroundImage = when (currentCondition) {
        WeatherCondition.SUNNY -> painterResource(R.drawable.sunny_background)
        WeatherCondition.CLOUDY -> painterResource(R.drawable.cloudy_background)
        WeatherCondition.RAINY -> painterResource(R.drawable.rainy_background)
        else -> painterResource(R.drawable.forest_background)
    }

    Box(modifier = Modifier.fillMaxSize()) {
        Image(
            painter = backgroundImage,
            contentDescription = "Background",
            modifier = Modifier.fillMaxSize(),
            contentScale = ContentScale.Crop
        )

        Column(
            modifier = Modifier.fillMaxSize()
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(Color.Transparent)
                    .padding(start = 16.dp, top = 16.dp, bottom = 8.dp)
            ) {
                Text(
                    text = "5 Day Forecast",
                    style = MaterialTheme.typography.titleLarge,
                    color = TextColor,
                    modifier = Modifier.align(Alignment.TopStart)
                )
            }

            Divider(color = Color.White, thickness = 1.dp, modifier = Modifier.fillMaxWidth())

            Crossfade(targetState = weatherState.isLoading, label = "") { isLoading ->
                if (isLoading) {
                    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                        CircularProgressIndicator()
                    }
                } else if (weatherState.error != null) {
                    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                        Text("Error: ${weatherState.error}", color = Color.Red, modifier = Modifier.padding(16.dp))
                    }
                } else {
                    LazyColumn(
                        modifier = Modifier.padding(horizontal = 24.dp),
                        verticalArrangement = Arrangement.spacedBy(8.dp),
                        contentPadding = PaddingValues(top = 24.dp) // Add top padding here
                    ) {
                        items(weatherState.forecasts) { forecast: ForecastdayUI ->
                            WeatherCard(forecast)
                        }
                    }
                }
            }
        }
    }
}


@Composable
fun WeatherCard(forecast: ForecastdayUI) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
        shape = RoundedCornerShape(8.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
                .heightIn(min = 58.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(
                horizontalAlignment = Alignment.Start,
                modifier = Modifier.weight(1f)
            ) {
                Text(
                    text = getDayOfWeek(forecast.date),
                    style = MaterialTheme.typography.labelMedium,
                    color = Color.Black,
                )
                Spacer(modifier = Modifier.height(4.dp))
                WeatherIcon(forecast.conditionCode)
            }

            Text(
                text = "${forecast.tempC.toInt()}°",
                style = MaterialTheme.typography.displayLarge,
                color = Color.Black,
            )
        }
    }
}

// Helper function to map condition codes to drawable resources(Keeping names to know which is which)
@Composable
fun WeatherIcon(conditionCode: Int) {
    val iconRes = when (conditionCode) {
        in 200..232 -> R.drawable.ic_weather_icon_12 // Thunderstorm
        in 300..321 -> R.drawable.ic_weather_icon_6  // Drizzle
        in 500..504 -> R.drawable.ic_weather_icon_20 // Rain (moderate to heavy)
        511 -> R.drawable.ic_weather_icon_18 // Freezing rain
        in 520..531 -> R.drawable.ic_weather_icon_18 // Shower rain
        in 600..622 -> R.drawable.ic_weather_icon_22 // Snow
        in 701..781 -> R.drawable.ic_weather_icon_7  // Atmosphere (mist, fog, etc.)
        800 -> R.drawable.ic_weather_icon_1 // Clear sky (sunny)
        801 -> R.drawable.ic_weather_icon_5 // Few clouds
        802 -> R.drawable.ic_weather_icon_10 // Scattered clouds
        803 -> R.drawable.ic_weather_icon_7 // Broken clouds
        804 -> R.drawable.ic_weather_icon_7// Overcast clouds
        else -> R.drawable.ic_weather_icon_1 // Default: Sunny
    }

    Image(
        painter = painterResource(id = iconRes),
        contentDescription = "Weather Icon",
        modifier = Modifier.size(24.dp)
    )
}