package com.example.communicationinfo.screens.baseStation

import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.NavController


@Composable
fun BaseStationScreen(navController: NavController){


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
    }


}