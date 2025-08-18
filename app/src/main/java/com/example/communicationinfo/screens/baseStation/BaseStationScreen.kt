package com.example.communicationinfo.screens.baseStation

import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
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
import com.example.communicationinfo.screens.WiFi.SignalIcon
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
                val telephonyManager = GetBaseStations()
                val simOne = telephonyManager[0].first
                val simOneSlot =  telephonyManager[0].second
                val simTwoPair = telephonyManager.getOrNull(1)
                val simTwo = simTwoPair?.first
                val simTwoSlot = simTwoPair?.second
                Surface(
                    modifier = Modifier
                        .padding(2.dp)
                        .fillMaxWidth(),
                    color = Color(0xFF8ED5F6),
                    shape = RoundedCornerShape(15.dp)
                ) {
                    val expanded by remember {
                        mutableStateOf(false)
                    }
                    Column(
                        modifier = Modifier.fillMaxWidth(),
                        verticalArrangement = Arrangement.Center,
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.Start
                        ) {
                               Text(simOne.allCellInfo.toString())
                                Spacer(modifier = Modifier.size(width = 160.dp, height = 0.dp))
                                Text(text = "${simOneSlot}")
                        }

                        AnimatedVisibility(visible = expanded) {
                            Column {

                                Text(
                                    "BSSID:",
                                    modifier = Modifier.padding(start = 10.dp, top = 20.dp),
                                    style = MaterialTheme.typography.bodyMedium,
                                    fontSize = 15.sp
                                )

                                Text(
                                    "Capabilities:",
                                    modifier = Modifier.padding(start = 10.dp, top = 20.dp),
                                    style = MaterialTheme.typography.bodyMedium,
                                    fontSize = 15.sp
                                )
//                    Text("Operator: ${result.}", modifier = Modifier.padding(start = 10.dp, top = 20.dp,),
//                        style = MaterialTheme.typography.bodyMedium,
//                        fontSize = 15.sp)

                            }
                        }

                        Icon(
                            imageVector = if (!expanded) Icons.Default.KeyboardArrowDown
                            else Icons.Default.KeyboardArrowUp,
                            contentDescription = "arrow up",
                            modifier = Modifier.clickable {
                                expanded = !expanded
                            }
                        )
                    }
                }


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