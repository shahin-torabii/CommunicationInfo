package com.example.communicationinfo.screens.WiFi

import android.net.wifi.ScanResult
import android.util.Log
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.communicationinfo.widgets.InFoAppBar
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.rememberMultiplePermissionsState


@OptIn(ExperimentalPermissionsApi::class)
@Composable
fun WifiScreen(navController: NavController){


    var wifiList by remember { mutableStateOf<List<ScanResult>>(
        emptyList()) }


    val context = LocalContext.current


    var isFineGranted by remember {
        mutableStateOf(false)
    }
    var isAccessWifi by remember {
        mutableStateOf(false)
    }

    var isCoarseGranted by remember {
        mutableStateOf(false)
    }


    var isChangeWifiGranted by remember {
        mutableStateOf(false)
    }


    var isNearWifiGranted by remember {
        mutableStateOf(false)
    }

    val permissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestMultiplePermissions()
    ) {permissionsMap->
        isFineGranted = permissionsMap[android.Manifest.permission.ACCESS_FINE_LOCATION] ?: false
        isCoarseGranted = permissionsMap[android.Manifest.permission.ACCESS_COARSE_LOCATION] ?: false
        isAccessWifi = permissionsMap[android.Manifest.permission.ACCESS_WIFI_STATE] ?: false
        isChangeWifiGranted = permissionsMap[android.Manifest.permission.CHANGE_WIFI_STATE] ?: false
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.TIRAMISU) {
            isNearWifiGranted =
                permissionsMap[android.Manifest.permission.NEARBY_WIFI_DEVICES] ?: false
        }

        if(permissionsMap.containsValue(false)){
            Toast.makeText(context, "Permissions Denied", Toast.LENGTH_SHORT).show()

        }else{
            Toast.makeText(context, "Permissions Granted", Toast.LENGTH_SHORT).show()
        }

    }



    LaunchedEffect(Unit) {

        val permissions = mutableListOf<String>()
        if (!isFineGranted){
            permissions.add(android.Manifest.permission.ACCESS_FINE_LOCATION)
        }
        if(!isCoarseGranted){
            permissions.add(android.Manifest.permission.ACCESS_COARSE_LOCATION)
        }
        if (!isAccessWifi){
            permissions.add(android.Manifest.permission.ACCESS_WIFI_STATE)
        }
        if(!isChangeWifiGranted){
            permissions.add(android.Manifest.permission.CHANGE_WIFI_STATE)
        }

        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.TIRAMISU) {
            if(!isNearWifiGranted){
                permissions.add(android.Manifest.permission.NEARBY_WIFI_DEVICES)
            }
        }
        permissionLauncher.launch(permissions.toTypedArray())
        Log.d("dd", "${permissions}")
    }


    Scaffold(topBar = { InFoAppBar(title = "Nearby Wi-Fi's"){
        navController.popBackStack()
    } }) { contentPadding ->
        Surface (modifier = Modifier.padding(contentPadding),
            color = Color(0xFFD5C593)) {
            Column(modifier = Modifier.fillMaxSize()) {
                if(isAccessWifi && isNearWifiGranted &&isChangeWifiGranted
                    && isFineGranted && isCoarseGranted) {
                    val result = GetNearByWifi()
                    Text("${result[0]}")
                }
            }
        }
    }
}


@Preview
@Composable
fun prev(){
    Surface (modifier = Modifier.padding(2.dp),
        color = Color(0xFFD5C593)){
        Column(modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally) {
    }
}}