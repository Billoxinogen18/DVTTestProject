package com.corporate.dvtweatherapp.android.viewmodel

import android.util.Log
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.corporate.dvtweatherapp.android.BuildConfig
import com.corporate.dvtweatherapp.android.model.ForecastdayUI
import com.corporate.dvtweatherapp.android.network.network.WeatherApiService
import com.corporate.dvtweatherapp.android.utils.WeatherCondition
import io.ktor.serialization.*
import kotlinx.coroutines.launch

data class WeatherState(
    val isLoading: Boolean = false,
    val forecasts: List<ForecastdayUI> = emptyList(),
    val error: String? = null
)

class WeatherViewModel(private val apiService: WeatherApiService) : ViewModel() {

    var weatherState = mutableStateOf(WeatherState())
        private set

    val currentCondition = mutableStateOf<WeatherCondition?>(null)

    constructor() : this(WeatherApiService())


    fun fetchWeatherData(query: String) {
        viewModelScope.launch {
            weatherState.value = weatherState.value.copy(isLoading = true)
            try {
                val weatherForecast = apiService.getWeatherForecast(
                    BuildConfig.OPENWEATHERMAP_API_KEY,
                    query,
                    5,
                    "no",
                    "no"
                )

                // --- Get Current Condition (from the *first* item in the list) ---
                if (weatherForecast.list.isNotEmpty()) {
                    currentCondition.value = when {
                        weatherForecast.list[0].weather[0].main.contains("clear", ignoreCase = true) -> WeatherCondition.SUNNY //The condition
                        weatherForecast.list[0].weather[0].main.contains("cloud", ignoreCase = true) -> WeatherCondition.CLOUDY
                        weatherForecast.list[0].weather[0].main.contains("rain", ignoreCase = true) -> WeatherCondition.RAINY
                        else -> WeatherCondition.SUNNY // Default
                    }
                }
                // --- Group Forecasts by Day ---
                val groupedForecasts = weatherForecast.list.groupBy {
                    it.dtTxt.substring(0, 10) // Extract yyyy-MM-dd
                }

                // --- Adapt to ForecastdayUI, taking the *average* temperature for each day ---
                val adaptedForecasts = groupedForecasts.map { (date, forecastsForDay) ->
                    val averageTemp = forecastsForDay.map { it.main.temp }.average()
                    val firstForecast = forecastsForDay.first() // Use the first entry for condition
                    ForecastdayUI(
                        date = date, // Use the extracted date
                        tempC = averageTemp,
                        conditionCode = firstForecast.weather[0].id.toInt() // Condition from first entry
                    )
                }
                weatherState.value = weatherState.value.copy(
                    isLoading = false,
                    forecasts = adaptedForecasts,
                    error = null
                )

                Log.d("WeatherViewModel", "Weather data: $weatherForecast")
            } catch (e: JsonConvertException) {
                weatherState.value = weatherState.value.copy(
                    isLoading = false,
                    error = "Error parsing weather data: ${e.message}"
                )
                Log.e("WeatherViewModel", "JSON Error", e)

            } catch (e: Exception) {
                weatherState.value = weatherState.value.copy(
                    isLoading = false,
                    error = e.message ?: "An unexpected error occurred."
                )
                Log.e("WeatherViewModel", "Error fetching weather data", e)
            }
        }
    }
}