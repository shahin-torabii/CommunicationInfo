package com.example.communicationinfo.screens


import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.communicationinfo.navigations.InfoNavigation
import com.example.communicationinfo.navigations.InfoScreens
import com.example.communicationinfo.widgets.Button


@Composable
fun MainScreen(navController: NavController){

    Surface(modifier = Modifier.padding(2.dp),
        color = Color(0xFFD5C593)) {

        Column(modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center) {

            Button(modifier = Modifier.size(width = 300.dp, height = 70.dp),"Getting Device Location",
                onClick = {
                    navController.navigate(InfoScreens.LocationScreen.name)
                })
            Spacer(modifier = Modifier.size(90.dp))
            Button(modifier = Modifier.size(width = 300.dp, height = 70.dp),"Getting nearby Wi-Fi's",
                onClick = {
                    navController.navigate(InfoScreens.WiFiScreen.name)
                })
            Spacer(modifier = Modifier.size(90.dp))
            Button(modifier = Modifier.size(width = 300.dp, height = 70.dp),"Getting Device BaseStation",
                onClick = {
                    navController.navigate(InfoScreens.BaseStationScreen.name)
                })
        }
    }

}