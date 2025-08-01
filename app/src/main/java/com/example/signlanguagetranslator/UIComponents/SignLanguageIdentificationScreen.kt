package com.example.signlanguagetranslator.UIComponents

import android.bluetooth.BluetoothAdapter
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.compose.LocalLifecycleOwner
import com.example.signlanguagetranslator.UIComponents.Permissions.PermissionUtils
import com.example.signlanguagetranslator.UIComponents.Permissions.SystemBroadcastReceiver
import com.example.signlanguagetranslator.data.ConnectionState
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.rememberMultiplePermissionsState

//@OptIn(ExperimentalPermissionsApi::class)
//@Composable
//fun SignLanguageIdentificationScreen(
//    onBluetoothStateChanged: () -> Unit,
//    viewModel: SignLanguageViewModel = hiltViewModel()
//){
//    SystemBroadcastReceiver(systemAction = BluetoothAdapter.ACTION_STATE_CHANGED) { bluetoothState ->
//        val action = bluetoothState?.action ?: return@SystemBroadcastReceiver
//
//        if(action == BluetoothAdapter.ACTION_STATE_CHANGED){
//            onBluetoothStateChanged()
//        }
//    }
//
//
//    val permissionState =
//        rememberMultiplePermissionsState(permissions = PermissionUtils.permissions)
//    val lifecycleOwner = LocalLifecycleOwner.current
//    val bleConnectionState = viewModel.connectionState
//
//    DisposableEffect(
//        key1 = lifecycleOwner,
//        effect = {
//            val observer = LifecycleEventObserver { _, event ->
//                if (event == Lifecycle.Event.ON_START) {
//                    permissionState.launchMultiplePermissionRequest()
//                    if (permissionState.allPermissionsGranted && bleConnectionState == ConnectionState.Disconnected) {
//                        viewModel.reconnect()
//                    }
//                }
//                if (event == Lifecycle.Event.ON_STOP) {
//                    if (bleConnectionState == ConnectionState.Connected) {
//                        viewModel.disconnect()
//                    }
//                }
//            }
//            lifecycleOwner.lifecycle.addObserver(observer)
//
//            onDispose {
//                lifecycleOwner.lifecycle.removeObserver(observer)
//            }
//        }
//    )
//    LaunchedEffect(key1 = permissionState.allPermissionsGranted) {
//        if (permissionState.allPermissionsGranted) {
//            if (bleConnectionState == ConnectionState.Uninitialized) {
//                viewModel.initializeConnection()
//            }
//        }
//    }
//
//    Box(
//        modifier = Modifier
//            .fillMaxSize(),
//        contentAlignment = Alignment.Center
//    ) {
//        Column(
//            modifier = Modifier
//                .fillMaxWidth(0.6f)
//                .aspectRatio(1f)
//                .border(
//                    BorderStroke(
//                        5.dp, Color.Blue
//                    ),
//                    RoundedCornerShape(10.dp)
//                ),
//            verticalArrangement = Arrangement.Center,
//            horizontalAlignment = Alignment.CenterHorizontally
//        ) {
//            if (bleConnectionState == ConnectionState.CurrentlyInitializing) {
//                Column(
//                    modifier = Modifier
//                        .fillMaxWidth(),
//                    verticalArrangement = Arrangement.spacedBy(5.dp),
//                    horizontalAlignment = Alignment.CenterHorizontally
//                ) {
//                    CircularProgressIndicator()
//                    if (viewModel.initializingMessage != null) {
//                        Text(
//                            text = viewModel.initializingMessage!!
//                        )
//                    }
//                }
//            } else if (!permissionState.allPermissionsGranted) {
//                Text(
//                    text = "Go to the app setting and allow the missing permissions.",
//                    style = MaterialTheme.typography.bodyMedium,
//                    modifier = Modifier.padding(10.dp),
//                    textAlign = TextAlign.Center
//                )
//            } else if (viewModel.errorMessage != null) {
//                Column(
//                    modifier = Modifier.fillMaxSize(),
//                    verticalArrangement = Arrangement.Center,
//                    horizontalAlignment = Alignment.CenterHorizontally
//                ) {
//                    Text(
//                        text = viewModel.errorMessage!!
//                    )
//                    Button(
//                        onClick = {
//                            if (permissionState.allPermissionsGranted) {
//                                viewModel.initializeConnection()
//                            }
//                        }
//                    ) {
//                        Text(
//                            "Try again"
//                        )
//                    }
//                }
//            } else if (bleConnectionState == ConnectionState.Connected) {
//                Column(
//                    modifier = Modifier.fillMaxSize(),
//                    horizontalAlignment = Alignment.CenterHorizontally,
//                    verticalArrangement = Arrangement.Center
//                ) {
//                    Text(
//                        text = "pointx: ${viewModel.pointX}",
//                        style = MaterialTheme.typography.headlineSmall
//                    )
//                    Text(
//                        text = "Temperature: ${viewModel.pointY}",
//                        style = MaterialTheme.typography.headlineSmall
//                    )
//                    Text(
//                        text = "Temperature: ${viewModel.pointZ}",
//                        style = MaterialTheme.typography.headlineSmall
//                    )
//                }
//            } else if (bleConnectionState == ConnectionState.Disconnected) {
//                Button(onClick = {
//                    viewModel.initializeConnection()
//                }) {
//                    Text("Initialize again")
//                }
//            }
//        }
//    }
//
//
//
//}

