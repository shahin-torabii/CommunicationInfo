package com.example.communicationinfo.navigations

import MainScreen
import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.communicationinfo.screens.BaseStationScreen
import com.example.communicationinfo.screens.LocationScreen
import com.example.communicationinfo.screens.WifiScreen


@Composable
fun InfoNavigation(){

    val navController = rememberNavController()

    NavHost(navController = navController,
        startDestination = InfoScreens.MainScreen.name){

        composable(InfoScreens.MainScreen.name){
            MainScreen(navController = navController)
        }

        composable(InfoScreens.LocationScreen.name){
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