package com.corporate.dvtweatherapp.android.utils

import android.annotation.SuppressLint
import android.content.Context
import android.location.Location
import android.os.Looper
import android.util.Log
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationResult
import com.google.android.gms.location.LocationServices
import com.google.android.gms.location.Priority
import java.text.SimpleDateFormat
import java.util.*
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine

class Utils {

    companion object {
        @SuppressLint("StaticFieldLeak")
        private var context: Context? = null

        fun setContext(context: Context) {
            Companion.context = context.applicationContext
        }

        fun getContext(): Context {
            return context
                ?: throw IllegalStateException("Context not set. Call Utils.setContext(context) first.")
        }

        @SuppressLint("MissingPermission")
        suspend fun getCurrentLocation(): Location? = suspendCoroutine { continuation ->
            val fusedLocationClient: FusedLocationProviderClient =
                LocationServices.getFusedLocationProviderClient(getContext())

            fusedLocationClient.lastLocation
                .addOnSuccessListener { location: Location? ->
                    if (location != null) {
                        Log.e("Location", "Last known location: ${location.latitude}, ${location.longitude}")
                        continuation.resume(location)
                    } else {
                        Log.e("Location", "Last known location is null, requesting updates")
                        val locationRequest = LocationRequest.create().apply {
                            priority = Priority.PRIORITY_HIGH_ACCURACY
                            interval = 10000
                            fastestInterval = 5000
                            maxWaitTime = 20000
                            numUpdates = 1
                        }

                        val locationCallback = object : LocationCallback() {
                            override fun onLocationResult(locationResult: LocationResult) {
                                locationResult.lastLocation?.let { location ->
                                    Log.e("Location", "New location: ${location.latitude}, ${location.longitude}")
                                    continuation.resume(location)
                                    fusedLocationClient.removeLocationUpdates(this)
                                } ?: run {
                                    Log.e("LocationError", "Could not retrieve location after update request.")

                                    val fallbackLocation = Location("").apply {
                                        latitude = -1.2921
                                        longitude = 36.8219
                                    }
                                    Log.e("Location", "Using fallback location: ${fallbackLocation.latitude}, ${fallbackLocation.longitude}")
                                    continuation.resume(fallbackLocation)
                                    fusedLocationClient.removeLocationUpdates(this)
                                }
                            }
                        }

                        fusedLocationClient.requestLocationUpdates(
                            locationRequest,
                            locationCallback,
                            Looper.getMainLooper()
                        )
                    }
                }
                .addOnFailureListener { e ->
                    Log.e("LocationError", "Error getting location", e)
                    // Fallback coordinates(Nairobi city, GPO)
                    val fallbackLocation = Location("").apply {
                        latitude = -1.2921
                        longitude = 36.8219
                    }
                    Log.e("Location", "Using fallback location: ${fallbackLocation.latitude}, ${fallbackLocation.longitude}")
                    continuation.resume(fallbackLocation)
                }
        }


        fun getDayOfWeek(dateString: String): String {
            val inputFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
            val date = inputFormat.parse(dateString)
            val outputFormat = SimpleDateFormat("EEEE", Locale.getDefault()) // "EEEE" gives full day name
            return date?.let { outputFormat.format(it) } ?: ""
        }
    }
}