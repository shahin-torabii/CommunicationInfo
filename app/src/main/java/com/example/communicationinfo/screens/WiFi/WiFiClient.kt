package com.example.communicationinfo.screens.WiFi

import android.Manifest
import android.net.wifi.ScanResult
import android.net.wifi.WifiManager
import androidx.annotation.RequiresPermission
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.ui.platform.LocalContext
import kotlinx.coroutines.delay

@RequiresPermission(
    allOf = [
        Manifest.permission.ACCESS_FINE_LOCATION,
        Manifest.permission.NEARBY_WIFI_DEVICES
    ]
)
@Composable
fun GetNearByWifi(): List<ScanResult> {
    val context = LocalContext.current
    val wifiManager = context.applicationContext.getSystemService(WifiManager::class.java)

    var wifiList by remember { mutableStateOf<List<ScanResult>>(emptyList()) }

    LaunchedEffect(Unit) {
        while (true) {
            wifiList = wifiManager.scanResults
            delay(5000)
        }
    }
    return wifiList
}
