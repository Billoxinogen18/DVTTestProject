package com.corporate.dvtweatherapp.android.viewmodel

import android.util.Log
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.corporate.dvtweatherapp.android.model.Forecastday
import com.corporate.dvtweatherapp.android.network.network.WeatherApiService
import com.corporate.dvtweatherapp.android.BuildConfig
import com.corporate.dvtweatherapp.android.utils.WeatherCondition
import kotlinx.coroutines.launch

data class WeatherState(
    val isLoading: Boolean = false,
    val forecasts: List<Forecastday> = emptyList(),
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
                val currentCond =  weatherForecast.current.condition.text
                currentCondition.value =  when {
                    currentCond.contains("sun", ignoreCase = true) -> WeatherCondition.SUNNY
                    currentCond.contains("cloud", ignoreCase = true) -> WeatherCondition.CLOUDY
                    currentCond.contains("rain", ignoreCase = true) -> WeatherCondition.RAINY
                    else -> WeatherCondition.SUNNY //Default
                }
                weatherState.value = weatherState.value.copy(
                    isLoading = false,
                    forecasts = weatherForecast.forecast.forecastday,
                    error = null
                )

                Log.d("WeatherViewModel", "Weather data: $weatherForecast")
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