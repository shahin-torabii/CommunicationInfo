package com.example.communicationinfo.screens.baseStation

import android.os.Build
import android.telephony.*
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.communicationinfo.widgets.InFoAppBar
import androidx.compose.foundation.lazy.items

@Composable
fun BaseStationScreen(navController: NavController) {
    val context = LocalContext.current

    var isFineGranted by remember { mutableStateOf(false) }
    var isReadPhoneGranted by remember { mutableStateOf(false) }
    var isReadPhoneNumberGranted by remember { mutableStateOf(false) }

    val permissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestMultiplePermissions()
    ) { permissionsMap ->
        isFineGranted = permissionsMap[android.Manifest.permission.ACCESS_FINE_LOCATION] ?: false
        isReadPhoneGranted = permissionsMap[android.Manifest.permission.READ_PHONE_STATE] ?: false
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            isReadPhoneNumberGranted =
                permissionsMap[android.Manifest.permission.READ_PHONE_NUMBERS] ?: false
        }

        Toast.makeText(
            context,
            if (permissionsMap.containsValue(false)) "Permissions Denied" else "Permissions Granted",
            Toast.LENGTH_SHORT
        ).show()
    }

    // Ask permissions at launch
    LaunchedEffect(Unit) {
        val permissions = mutableListOf<String>()
        if (!isFineGranted) permissions.add(android.Manifest.permission.ACCESS_FINE_LOCATION)
        if (!isReadPhoneGranted) permissions.add(android.Manifest.permission.READ_PHONE_STATE)
        if (!isReadPhoneNumberGranted && Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            permissions.add(android.Manifest.permission.READ_PHONE_NUMBERS)
        }
        if (permissions.isNotEmpty()) {
            permissionLauncher.launch(permissions.toTypedArray())
        }
    }

    Scaffold(
        topBar = {
            InFoAppBar(title = "Base Stations") {
                navController.popBackStack()
            }
        }
    ) { contentPadding ->
        Surface(
            modifier = Modifier.padding(contentPadding),
            color = Color(0xFFD5C593)
        ) {
            val permissionGrants = isReadPhoneGranted && isFineGranted &&
                    (Build.VERSION.SDK_INT < Build.VERSION_CODES.TIRAMISU || isReadPhoneNumberGranted)

            if (permissionGrants) {
                val telephonyManager = GetBaseStations()

                Column(
                    modifier = Modifier
                        .padding(8.dp)
                        .fillMaxSize(),
                    verticalArrangement = Arrangement.Top,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    lateinit var simForGlobalInfo : TelephonyManager
                    telephonyManager.forEachIndexed { index, triple ->
                        val sim = triple.first
                        val slot = triple.second
                        val number = triple.third
                        simForGlobalInfo =sim
                        if (sim == null) {
                            Text("No SIM card found in slot $slot")
                        } else {
                            SimCardInfo(sim, slot, number)
                        }
                    }
                    Text(
                        "Cell Infos",
                        modifier = Modifier.padding(start = 10.dp, top = 10.dp),
                        style = MaterialTheme.typography.bodyMedium,
                        fontSize = 15.sp
                    )
                    LazyColumn {
                            simForGlobalInfo.allCellInfo?.let { list ->
                                items(list) { cell ->
                            when (cell) {
                                is CellInfoGsm -> InfoCard(
                                    "GSM Cell", mapOf(
                                        "MCC" to cell.cellIdentity.mccString,
                                        "MNC" to cell.cellIdentity.mncString,
                                        "LAC" to cell.cellIdentity.lac,
                                        "CID" to cell.cellIdentity.cid,
                                        "ARFCN" to cell.cellIdentity.arfcn
                                    )
                                )

                                is CellInfoWcdma -> InfoCard(
                                    "WCDMA Cell", mapOf(
                                        "MCC" to cell.cellIdentity.mcc,
                                        "MNC" to cell.cellIdentity.mnc,
                                        "LAC" to cell.cellIdentity.lac,
                                        "CID" to cell.cellIdentity.cid,
                                        "UARFCN" to cell.cellIdentity.uarfcn,
                                        "PSC" to cell.cellIdentity.psc
                                    )
                                )

                                is CellInfoLte -> InfoCard(
                                    "LTE Cell", mapOf(
                                        "MCC" to cell.cellIdentity.mccString,
                                        "MNC" to cell.cellIdentity.mncString,
                                        "TAC" to cell.cellIdentity.tac,
                                        "CI" to cell.cellIdentity.ci,
                                        "PCI" to cell.cellIdentity.pci,
                                        "EARFCN" to cell.cellIdentity.earfcn,
                                        "Bandwidth" to cell.cellIdentity.bandwidth
                                    )
                                )

                                is CellInfoNr -> if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                                    val id = cell.cellIdentity as CellIdentityNr
                                    InfoCard(
                                        "5G NR Cell", mapOf(
                                            "MCC" to id.mccString,
                                            "MNC" to id.mncString,
                                            "NCI" to id.nci,
                                            "PCI" to id.pci,
                                            "TAC" to id.tac,
                                            "NRARFCN" to id.nrarfcn
                                        )
                                    )
                                }
                            }
                            }
                            }
                        }
                    }

            } else {
                PermissionDeniedView { permissionLauncher.launch(getBasePermissions()) }
            }
        }
    }
}

