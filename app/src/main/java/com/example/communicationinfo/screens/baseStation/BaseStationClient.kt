package com.example.communicationinfo.screens.baseStation

import android.content.Context
import android.telephony.SubscriptionInfo
import android.telephony.SubscriptionManager
import android.telephony.TelephonyManager
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalContext
import java.util.concurrent.Flow


@Composable
fun GetBaseStations(): List< Pair<TelephonyManager, Int>>{

    val context = LocalContext.current

    val subscriptionManager = context.applicationContext.getSystemService(Context.TELEPHONY_SUBSCRIPTION_SERVICE) as SubscriptionManager

    val activeSubscriptions :List<SubscriptionInfo> = subscriptionManager.activeSubscriptionInfoList()

    val telManagers = remember {
        mutableListOf< Pair<TelephonyManager, Int>>()
    }

    activeSubscriptions.forEach { subscription ->
        val telephonyManager = context.applicationContext.getSystemService(TelephonyManager::class.java)
            .createForSubscriptionId(subscription.subscriptionId)
        telManagers.add(Pair(telephonyManager, subscription.simSlotIndex))
    }
    return  telManagers
}