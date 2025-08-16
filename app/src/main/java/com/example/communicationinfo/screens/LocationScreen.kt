package com.example.communicationinfo.screens

import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.NavController
import com.example.communicationinfo.widgets.InFoAppBar
import java.util.jar.Manifest

@Composable

fun LocationScreen(navController: NavController){

    val context = LocalContext.current

    val permissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission()
    ) {granted ->
        if(granted){

        }else{

        }

    }

    permissionLauncher.launch(android.Manifest.permission.ACCESS_FINE_LOCATION)
    permissionLauncher.launch(android.Manifest.permission.ACCESS_COARSE_LOCATION)

    Scaffold(topBar = { InFoAppBar(title = "Location"){
        navController.popBackStack()
    } }) { contentPadding ->
        Surface (modifier = Modifier.padding(contentPadding),
            color = Color(0xFFD5C593)){
            Column(modifier = Modifier.fillMaxSize(),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally) {

            }
        }
    }
}