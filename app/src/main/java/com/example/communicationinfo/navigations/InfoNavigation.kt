package com.example.communicationinfo.navigations

import com.example.communicationinfo.screens.MainScreen
import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.communicationinfo.screens.BaseStationScreen
import com.example.communicationinfo.screens.Location.LocationScreen
import com.example.communicationinfo.screens.WiFi.WifiScreen


@Composable
fun InfoNavigation(){

    val navController = rememberNavController()

    NavHost(navController = navController,
        startDestination = InfoScreens.MainScreen.name){

        composable(InfoScreens.MainScreen.name){
            MainScreen(navController = navController)
        }

        composable(InfoScreens.LocationScreen.name) @androidx.annotation.RequiresPermission(allOf = [android.Manifest.permission.ACCESS_FINE_LOCATION, android.Manifest.permission.ACCESS_COARSE_LOCATION]) {
            LocationScreen(navController = navController)
        }


        composable(InfoScreens.WiFiScreen.name) {
            WifiScreen(navController = navController)
        }

        composable(InfoScreens.BaseStationScreen.name) {
            BaseStationScreen(navController = navController)
        }
    }
}