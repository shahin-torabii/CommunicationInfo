package com.example.communicationinfo.screens.WiFi

import android.net.wifi.ScanResult
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
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
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.communicationinfo.widgets.InFoAppBar
import com.google.accompanist.permissions.ExperimentalPermissionsApi


@OptIn(ExperimentalPermissionsApi::class)
@Composable
fun WifiScreen(navController: NavController) {

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
    ) { permissionsMap ->
        isFineGranted = permissionsMap[android.Manifest.permission.ACCESS_FINE_LOCATION] ?: false
        isCoarseGranted =
            permissionsMap[android.Manifest.permission.ACCESS_COARSE_LOCATION] ?: false
        isAccessWifi = permissionsMap[android.Manifest.permission.ACCESS_WIFI_STATE] ?: false
        isChangeWifiGranted = permissionsMap[android.Manifest.permission.CHANGE_WIFI_STATE] ?: false
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.TIRAMISU) {
            isNearWifiGranted =
                permissionsMap[android.Manifest.permission.NEARBY_WIFI_DEVICES] ?: false
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
        if (!isCoarseGranted) {
            permissions.add(android.Manifest.permission.ACCESS_COARSE_LOCATION)
        }
        if (!isAccessWifi) {
            permissions.add(android.Manifest.permission.ACCESS_WIFI_STATE)
        }
        if (!isChangeWifiGranted) {
            permissions.add(android.Manifest.permission.CHANGE_WIFI_STATE)
        }

        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.TIRAMISU) {
            if (!isNearWifiGranted) {
                permissions.add(android.Manifest.permission.NEARBY_WIFI_DEVICES)
            }
        }
        permissionLauncher.launch(permissions.toTypedArray())
    }


    Scaffold(topBar = {
        InFoAppBar(title = "Nearby Wi-Fi's") {
            navController.popBackStack()
        }
    }) { contentPadding ->
        Surface(
            modifier = Modifier.padding(contentPadding),
            color = Color(0xFFD5C593)
        ) {
            if (isAccessWifi && isNearWifiGranted && isChangeWifiGranted
                && isFineGranted && isCoarseGranted
            ) {
                val results = GetNearByWifi()
                LazyColumn() {
                    items(results) { result ->
                        WifiRow(result)
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
                    val basePermissions = arrayOf(
                        android.Manifest.permission.ACCESS_COARSE_LOCATION,
                        android.Manifest.permission.ACCESS_FINE_LOCATION,
                        android.Manifest.permission.CHANGE_WIFI_STATE,
                        android.Manifest.permission.ACCESS_WIFI_STATE
                    )

                    val permissions =
                        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.TIRAMISU) {
                            basePermissions + android.Manifest.permission.NEARBY_WIFI_DEVICES
                        } else {
                            basePermissions
                        }


                    Spacer(modifier = Modifier.size(40.dp))
                    Button(onClick = {
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

@Composable
fun WifiRow(result: ScanResult) {

    var expanded by remember {
        mutableStateOf(false)
    }

    val level = result.level // dBm
    val bars = signalLevelToBars(level)

    Surface(
        modifier = Modifier
            .padding(2.dp)
            .fillMaxWidth(),
        color = Color(0xFF8ED5F6),
        shape = RoundedCornerShape(15.dp)
    ) {
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
                Text(
                    "${result.wifiSsid}", modifier = Modifier.padding(start = 10.dp, top = 20.dp),
                    style = MaterialTheme.typography.bodyMedium,
                    fontSize = 15.sp
                )
                Spacer(modifier = Modifier.size(width = 160.dp, height = 0.dp))

                Text(
                    "Signal: ${result.level} dbm",
                    modifier = Modifier.padding(start = 10.dp, top = 20.dp),
                    style = MaterialTheme.typography.bodyMedium,
                    fontSize = 15.sp
                )
                SignalIcon(bars)
                //Icon(imageVector = Icons.Filled.Wifi, contentDescription = "wifi")
            }

            AnimatedVisibility(visible = expanded) {
                Column {

                    Text(
                        "BSSID: ${result.BSSID}",
                        modifier = Modifier.padding(start = 10.dp, top = 20.dp),
                        style = MaterialTheme.typography.bodyMedium,
                        fontSize = 15.sp
                    )
                    Text(
                        "Freq: ${result.frequency}",
                        modifier = Modifier.padding(start = 10.dp, top = 20.dp),
                        style = MaterialTheme.typography.bodyMedium,
                        fontSize = 15.sp
                    )
                    Text(
                        "Channel Width: ${result.channelWidth}",
                        modifier = Modifier.padding(start = 10.dp, top = 20.dp),
                        style = MaterialTheme.typography.bodyMedium,
                        fontSize = 15.sp
                    )
                    Text(
                        "Capabilities: ${result.capabilities}",
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

}

fun signalLevelToBars(dbm: Int): Int {
    return when {
        dbm >= -50 -> 4
        dbm >= -60 -> 3
        dbm >= -70 -> 2
        dbm >= -80 -> 1
        else -> 0
    }
}

@Composable
fun SignalIcon(bars: Int) {
    Row(verticalAlignment = Alignment.Bottom, horizontalArrangement = Arrangement.spacedBy(2.dp)) {
        for (i in 1..4) {
            Box(
                modifier = Modifier
                    .width(4.dp)
                    .height((i * 4).dp)
                    .background(
                        color = if (i <= bars) Color.Green else Color.LightGray,
                        shape = RoundedCornerShape(1.dp)
                    )
            )
        }
    }
}


@Preview
@Composable
fun Prev() {
    val listStr = listOf("hi", "it is me", "shahin")
    Surface(
        modifier = Modifier.padding(2.dp),
        color = Color(0xFFD5C593)
    ) {
        LazyColumn {
            items(listStr) { str ->

                SignalIcon(3)
            }
        }
    }
}
