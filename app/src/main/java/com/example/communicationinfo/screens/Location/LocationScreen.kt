package com.example.communicationinfo.screens.Location

import android.Manifest
import android.util.Log
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresPermission
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.content.ContextCompat
import androidx.navigation.NavController
import com.example.communicationinfo.widgets.InFoAppBar
import com.google.android.gms.maps.model.LatLng



@RequiresPermission(allOf = [Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION])
@Composable
fun LocationScreen(navController: NavController){

    val context = LocalContext.current

    var isFineGranted by remember {
        mutableStateOf(false)
    }
    var isCoarseGranted by remember {
        mutableStateOf(false)
    }

    val permissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestMultiplePermissions()
    ) {permissionsMap->
        isFineGranted = permissionsMap[android.Manifest.permission.ACCESS_FINE_LOCATION] ?: false
        isCoarseGranted = permissionsMap[android.Manifest.permission.ACCESS_COARSE_LOCATION] ?: false

        if(permissionsMap.containsValue(false)){
                Toast.makeText(context, "Permission Denied", Toast.LENGTH_SHORT).show()

        }else{
            Toast.makeText(context, "Permission Granted", Toast.LENGTH_SHORT).show()
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
        permissionLauncher.launch(permissions.toTypedArray())
        Log.d("dd", "${permissions}")
    }

    Scaffold(topBar = { InFoAppBar(title = "Location"){
        navController.popBackStack()
    } }) { contentPadding ->
        Surface (modifier = Modifier.padding(contentPadding),
            color = Color(0xFFD5C593)){
            Column(modifier = Modifier.fillMaxSize(),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally) {
                    if(!isCoarseGranted && !isFineGranted){
                        Text("Permission Denied", style = MaterialTheme.typography.bodyLarge,
                            fontWeight = FontWeight.Bold,
                            fontSize = 30.sp)
                        Spacer(modifier = Modifier.size(40.dp))
                        Button(onClick = {
                            val permission = arrayOf(
                                android.Manifest.permission.ACCESS_COARSE_LOCATION,
                                android.Manifest.permission.ACCESS_COARSE_LOCATION
                            )
                            permissionLauncher.launch(permission)
                        }) {
                            Text(
                                text = "Grant Permissions",
                                style = MaterialTheme.typography.labelMedium,
                                fontSize = 20.sp,
                                fontWeight = FontWeight.Bold
                            )
                        }

                    }else{
                        var currentLocation by remember { mutableStateOf<LatLng?>(null) }
                        currentLocation = GetLocation()
                        Text("${currentLocation?.latitude},${currentLocation?.longitude}")

                    }
            }
        }
    }
}


@Preview
@Composable
fun LocationPreview(){

}