package com.example.signlanguagetranslator

import android.Manifest
import android.app.Activity
import android.bluetooth.BluetoothAdapter
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import com.example.signlanguagetranslator.UIComponents.Navigation
import com.example.signlanguagetranslator.ui.theme.SignLanguageTranslatorTheme
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    @Inject
    lateinit var bluetoothAdapter: BluetoothAdapter

    // Check for Bluetooth permissions and request if needed
    private val bluetoothPermissionRequest = registerForActivityResult(ActivityResultContracts.RequestMultiplePermissions()) { permissions ->
        val isBluetoothConnectGranted = permissions[Manifest.permission.BLUETOOTH_CONNECT] == true
        val isBluetoothScanGranted = permissions[Manifest.permission.BLUETOOTH_SCAN] == true

        if (isBluetoothConnectGranted && isBluetoothScanGranted) {
            // Permissions granted, proceed with Bluetooth tasks
            showBluetoothDialog()
        } else {
            Toast.makeText(this, "Bluetooth permissions are required", Toast.LENGTH_SHORT).show()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            SignLanguageTranslatorTheme {
                Navigation(onBluetoothStateChanged = {showBluetoothDialog()})
            }

        // Check if the Bluetooth permissions are already granted
        val bluetoothConnectPermission = ContextCompat.checkSelfPermission(
            this,
            Manifest.permission.BLUETOOTH_CONNECT
        ) == PackageManager.PERMISSION_GRANTED

        val bluetoothScanPermission = ContextCompat.checkSelfPermission(
            this,
            Manifest.permission.BLUETOOTH_SCAN
        ) == PackageManager.PERMISSION_GRANTED
        Toast.makeText(this,
            "Bluetooth permissions are required: bt connect: $bluetoothConnectPermission , scan: $bluetoothScanPermission", Toast.LENGTH_SHORT).show()

        if (!bluetoothConnectPermission || !bluetoothScanPermission) {
            bluetoothPermissionRequest.launch(
                arrayOf(
                    Manifest.permission.BLUETOOTH_CONNECT,
                    Manifest.permission.BLUETOOTH_SCAN
                )
            )
        } else {
            showBluetoothDialog()
        }


        }
    }
    private var isBluetoothDialogShown = false
    private fun showBluetoothDialog() {
        Toast.makeText(this,
            "Bluetooth permissions are required: bt connect: " + bluetoothAdapter.isEnabled, Toast.LENGTH_SHORT).show()
        if (!bluetoothAdapter.isEnabled) {
            if(!isBluetoothDialogShown) {
                val enableBluetoothIntent = Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE)
                startBluetoothIntentForResult.launch(enableBluetoothIntent)
                isBluetoothDialogShown = true
            }
        }
    }

    private val startBluetoothIntentForResult =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            isBluetoothDialogShown = false
            if (result.resultCode != Activity.RESULT_OK) {
                showBluetoothDialog()
            }
        }
}
