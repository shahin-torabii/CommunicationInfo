package com.example.communicationinfo.navigations

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.communicationinfo.screens.LocationScreen


@Composable
fun InfoNavigation(){

    val navController = rememberNavController()

    NavHost(navController = navController,
        startDestination = InfoScreens.MainScreen){

        composable(InfoScreens.LocationScreen.name){
            LocationScreen(navController = navController)
        }

    }
}