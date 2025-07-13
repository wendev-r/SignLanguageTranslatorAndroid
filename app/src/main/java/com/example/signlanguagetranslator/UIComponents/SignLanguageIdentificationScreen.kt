package com.example.signlanguagetranslator.UIComponents

import android.bluetooth.BluetoothAdapter
import androidx.compose.runtime.Composable
import com.example.signlanguagetranslator.UIComponents.Permissions.SystemBroadcastReceiver

@Composable
fun SignLanguageIdentificationScreen(
    onBluetoothStateChanged:()-> Unit
){
    SystemBroadcastReceiver(systemAction = BluetoothAdapter.ACTION_STATE_CHANGED) { bluetoothState ->
        val action = bluetoothState?.action ?: return@SystemBroadcastReceiver

        if(action == BluetoothAdapter.ACTION_STATE_CHANGED){
            onBluetoothStateChanged()
        }
    }
}