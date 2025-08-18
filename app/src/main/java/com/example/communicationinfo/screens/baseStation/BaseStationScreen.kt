package com.example.communicationinfo.screens.baseStation

import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
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
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.communicationinfo.screens.WiFi.GetNearByWifi
import com.example.communicationinfo.screens.WiFi.WifiRow
import com.example.communicationinfo.widgets.InFoAppBar


@Composable
fun BaseStationScreen(navController: NavController) {


    val context = LocalContext.current


    var isFineGranted by remember {
        mutableStateOf(false)
    }
    var isReadPhoneGranted by remember {
        mutableStateOf(false)
    }


    var isReadPhoneNumberGranted by remember {
        mutableStateOf(false)
    }


    val permissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestMultiplePermissions()
    ) { permissionsMap ->
        isFineGranted = permissionsMap[android.Manifest.permission.ACCESS_FINE_LOCATION] ?: false
        isReadPhoneGranted = permissionsMap[android.Manifest.permission.READ_PHONE_STATE] ?: false
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.TIRAMISU) {
            isReadPhoneNumberGranted =
                permissionsMap[android.Manifest.permission.READ_PHONE_NUMBERS] ?: false
        }
        if (permissionsMap.containsValue(false)) {
            Toast.makeText(context, "Permissions Denied", Toast.LENGTH_SHORT).show()

        } else {
            Toast.makeText(context, "Permissions Granted", Toast.LENGTH_SHORT).show()
        }

    }



    LaunchedEffect(Unit) {

        val permissions = mutableListOf<String>()
        if (!isFineGranted) {
            permissions.add(android.Manifest.permission.ACCESS_FINE_LOCATION)
        }
        if (!isReadPhoneGranted) {
            permissions.add(android.Manifest.permission.READ_PHONE_STATE)
        }

        if (!isReadPhoneNumberGranted) {
            permissions.add(android.Manifest.permission.READ_PHONE_NUMBERS)
        }

        permissionLauncher.launch(permissions.toTypedArray())
    }

    Scaffold(topBar = {
        InFoAppBar(title = "Base Stations") {
            navController.popBackStack()
        }
    }) { contentPadding ->
        Surface(
            modifier = Modifier.padding(contentPadding),
            color = Color(0xFFD5C593)
        ) {
            val permissionGrants = isReadPhoneGranted && isFineGranted &&
                    (android.os.Build.VERSION.SDK_INT < android.os.Build.VERSION_CODES.TIRAMISU || isReadPhoneNumberGranted)

            if (permissionGrants) {


            } else {
                Column(
                    modifier = Modifier.fillMaxSize(),
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        "Permission Denied", style = MaterialTheme.typography.bodyLarge,
                        fontWeight = FontWeight.Bold,
                        fontSize = 30.sp
                    )

                    Spacer(modifier = Modifier.size(40.dp))
                    Button(onClick = {
                        val basePermissions = arrayOf(
                            android.Manifest.permission.ACCESS_FINE_LOCATION,
                            android.Manifest.permission.READ_PHONE_STATE
                        )

                        val permissions =
                            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.TIRAMISU) {
                                basePermissions + android.Manifest.permission.READ_PHONE_NUMBERS
                            } else {
                                basePermissions
                            }

                        permissionLauncher.launch(permissions)
                    }) {
                        Text(
                            text = "Grant Permissions",
                            style = MaterialTheme.typography.labelMedium,
                            fontSize = 20.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
            }
        }

    }
}