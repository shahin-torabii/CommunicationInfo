package com.example.communicationinfo.screens.Location

import android.Manifest
import android.os.Looper
import androidx.annotation.RequiresPermission
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.platform.LocalContext
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationResult
import com.google.android.gms.location.LocationServices
import com.google.android.gms.location.Priority
import com.google.android.gms.maps.model.LatLng


@RequiresPermission(allOf = [Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION])
@Composable
fun GetLocation() : LatLng?{

    val context = LocalContext.current

    val fusedLocationClient = remember {
        LocationServices.getFusedLocationProviderClient(context)
    }

    val accuracy by remember { mutableStateOf(Priority.PRIORITY_HIGH_ACCURACY) }

    var currentLocation by remember { mutableStateOf<LatLng?>(null) }

    val locationRequest = LocationRequest.Builder(accuracy, 3000)
            .setMinUpdateIntervalMillis(1500)
            .build()

    val locationCallback = remember {
        object : LocationCallback(){
            override fun onLocationResult(result: LocationResult) {
                super.onLocationResult(result)
                for(location in result.locations){
                    currentLocation = LatLng(location.latitude, location.altitude)
                }
            }
        }
    }

    fusedLocationClient.requestLocationUpdates(
        locationRequest,
        locationCallback,
        Looper.getMainLooper()
    )

    return currentLocation
}
