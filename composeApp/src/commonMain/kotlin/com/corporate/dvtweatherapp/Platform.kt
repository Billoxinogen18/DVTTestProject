package com.corporate.dvtweatherapp

interface Platform {
    val name: String
}

expect fun getPlatform(): Platform