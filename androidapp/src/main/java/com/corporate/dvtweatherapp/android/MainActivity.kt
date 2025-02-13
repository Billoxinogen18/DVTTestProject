package com.corporate.dvtweatherapp.android

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import com.corporate.dvtweatherapp.android.ui.theme.DVTWeatherAppTheme

import androidx.lifecycle.viewmodel.compose.viewModel
import com.corporate.dvtweatherapp.android.ui.WeatherScreen
import com.corporate.dvtweatherapp.android.utils.Utils
import com.corporate.dvtweatherapp.android.viewmodel.WeatherViewModel

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Utils.setContext(this)
        setContent {
            DVTWeatherAppTheme {
                val weatherViewModel: WeatherViewModel = viewModel()
                WeatherScreen(weatherViewModel)
            }
        }
    }
}