package com.corporate.dvtweatherapp.android.ui

import android.Manifest
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
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
import com.corporate.dvtweatherapp.android.R
import com.corporate.dvtweatherapp.android.utils.WeatherCondition
import com.corporate.dvtweatherapp.android.model.Forecastday
import com.corporate.dvtweatherapp.android.ui.theme.color.CardBackgroundColor
import com.corporate.dvtweatherapp.android.ui.theme.color.TextColor
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.rememberMultiplePermissionsState

import com.corporate.dvtweatherapp.android.utils.Utils.Companion.getDayOfWeek
import com.corporate.dvtweatherapp.android.utils.Utils.Companion.getCurrentLocation
import com.corporate.dvtweatherapp.android.viewmodel.WeatherState

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
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
        ) {
            Text(
                text = "5 Day Forecast",
                style = MaterialTheme.typography.titleLarge,
                color = TextColor,
                modifier = Modifier.align(Alignment.CenterHorizontally)
            )

            Spacer(modifier = Modifier.height(16.dp))

            if (weatherState.isLoading) {
                CircularProgressIndicator(modifier = Modifier.align(Alignment.CenterHorizontally))
            } else if (weatherState.error != null) {
                Text("Error: ${weatherState.error}", color = Color.Red)
            } else {
                LazyColumn {
                    items(weatherState.forecasts) { forecast ->
                        WeatherCard(forecast)
                    }
                }
            }
        }
    }
}


@Composable
fun WeatherCard(forecast: Forecastday) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
        shape = RoundedCornerShape(8.dp),
        colors = CardDefaults.cardColors(containerColor = CardBackgroundColor)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = getDayOfWeek(forecast.date),
                style = MaterialTheme.typography.labelMedium,
                color = TextColor
            )

            WeatherIcon(forecast.day.condition.code) // Helper function to map weather conditions from open weather API to drawable resources

            Text(
                text = "${forecast.day.avgtempC.toInt()}°",
                style = MaterialTheme.typography.displayLarge,
                color = TextColor
            )
        }
    }
}

// Helper function to map condition codes to drawable resources, auto renamed using mac's mass rename finder feature
@Composable
fun WeatherIcon(conditionCode: Int) {
    val iconRes = when (conditionCode) {
        1000 -> R.drawable.ic_weather_icon_1 // Sunny
        1003 -> R.drawable.ic_weather_icon_5 // Partly cloudy
        1006 -> R.drawable.ic_weather_icon_10 // Cloudy
        1009 -> R.drawable.ic_weather_icon_7 // Overcast (mostly cloudy)
        1030 -> R.drawable.ic_weather_icon_7 // Mist  (mostly cloudy)
        1063 -> R.drawable.ic_weather_icon_6 // Patchy rain possible
        1066 -> R.drawable.ic_weather_icon_22 // Patchy snow possible
        1069 -> R.drawable.ic_weather_icon_23 // Patchy sleet possible
        1072 -> R.drawable.ic_weather_icon_6 // Patchy freezing drizzle possible
        1087 -> R.drawable.ic_weather_icon_12 // Thundery outbreaks possible
        1114 -> R.drawable.ic_weather_icon_22// Blowing snow
        1117 -> R.drawable.ic_weather_icon_22 // Blizzard
        1135 -> R.drawable.ic_weather_icon_7// Fog (mostly cloudy)
        1147 -> R.drawable.ic_weather_icon_7 // Freezing fog (mostly cloudy)
        1150 -> R.drawable.ic_weather_icon_6// Patchy light drizzle
        1153 -> R.drawable.ic_weather_icon_6 // Light drizzle
        1168 -> R.drawable.ic_weather_icon_6// Freezing drizzle
        1171 -> R.drawable.ic_weather_icon_18 // Heavy freezing drizzle
        1180 -> R.drawable.ic_weather_icon_6// Patchy light rain
        1183 -> R.drawable.ic_weather_icon_20 // Light rain
        1186 -> R.drawable.ic_weather_icon_6// Moderate rain at times
        1189 -> R.drawable.ic_weather_icon_20// Moderate rain
        1192 -> R.drawable.ic_weather_icon_18// Heavy rain at times
        1195 -> R.drawable.ic_weather_icon_18// Heavy rain
        1198 -> R.drawable.ic_weather_icon_6 // Light freezing rain
        1201 -> R.drawable.ic_weather_icon_18// Moderate or heavy freezing rain
        1204 -> R.drawable.ic_weather_icon_23// Light sleet
        1207 -> R.drawable.ic_weather_icon_23 // Moderate or heavy sleet
        1210 -> R.drawable.ic_weather_icon_22// Patchy light snow
        1213 -> R.drawable.ic_weather_icon_22 // Light snow
        1216 -> R.drawable.ic_weather_icon_22// Patchy moderate snow
        1219 -> R.drawable.ic_weather_icon_22// Moderate snow
        1222 -> R.drawable.ic_weather_icon_22// Patchy heavy snow
        1225 -> R.drawable.ic_weather_icon_14 // Heavy snow
        1237 -> R.drawable.ic_weather_icon_23// Ice pellets
        1240 -> R.drawable.ic_weather_icon_20// Light rain shower
        1243 -> R.drawable.ic_weather_icon_18// Moderate or heavy rain shower
        1246 -> R.drawable.ic_weather_icon_18// Torrential rain shower
        1249 -> R.drawable.ic_weather_icon_23// Light sleet showers
        1252 -> R.drawable.ic_weather_icon_23// Moderate or heavy sleet showers
        1255 -> R.drawable.ic_weather_icon_22// Light snow showers
        1258 -> R.drawable.ic_weather_icon_14// Moderate or heavy snow showers
        1261 -> R.drawable.ic_weather_icon_23// Light showers of ice pellets
        1264 -> R.drawable.ic_weather_icon_23// Moderate or heavy showers of ice pellets
        1273 -> R.drawable.ic_weather_icon_12// Patchy light rain with thunder
        1276 -> R.drawable.ic_weather_icon_18// Moderate or heavy rain with thunder
        1279 -> R.drawable.ic_weather_icon_22// Patchy light snow with thunder
        1282 -> R.drawable.ic_weather_icon_14// Moderate or heavy snow with thunder

        else -> R.drawable.ic_weather_icon_1 // Default: Sunny
    }

    Image(
        painter = painterResource(id = iconRes),
        contentDescription = "Weather Icon",
        modifier = Modifier.size(24.dp)
    )
}