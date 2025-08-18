package com.example.communicationinfo.screens.WiFi

import android.Manifest
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.net.wifi.ScanResult
import android.net.wifi.WifiManager
import androidx.annotation.RequiresPermission
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.platform.LocalContext


@RequiresPermission(Manifest.permission.ACCESS_FINE_LOCATION)
@Composable
fun GetNearByWifi(): List<ScanResult>{
    val context = LocalContext.current

    var wifiManager = context.applicationContext.getSystemService(Context.WIFI_SERVICE) as WifiManager

    var wifiList by remember { mutableStateOf<List<ScanResult>>(
        emptyList()) }
    DisposableEffect(Unit) {
        val receiver = object : BroadcastReceiver() {
            override fun onReceive(context: Context?, intent: Intent?) {
                if (intent?.action == WifiManager.SCAN_RESULTS_AVAILABLE_ACTION) {
                    wifiList = wifiManager.scanResults
                }
            }
        }

        context.registerReceiver(receiver, IntentFilter(WifiManager.SCAN_RESULTS_AVAILABLE_ACTION))
        onDispose {
            context.unregisterReceiver(receiver) }
    }
    wifiManager.startScan()

    return wifiList
}