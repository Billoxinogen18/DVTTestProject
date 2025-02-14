
package com.corporate.dvtweatherapp.android

import com.corporate.dvtweatherapp.android.model.*
import com.corporate.dvtweatherapp.android.network.network.WeatherApiService
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
import com.corporate.dvtweatherapp.android.utils.WeatherCondition
import com.corporate.dvtweatherapp.android.viewmodel.WeatherViewModel

@ExperimentalCoroutinesApi
class WeatherViewModelTest {

    @get:Rule
    val instantExecutorRule = InstantTaskExecutorRule() // For LiveData (not strictly needed here, but good practice)

    private lateinit var viewModel: WeatherViewModel
    private lateinit var mockApiService: WeatherApiService
    private val testDispatcher = StandardTestDispatcher()

    @Before
    fun setup() {
        Dispatchers.setMain(testDispatcher) // Set the main dispatcher for coroutines
        mockApiService = mockk() // Create a mock of the WeatherApiService
        viewModel = WeatherViewModel(mockApiService) // Inject the mock into the ViewModel
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain() // Reset the main dispatcher after the test
    }

    @Test
    fun `fetchWeatherData successfully updates weatherState`() = runTest {
        // Arrange (Given) - Set up the test conditions
        val fakeForecast = createFakeWeatherForecast()
        coEvery { mockApiService.getWeatherForecast(any(), any(), any(), any(), any()) } returns fakeForecast

        // Act (When) - Execute the function being tested
        viewModel.fetchWeatherData("dummy_location")
        advanceUntilIdle() // Ensure all coroutines complete

        // Assert (Then) - Verify the expected outcome
        assertFalse(viewModel.weatherState.value.isLoading)
        assertEquals(5, viewModel.weatherState.value.forecasts.size) // Check forecast count (now 5)
        assertEquals("2025-02-14", viewModel.weatherState.value.forecasts[0].date) // Example: Check date
        assertEquals(21, viewModel.weatherState.value.forecasts[0].tempC.toInt()) // Check the *average* temp
        assertEquals(803, viewModel.weatherState.value.forecasts[0].conditionCode) //check condition code

        assertNull(viewModel.weatherState.value.error)       // Check for no error
        assertEquals(WeatherCondition.CLOUDY, viewModel.currentCondition.value) //Check the condition
    }
    @Test
    fun `fetchWeatherData with network error updates weatherState with error`() = runTest {
        // Arrange
        val errorMessage = "Network error"
        coEvery { mockApiService.getWeatherForecast(any(), any(), any(),any(), any()) } throws Exception(errorMessage)

        // Act
        viewModel.fetchWeatherData("dummy_location")
        advanceUntilIdle()

        // Assert
        assertFalse(viewModel.weatherState.value.isLoading)
        assertTrue(viewModel.weatherState.value.forecasts.isEmpty())
        assertEquals(errorMessage, viewModel.weatherState.value.error)
        assertNull(viewModel.currentCondition.value) //current condition should not change
    }

    @Test
    fun `fetchWeatherData with JSON error updates weatherState with error`() = runTest {
        // Arrange
        val errorMessage = "JSON parsing error"
        coEvery { mockApiService.getWeatherForecast(any(), any(), any(), any(), any()) } throws io.ktor.serialization.JsonConvertException(errorMessage)

        //Act
        viewModel.fetchWeatherData("dummy_location")
        advanceUntilIdle()

        //Assert
        assertFalse(viewModel.weatherState.value.isLoading)
        assertTrue(viewModel.weatherState.value.forecasts.isEmpty())
        assertEquals("Error parsing weather data: $errorMessage", viewModel.weatherState.value.error) //check error message

    }

    // Helper function to create fake forecast data (matching OpenWeatherMap structure)
    private fun createFakeWeatherForecast(): WeatherForecast {
        val clouds = Clouds(all = 66)
        val main = Main(
            feelsLike = 19.59,
            grndLevel = 837,
            humidity = 56,
            pressure = 1014,
            seaLevel = 1014,
            temp = 20.07,
            tempKf = 0.0,
            tempMax = 20.07,
            tempMin = 20.07
        )
        val main2 = Main( //Different values
            feelsLike = 19.59,
            grndLevel = 837,
            humidity = 56,
            pressure = 1014,
            seaLevel = 1014,
            temp = 22.07,
            tempKf = 0.0,
            tempMax = 20.07,
            tempMin = 20.07
        )
        val weather = listOf(Weather(description = "broken clouds", icon = "04n", id = 803, main = "Clouds"))
        val wind = Wind(deg = 72, gust = 11.21, speed = 5.95)
        val sys = Sys("n")

        // Create 5 forecast days -  CRITICAL:  Unique dates!
        val forecastList = listOf(
            Forecastday(clouds, 1739556000, "2025-02-14 18:00:00", main, 0.0, sys, 10000, weather, wind),
            Forecastday(clouds, 1739556000 + 86400, "2025-02-15 21:00:00", main2, 0.0, sys, 10000, weather, wind),
            Forecastday(clouds, 1739556000 + 86400 * 2, "2025-02-16 21:00:00", main.copy(temp = 23.5), 0.0, sys, 10000, weather, wind),
            Forecastday(clouds, 1739556000 + 86400 * 3, "2025-02-17 21:00:00", main.copy(temp = 18.2), 0.0, sys, 10000, weather, wind),
            Forecastday(clouds, 1739556000 + 86400 * 4, "2025-02-18 21:00:00", main.copy(temp = 24.1), 0.0, sys, 10000, weather, wind),
            //Add more to test grouping
            Forecastday(clouds, 1739556000 + 100, "2025-02-14 21:00:00", main.copy(temp = 23.07), 0.0, sys, 10000, weather, wind), //Different time same day
        )

        val city = City(
            coord = Coord(lat = -1.2833, lon = 36.8172),
            country = "KE",
            id = 184745,
            name = "Nairobi",
            population = 2750547,
            sunrise = 1739504546,
            sunset = 1739548297,
            timezone = 10800
        )

        return WeatherForecast(city, 40, "200", forecastList, 0)
    }
}