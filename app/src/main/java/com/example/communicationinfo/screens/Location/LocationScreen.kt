package com.example.communicationinfo.screens.Location

import android.content.pm.PackageManager
import android.util.Log
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.core.content.ContextCompat
import androidx.navigation.NavController
import com.example.communicationinfo.widgets.InFoAppBar

@Composable
fun LocationScreen(navController: NavController){

    val context = LocalContext.current

    val permissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestMultiplePermissions()
    ) {permissionsMap->
        if(permissionsMap.containsValue(false)){
            Log.d("hi", "this is it")
        }else{
            Log.d("hi", "this is it")
        }

    }

    val isFineGranted = checkFinePermission(context)
    val isCoarseGranted = checkCoarsePermission(context)

    LaunchedEffect(Unit) {
        var permissions : Array<String> = emptyArray<String>()
        var permission = permissions.toMutableList()
        if (!isFineGranted){
            permission.add(android.Manifest.permission.ACCESS_FINE_LOCATION)
        }
        if(!isCoarseGranted){
            permission.add(android.Manifest.permission.ACCESS_COARSE_LOCATION)
        }
        permissions = permission.toTypedArray()
        permissionLauncher.launch(permissions)
    }

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


@Preview
@Composable
fun LocationPreview(){

}