@OptIn(ExperimentalPermissionsApi::class)
@Composable
fun SignLanguageIdentificationScreen(
    onBluetoothStateChanged: () -> Unit,
    viewModel: SignLanguageViewModel = hiltViewModel()
) {

    SystemBroadcastReceiver(systemAction = BluetoothAdapter.ACTION_STATE_CHANGED) { bluetoothState ->
        val action = bluetoothState?.action ?: return@SystemBroadcastReceiver
        if(action == BluetoothAdapter.ACTION_STATE_CHANGED){
            onBluetoothStateChanged()
        }
    }

    val permissionState =
        rememberMultiplePermissionsState(permissions = PermissionUtils.permissions)
    val lifecycleOwner = LocalLifecycleOwner.current
    val bleConnectionState = viewModel.connectionState

    DisposableEffect(
        key1 = lifecycleOwner,
        effect = {
            val observer = LifecycleEventObserver { _, event ->
                if (event == Lifecycle.Event.ON_START) {
                    permissionState.launchMultiplePermissionRequest()
                    if (permissionState.allPermissionsGranted && bleConnectionState == ConnectionState.Disconnected) {
                        viewModel.reconnect()
                    }
                }
                if (event == Lifecycle.Event.ON_STOP) {
                    if (bleConnectionState == ConnectionState.Connected) {
                        viewModel.disconnect()
                    }
                }
            }
            lifecycleOwner.lifecycle.addObserver(observer)

            onDispose {
                lifecycleOwner.lifecycle.removeObserver(observer)
            }
        }
    )

    LaunchedEffect(key1 = permissionState.allPermissionsGranted) {
        if (permissionState.allPermissionsGranted) {
            if (bleConnectionState == ConnectionState.Uninitialized) {
                viewModel.initializeConnection()
            }
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth(0.6f)
                .aspectRatio(1f)
                .border(
                    BorderStroke(
                        5.dp, Color.Blue
                    ),
                    RoundedCornerShape(10.dp)
                ),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            if (bleConnectionState == ConnectionState.CurrentlyInitializing) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth(),
                    verticalArrangement = Arrangement.spacedBy(5.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    CircularProgressIndicator()
                    if (viewModel.initializingMessage != null) {
                        Text(
                            text = viewModel.initializingMessage!!
                        )
                    }
                }
            } else if (!permissionState.allPermissionsGranted) {
                Text(
                    text = "Go to the app setting and allow the missing permissions.",
                    style = MaterialTheme.typography.bodyMedium,
                    modifier = Modifier.padding(10.dp),
                    textAlign = TextAlign.Center
                )
            } else if (viewModel.errorMessage != null) {
                Column(
                    modifier = Modifier.fillMaxSize(),
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = viewModel.errorMessage!!
                    )
                    Button(
                        onClick = {
                            if (permissionState.allPermissionsGranted) {
                                viewModel.initializeConnection()
                            }
                        }
                    ) {
                        Text(
                            "Try again"
                        )
                    }
                }
            } else if (bleConnectionState == ConnectionState.Connected) {
                Column(
                    modifier = Modifier.fillMaxSize(),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    Text(
                        text = "Humidity: ${viewModel.pointX}",
                        style = MaterialTheme.typography.headlineSmall
                    )
                    Text(
                        text = "Temperature: ${viewModel.pointY}",
                        style = MaterialTheme.typography.headlineSmall
                    )

                    Text(
                        text = "Temperature: ${viewModel.pointZ}",
                        style = MaterialTheme.typography.headlineSmall
                    )
                }
            } else if (bleConnectionState == ConnectionState.Disconnected) {
                Button(onClick = {
                    viewModel.initializeConnection()
                }) {
                    Text("Initialize again")
                }
            }
        }
    }

}