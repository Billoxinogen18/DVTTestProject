package com.corporate.dvtweatherapp.android.network.network



import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.engine.android.*
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.client.request.*
import io.ktor.client.statement.*
import io.ktor.http.* // Import URLBuilder
import io.ktor.serialization.kotlinx.json.*
import kotlinx.serialization.json.Json
import android.util.Log
import com.corporate.dvtweatherapp.android.model.WeatherForecast


class WeatherApiService {

    private val client = HttpClient(Android) {
        install(ContentNegotiation) {
            json(Json {
                ignoreUnknownKeys = true
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
         val lat = query.split(",")[0]
        val lon = query.split(",")[1]
        //HTTPS protocal was available, clearType bypass is now defunct
        val url = "https://api.openweathermap.org/data/2.5/forecast?lat=$lat&lon=$lon&appid=$apiKey&units=metric"

        Log.e("WeatherApiService", "Request URL: $url")

        val response: HttpResponse = client.get(url)
        val responseBody: String = response.bodyAsText()
        Log.e("WeatherApiService", "Raw API Response: $responseBody")

        return response.body()
    }
}