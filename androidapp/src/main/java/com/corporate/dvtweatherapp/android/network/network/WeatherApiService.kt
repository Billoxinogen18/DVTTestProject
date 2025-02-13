package com.corporate.dvtweatherapp.android.network.network


import com.corporate.dvtweatherapp.android.model.WeatherForecast
import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.engine.android.*
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.client.request.*
import io.ktor.serialization.kotlinx.json.*
import kotlinx.serialization.json.Json


class WeatherApiService {

    private val client = HttpClient(Android) {
        install(ContentNegotiation) {
            json(Json {
                ignoreUnknownKeys = true // Important: Handle unknown keys gracefully
                isLenient = true
            })
        }
    }

    suspend fun getWeatherForecast(
        apiKey: String,
        query: String,
        days: Int,
        aqi: String,
        alerts: String
    ): WeatherForecast {
        val url =
            "http://api.weatherapi.com/v1/forecast.json?key=$apiKey&q=$query&days=$days&aqi=$aqi&alerts=$alerts"
        return client.get(url).body()
    }
}