@Composable
fun SimCardInfo(sim: TelephonyManager, slot: Int, numbers: String) {
    var expanded by remember { mutableStateOf(false) }

    val simOperator = sim.simOperator
    val simOperatorName = sim.simOperatorName
    val signalInfo = sim.signalStrength

    val level = signalInfo?.level ?: 0
    val dbm = signalInfo?.cellSignalStrengths?.firstOrNull()?.dbm

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
            // SIM basic info
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Start
            ) {
                Column(
                    modifier = Modifier.weight(1f),
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.Start
                ) {
                    Text(
                        "${simOperatorName} ($simOperator)",
                        modifier = Modifier.padding(start = 10.dp, top = 10.dp),
                        style = MaterialTheme.typography.bodyMedium,
                        fontSize = 15.sp
                    )
                    val number = numbers?:"Unkown Number"
                    Text(
                        text = "${number}",
                        modifier = Modifier.padding(start = 10.dp, top = 5.dp),
                        style = MaterialTheme.typography.bodyMedium,
                        fontSize = 15.sp
                    )
                }

                Text(
                    "Slot: $slot",
                    modifier = Modifier.padding(start = 5.dp, top = 5.dp),
                    style = MaterialTheme.typography.bodyMedium,
                    fontSize = 15.sp
                )
                Spacer(modifier = Modifier.width(2.dp))
                Text(
                    "Signal: $dbm dBm",
                    modifier = Modifier.padding(start = 5.dp, top = 5.dp),
                    style = MaterialTheme.typography.bodyMedium,
                    fontSize = 15.sp
                )
                Spacer(modifier = Modifier.width(2.dp))
                SignalIcon(bars = level)
            }

        }
    }
}

@Composable
fun PermissionDeniedView(onGrantPermissions: () -> Unit) {
    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            "Permission Denied",
            style = MaterialTheme.typography.bodyLarge,
            fontWeight = FontWeight.Bold,
            fontSize = 30.sp
        )
        Spacer(modifier = Modifier.size(40.dp))
        Button(onClick = onGrantPermissions) {
            Text(
                text = "Grant Permissions",
                style = MaterialTheme.typography.labelMedium,
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold
            )
        }
    }
}

@Composable
fun InfoCard(title: String, features: Map<String, Any?>) {
    Surface(
        modifier = Modifier
            .padding(2.dp)
            .fillMaxWidth(),
        color = Color(0xFF8ED5F6),
        shape = RoundedCornerShape(15.dp)
    ) {
    Column(modifier = Modifier.fillMaxWidth(), horizontalAlignment = Alignment.Start) {
        Text(
            text = title,
            modifier = Modifier.padding(start = 10.dp, top = 4.dp),
            style = MaterialTheme.typography.titleMedium,
            fontSize = 15.sp
        )
        features.forEach { (label, value) ->
            Text(
                text = "$label: $value",
                modifier = Modifier.padding(start = 10.dp, top = 4.dp),
                style = MaterialTheme.typography.bodyMedium,
                fontSize = 15.sp
            )
        }
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

// Helper for permissions
private fun getBasePermissions(): Array<String> {
    val basePermissions = arrayOf(
        android.Manifest.permission.ACCESS_FINE_LOCATION,
        android.Manifest.permission.READ_PHONE_STATE
    )
    return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
        basePermissions + android.Manifest.permission.READ_PHONE_NUMBERS
    } else {
        basePermissions
    }
}
