
package com.corporate.dvtweatherapp.android.viewmodel

import com.corporate.dvtweatherapp.android.model.*
import com.corporate.dvtweatherapp.android.network.WeatherApiService
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.*
import org.junit.After
import org.junit.Assert.*
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import androidx.arch.core.executor.testing.InstantTaskExecutorRule

@ExperimentalCoroutinesApi
class WeatherViewModelTest {

    @get:Rule
    val instantExecutorRule = InstantTaskExecutorRule()

    private lateinit var viewModel: WeatherViewModel
    private lateinit var mockApiService: WeatherApiService
    private val testDispatcher = StandardTestDispatcher()

    @Before
    fun setup() {
        Dispatchers.setMain(testDispatcher) // Set the main dispatcher
        mockApiService = mockk()
        viewModel = WeatherViewModel(mockApiService) // Inject the mock
    }
    @After
    fun tearDown() {
        Dispatchers.resetMain() // Reset the main dispatcher
    }


    @Test
    fun `fetchWeatherData successfully updates weatherState`() = runTest {
        // Arrange
        val fakeForecast = createFakeWeatherForecast()
        coEvery { mockApiService.getWeatherForecast(any(), any(), any(), any(), any()) } returns fakeForecast

        // Act
        viewModel.fetchWeatherData("dummy_api_key", "dummy_location")
        advanceUntilIdle() //Ensure coroutine completes

        // Assert
        assertFalse(viewModel.weatherState.value.isLoading)
        assertEquals(fakeForecast.forecast.forecastday, viewModel.weatherState.value.forecasts)
        assertNull(viewModel.weatherState.value.error)
    }


    @Test
    fun `fetchWeatherData with network error updates weatherState with error`() = runTest {
        // Arrange
        val errorMessage = "Network error"
        coEvery { mockApiService.getWeatherForecast(any(), any(), any(),any(), any()) } throws Exception(errorMessage)

        // Act
        viewModel.fetchWeatherData("dummy_api_key", "dummy_location")
        advanceUntilIdle() //Ensure coroutine completes

        // Assert
        assertFalse(viewModel.weatherState.value.isLoading)
        assertTrue(viewModel.weatherState.value.forecasts.isEmpty())
        assertEquals(errorMessage, viewModel.weatherState.value.error)
    }

    //Helper function to create fake forecast data
    private fun createFakeWeatherForecast(): WeatherForecast {
        val condition = Condition(code = 1000, icon = "//cdn.weatherapi.com/weather/64x64/day/113.png", text = "Sunny")
        val day = Day(avgtempC = 25.0, condition = condition, maxtempC = 30.0, mintempC = 20.0,
            maxwindKph = 10.0, totalprecipMm = 0.0, avgvisKm = 10.0, avghumidity = 60.0,
            dailyWillItRain = 0, dailyChanceOfRain = 0, dailyWillItSnow = 0, dailyChanceOfSnow = 0,
            uv = 7.0, avgtempF = 0.0, avgvisMiles = 0.0, maxtempF = 0.0, maxwindMph = 0.0, mintempF = 0.0, totalprecipIn = 0.0, totalsnowCm = 0.0
        )
        val astro = Astro(sunrise = "06:00 AM", sunset = "06:00 PM", moonrise = "12:00 AM", moonset = "12:00 PM", moonPhase = "New Moon", moonIllumination = "0")
        val hour = Hour(
            timeEpoch = 1700000000, time = "2023-11-15 00:00", tempC = 24.0, isDay = 1, condition = condition,
            windMph = 5.0, windKph = 8.0, windDegree = 180, windDir = "S", pressureMb = 1012.0,
            precipMm = 0.0, humidity = 65, cloud = 10, feelslikeC = 26.0, windchillC = 23.0,
            heatindexC = 27.0, dewpointC = 18.0, willItRain = 0, chanceOfRain = 0, willItSnow = 0,
            chanceOfSnow = 0, visKm = 10.0, gustMph = 6.0, gustKph = 9.0, uv = 7.0, tempF = 0.0,
            precipIn = 0.0, pressureIn = 0.0, feelslikeF = 0.0, windchillF = 0.0, heatindexF = 0.0, dewpointF = 0.0, visMiles = 0.0
        )
        val forecastDay = Forecastday(date = "2023-11-15", dateEpoch = 1700000000, day = day, astro = astro, hour = listOf(hour))
        val location = Location(name = "London", region = "London", country = "UK", lat = 51.52, lon = -0.11, tzId = "Europe/London", localtimeEpoch = 1700000000, localtime = "2023-11-15 12:00")

        return WeatherForecast(current = Current(
            lastUpdatedEpoch = 0,
            lastUpdated = "",
            tempC = 0.0,
            tempF = 0.0,
            isDay = 0,
            condition = condition,
            windMph = 0.0,
            windKph = 0.0,
            windDegree = 0,
            windDir = "",
            pressureMb = 0.0,
            pressureIn = 0.0,
            precipMm = 0.0,
            precipIn = 0.0,
            humidity = 0,
            cloud = 0,
            feelslikeC = 0.0,
            feelslikeF = 0.0,
            visKm = 0.0,
            visMiles = 0.0,
            uv = 0.0,
            gustMph = 0.0,
            gustKph = 0.0
        ), forecast = Forecast(forecastday = listOf(forecastDay)), location = location)
    }

    @Test
    fun `fetchWeatherData location null updates weatherState with error`() = runTest {
        //Arrange
        val fakeForecast = createFakeWeatherForecast()
        coEvery { mockApiService.getWeatherForecast(any(), any(), any(), any(), any()) } returns fakeForecast


        //Act
        viewModel.fetchWeatherData("dummy_key", "") //Empty location
        advanceUntilIdle()


        //Assert that forecasts is empty
        assertTrue(viewModel.weatherState.value.forecasts.isEmpty())
    }
}