package com.example.communicationinfo.screens.baseStation

import android.os.Build
import android.telephony.CellIdentityNr
import android.telephony.CellInfoGsm
import android.telephony.CellInfoLte
import android.telephony.CellInfoNr
import android.telephony.CellInfoWcdma
import android.telephony.CellSignalStrength
import android.telephony.CellSignalStrengthNr
import android.telephony.SignalStrength
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
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.communicationinfo.screens.WiFi.GetNearByWifi
import com.example.communicationinfo.screens.WiFi.SignalIcon
import com.example.communicationinfo.screens.WiFi.WifiRow
import com.example.communicationinfo.widgets.InFoAppBar
import kotlin.properties.Delegates


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
                val simOneSlot = telephonyManager[0].second
                val simOneNmber = telephonyManager[0].third
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
                    var expanded by remember {
                        mutableStateOf(false)
                    }
                    val simOperator = simOne.simOperator
                    val networkOperator = simOne.networkOperator
                    val simOperatorName = simOne.simOperatorName
                    val networkOperatorName = simOne.networkOperatorName
                    val countryIso = simOne.simCountryIso

                    lateinit var signalStrength: CellSignalStrength
                    var dbm by Delegates.notNull<Int>()
                    var level by Delegates.notNull<Int>()


                    val simOneCell = simOne.allCellInfo

                    simOneCell?.forEach { info ->
                    signalStrength = info.cellSignalStrength
                        dbm = signalStrength.dbm        // Signal in dBm (negative, closer to 0 is better)
                        level = signalStrength.level
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
                            Column(
                                modifier = Modifier.weight(1f), // take only needed space
                                verticalArrangement = Arrangement.Center,
                                horizontalAlignment = Alignment.Start
                            ) {

                                    Text(
                                        "${simOperatorName}(${simOperator})",
                                        modifier = Modifier.padding(start = 10.dp, top = 10.dp),
                                        style = MaterialTheme.typography.bodyMedium,
                                        fontSize = 15.sp
                                    )
                                Text(
                                    "${simOneNmber}",
                                    modifier = Modifier.padding(start = 10.dp, top = 5.dp),
                                    style = MaterialTheme.typography.bodyMedium,
                                    fontSize = 15.sp
                                )
                            }

                            Text(
                                "Signal strength:${dbm}",
                                modifier = Modifier.padding(start = 5.dp, top = 5.dp),
                                style = MaterialTheme.typography.bodyMedium,
                                fontSize = 15.sp
                            )
                            Spacer(modifier = Modifier.width(2.dp))
                            SignalIcon(bars = level)
                        }

                        AnimatedVisibility(visible = expanded) {
                            simOneCell?.forEach { cell ->
                                when (cell) {
                                    is CellInfoGsm -> {
                                        val id = cell.cellIdentity
                                        InfoCard(
                                            "GSM Cell",
                                            mapOf(
                                                "MCC" to id.mccString,
                                                "MNC" to id.mncString,
                                                "LAC" to id.lac,
                                                "CID" to id.cid,
                                                "ARFCN" to id.arfcn,
                                            )
                                        )
                                    }
                                    is CellInfoWcdma -> {
                                        val id = cell.cellIdentity
                                        InfoCard(
                                            "WCDMA Cell",
                                            mapOf(
                                                "MCC" to id.mcc,
                                                "MNC" to id.mnc,
                                                "LAC" to id.lac,
                                                "CID" to id.cid,
                                                "UARFCN" to id.uarfcn,
                                                "PSC" to id.psc,
                                            )
                                        )
                                    }
                                    is CellInfoLte -> {
                                        val id = cell.cellIdentity
                                        InfoCard(
                                            "LTE Cell",
                                            mapOf(
                                                "MCC" to id.mccString,
                                                "MNC" to id.mncString,
                                                "TAC" to id.tac,
                                                "CI" to id.ci,
                                                "PCI" to id.pci,
                                                "EARFCN" to id.earfcn,
                                                "Bandwidth" to id.bandwidth,
                                            )
                                        )
                                    }
                                    is CellInfoNr -> if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                                        val id = cell.cellIdentity as CellIdentityNr
                                        InfoCard(
                                            "5G NR Cell",
                                            mapOf(

                                                "MCC" to id.mccString,
                                                "MNC" to id.mncString,
                                                "NCI" to id.nci,
                                                "PCI" to id.pci,
                                                "TAC" to id.tac,
                                                "NRARFCN" to id.nrarfcn,
                                            )
                                        )
                                    }
                                }
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

@Composable
fun InfoCard(title: String, features: Map<String, Any?>) {

        Column(modifier = Modifier.fillMaxWidth(),
            horizontalAlignment = Alignment.Start
            ) {
            Text(
                text = title,
                modifier = Modifier.padding(start = 10.dp, top = 4.dp),
                style = MaterialTheme.typography.titleMedium,
                fontSize = 15.sp
            )
            features.forEach { (label, value) ->
                Text(
                    text = "$label: $value",
                    modifier = Modifier.padding(start = 10.dp,top = 4.dp),
                    style = MaterialTheme.typography.bodyMedium,
                    fontSize = 15.sp
                )
            }
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


