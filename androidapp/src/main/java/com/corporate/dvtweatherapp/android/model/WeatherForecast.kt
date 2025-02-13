package com.corporate.dvtweatherapp.android.model;

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class WeatherForecast(
    @SerialName("current")
    val current: Current,
    @SerialName("forecast")
    val forecast: Forecast,
    @SerialName("location")
    val location: Location
)

@Serializable
data class Current(
    @SerialName("condition")
    val condition: Condition,
    @SerialName("feelslike_c")
    val feelslikeC: Double,
    @SerialName("feelslike_f")
    val feelslikeF: Double,
    @SerialName("gust_kph")
    val gustKph: Double,
    @SerialName("gust_mph")
    val gustMph: Double,
    @SerialName("humidity")
    val humidity: Int,
    @SerialName("is_day")
    val isDay: Int,
    @SerialName("last_updated")
    val lastUpdated: String,
    @SerialName("last_updated_epoch")
    val lastUpdatedEpoch: Int,
    @SerialName("precip_in")
    val precipIn: Double,
    @SerialName("precip_mm")
    val precipMm: Double,
    @SerialName("pressure_in")
    val pressureIn: Double,
    @SerialName("pressure_mb")
    val pressureMb: Double,
    @SerialName("temp_c")
    val tempC: Double,
    @SerialName("temp_f")
    val tempF: Double,
    @SerialName("uv")
    val uv: Double,
    @SerialName("vis_km")
    val visKm: Double,
    @SerialName("vis_miles")
    val visMiles: Double,
    @SerialName("wind_degree")
    val windDegree: Int,
    @SerialName("wind_dir")
    val windDir: String,
    @SerialName("wind_kph")
    val windKph: Double,
    @SerialName("wind_mph")
    val windMph: Double
)

@Serializable
data class Forecast(
    @SerialName("forecastday")
    val forecastday: List<Forecastday>
)

@Serializable
data class Location(
    @SerialName("country")
    val country: String,
    @SerialName("lat")
    val lat: Double,
    @SerialName("localtime")
    val localtime: String,
    @SerialName("localtime_epoch")
    val localtimeEpoch: Int,
    @SerialName("lon")
    val lon: Double,
    @SerialName("name")
    val name: String,
    @SerialName("region")
    val region: String,
    @SerialName("tz_id")
    val tzId: String
)

@Serializable
data class Astro(
    @SerialName("moon_illumination")
    val moonIllumination: String,
    @SerialName("moon_phase")
    val moonPhase: String,
    @SerialName("moonrise")
    val moonrise: String,
    @SerialName("moonset")
    val moonset: String,
    @SerialName("sunrise")
    val sunrise: String,
    @SerialName("sunset")
    val sunset: String
)

@Serializable
data class Condition(
    @SerialName("code")
    val code: Int,
    @SerialName("icon")
    val icon: String,
    @SerialName("text")
    val text: String
)

@Serializable
data class Day(
    @SerialName("avgtemp_c")
    val avgtempC: Double,
    @SerialName("avgtemp_f")
    val avgtempF: Double,
    @SerialName("avgvis_km")
    val avgvisKm: Double,
    @SerialName("avgvis_miles")
    val avgvisMiles: Double,
    @SerialName("condition")
    val condition: Condition,
    @SerialName("daily_chance_of_rain")
    val dailyChanceOfRain: Int,
    @SerialName("daily_chance_of_snow")
    val dailyChanceOfSnow: Int,
    @SerialName("daily_will_it_rain")
    val dailyWillItRain: Int,
    @SerialName("daily_will_it_snow")
    val dailyWillItSnow: Int,
    @SerialName("maxtemp_c")
    val maxtempC: Double,
    @SerialName("maxtemp_f")
    val maxtempF: Double,
    @SerialName("maxwind_kph")
    val maxwindKph: Double,
    @SerialName("maxwind_mph")
    val maxwindMph: Double,
    @SerialName("mintemp_c")
    val mintempC: Double,
    @SerialName("mintemp_f")
    val mintempF: Double,
    @SerialName("totalprecip_in")
    val totalprecipIn: Double,
    @SerialName("totalprecip_mm")
    val totalprecMm: Double,
    @SerialName("totalsnow_cm")
    val totalsnowCm: Double,
    @SerialName("uv")
    val uv: Double
)

@Serializable
data class Forecastday(
    @SerialName("astro")
    val astro: Astro,
    @SerialName("date")
    val date: String,
    @SerialName("date_epoch")
    val dateEpoch: Int,
    @SerialName("day")
    val day: Day,
    @SerialName("hour")
    val hour: List<Hour>
)

@Serializable
data class Hour(
    @SerialName("chance_of_rain")
    val chanceOfRain: Int,
    @SerialName("chance_of_snow")
    val chanceOfSnow: Int,
    @SerialName("cloud")
    val cloud: Int,
    @SerialName("condition")
    val condition: Condition,
    @SerialName("dewpoint_c")
    val dewpointC: Double,
    @SerialName("dewpoint_f")
    val dewpointF: Double,
    @SerialName("feelslike_c")
    val feelslikeC: Double,
    @SerialName("feelslike_f")
    val feelslikeF: Double,
    @SerialName("gust_kph")
    val gustKph: Double,
    @SerialName("gust_mph")
    val gustMph: Double,
    @SerialName("heatindex_c")
    val heatindexC: Double,
    @SerialName("heatindex_f")
    val heatindexF: Double,
    @SerialName("humidity")
    val humidity: Int,
    @SerialName("is_day")
    val isDay: Int,
    @SerialName("precip_in")
    val precipIn: Double,
    @SerialName("precip_mm")
    val precipMm: Double,
    @SerialName("pressure_in")
    val pressureIn: Double,
    @SerialName("pressure_mb")
    val pressureMb: Double,
    @SerialName("temp_c")
    val tempC: Double,
    @SerialName("temp_f")
    val tempF: Double,
    @SerialName("time")
    val time: String,
    @SerialName("time_epoch")
    val timeEpoch: Int,
    @SerialName("uv")
    val uv: Double,
    @SerialName("vis_km")
    val visKm: Double,
    @SerialName("vis_miles")
    val visMiles: Double,
    @SerialName("will_it_rain")
    val willItRain: Int,
    @SerialName("will_it_snow")
    val willItSnow: Int,
    @SerialName("wind_degree")
    val windDegree: Int,
    @SerialName("wind_dir")
    val windDir: String,
    @SerialName("wind_kph")
    val windKph: Double,
    @SerialName("wind_mph")
    val windMph: Double,
    @SerialName("windchill_c")
    val windchillC: Double,
    @SerialName("windchill_f")
    val windchillF: Double
)