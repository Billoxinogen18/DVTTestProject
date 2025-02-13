package com.corporate.dvtweatherapp.android.utils

import android.annotation.SuppressLint
import android.location.Location
import android.util.Log
import java.text.SimpleDateFormat
import java.util.*
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine
import com.google.android.gms.location.LocationServices
import com.google.android.gms.location.FusedLocationProviderClient

class Utils {

    companion object {
        @SuppressLint("MissingPermission")
        suspend fun getCurrentLocation(): Location? = suspendCoroutine { continuation ->
              val fusedLocationClient: FusedLocationProviderClient =
                LocationServices.getFusedLocationProviderClient(getContext()) // Replace with your context

            fusedLocationClient.lastLocation
                .addOnSuccessListener { location: Location? ->
                    continuation.resume(location)
                }
                .addOnFailureListener {
                    Log.e("LocationError", "Error getting location", it)
                    continuation.resume(null) // Resume with null on failure
                }
        }

        @SuppressLint("StaticFieldLeak")
        private var context: android.content.Context? = null

        fun setContext(context: android.content.Context) {
            this.context = context.applicationContext
        }

        fun getContext(): android.content.Context {
            return context
                ?: throw IllegalStateException("Context not set. Call Utils.setContext(context) first.")
        }
        fun getDayOfWeek(dateString: String): String {
            val inputFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
            val date = inputFormat.parse(dateString)
            val outputFormat = SimpleDateFormat("EEEE", Locale.getDefault()) // "EEEE" gives full day name
            return date?.let { outputFormat.format(it) } ?: ""
        }
    }

}