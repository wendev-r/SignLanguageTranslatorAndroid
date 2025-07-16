package com.example.signlanguagetranslator.data.ble

import android.annotation.SuppressLint
import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothGatt
import android.bluetooth.BluetoothGattCallback
import android.bluetooth.BluetoothGattCharacteristic
import android.bluetooth.BluetoothGattDescriptor
import android.bluetooth.BluetoothProfile
import android.bluetooth.le.ScanCallback
import android.bluetooth.le.ScanResult
import android.bluetooth.le.ScanSettings
import android.content.Context
import android.util.Log
import com.example.signlanguagetranslator.data.ConnectionState
import com.example.signlanguagetranslator.data.GloveResult
import com.example.signlanguagetranslator.data.GloveSensorReceiveManager
import com.example.signlanguagetranslator.util.Resource
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.launch
import java.util.UUID
import javax.inject.Inject

//@SuppressLint("MissingPermission")
//class GloveSensorBLEReceiveManager @Inject constructor(
//    private val bluetoothAdapter: BluetoothAdapter,
//    private val context: Context
//) : GloveSensorReceiveManager {
//
//    private val DEVICE_NAME = "NimBLE_GATT"
//    private val SERVICE_UUID = "0000180D-0000-1000-8000-00805F9B34FB"
//    private val CHAR_UUID = "00002A37-0000-1000-8000-00805F9B34FB"
//    override val data: MutableSharedFlow<Resource<GloveResult>> = MutableSharedFlow()
//
//    private val bleScanner by lazy {
//        bluetoothAdapter.bluetoothLeScanner
//    }
//
//    private val scanSettings = ScanSettings.Builder()
//        .setScanMode(ScanSettings.SCAN_MODE_LOW_LATENCY)
//        .build()
//
//    private var gatt: BluetoothGatt? = null
//
//    private var isScanning = false
//
//    private val coroutineScope = CoroutineScope(Dispatchers.Default)
//
//    private val scanCallback = object : ScanCallback() {
//        override fun onScanResult(callbackType: Int, result: ScanResult) {
//            if (result.device.name == DEVICE_NAME) {
//                coroutineScope.launch {
//                    data.emit(Resource.Loading(message = "Connecting to device..."))
//                }
//                if (isScanning) {
//                    result.device.connectGatt(context, false, gattCallback)
//                    isScanning = false
//                    bleScanner.stopScan(this)
//
//                }
//            }
//        }
//    }
//    private var currentConnectionAttempt = 1
//    private val MAX_CONNECTION_ATTEMPTS = 5
//    private val gattCallback = object : BluetoothGattCallback() {
//        override fun onConnectionStateChange(gatt: BluetoothGatt, status: Int, newState: Int) {
//            if (status == BluetoothGatt.GATT_SUCCESS) {
//                if (newState == BluetoothProfile.STATE_CONNECTED) {
//                    coroutineScope.launch { data.emit(Resource.Loading(message = "Discovering Services...")) }
//
//                    gatt.discoverServices()
//                    this@GloveSensorBLEReceiveManager.gatt = gatt
//                } else if (newState == BluetoothProfile.STATE_DISCONNECTED) {
//                coroutineScope.launch {
//                    data.emit(
//                        Resource.Success(
//                            data = GloveResult(
//                                0f, 0f, 0f,
//                                ConnectionState.Disconnected
//                            )
//                        )
//                    )
//                }
//                gatt.close()
//                }
//            } else {
//                gatt.close()
//                currentConnectionAttempt += 1
//                coroutineScope.launch {
//                    data.emit(
//                        Resource.Loading(
//                            message = "Attempting to connect $currentConnectionAttempt/$MAX_CONNECTION_ATTEMPTS"
//                        )
//                    )
//                }
//                    if (currentConnectionAttempt <= MAX_CONNECTION_ATTEMPTS) {
//                        startReceiving()
//                    } else {
//                        coroutineScope.launch {
//                            data.emit(Resource.Error("Cound Not Connect to BLE Device"))
//                        }
//                    }
//                }
//            }
//
//
//
//        override fun onServicesDiscovered(gatt: BluetoothGatt, status: Int) {
//            with(gatt) {
//                printGattTable()
//                coroutineScope.launch {
//                    data.emit(Resource.Loading(message = "Adjusting MTU space..."))
//                }
//                gatt.requestMtu(517)
//            }
//        }
//
//        override fun onMtuChanged(gatt: BluetoothGatt, mtu: Int, status: Int) {
//            val characteristic = findCharacteristics(SERVICE_UUID, CHAR_UUID)
//            if (characteristic == null) {
//                coroutineScope.launch {
//                    data.emit(Resource.Error(errorMessage = "Could not find glove"))
//
//                }
//                return
//            }
//            enableNotification(characteristic)
//        }
//
//        override fun onCharacteristicChanged(
//            gatt: BluetoothGatt,
//            characteristic: BluetoothGattCharacteristic
//        ) {
//            with(characteristic) {
//                when (uuid) {
//                    UUID.fromString(CHAR_UUID) -> {
//                        //XX XX XX XX XX XX
//                        val xVal = if (value.first().toInt() > 0) -1f else 1f
//                        val yVal = if (value.first().toInt() > 0) -1f else 1f
//                        val zVal = if (value.first().toInt() > 0) -1f else 1f
//                        val gloveResult = GloveResult(
//                            xVal,
//                            yVal,
//                            zVal,
//                            ConnectionState.Connected
//                        )
//                        TODO("Instead of emitting these values, we want to pass it through our ML Model to get the resulting ASL Classification and display on screen")
//                        coroutineScope.launch {
//                            data.emit(
//                                Resource.Success(data = gloveResult)
//                            )
//                        }
//                    }
//
//                    else -> Unit
//                }
//            }
//        }
//    }
//
//
//    private fun enableNotification(characteristic: BluetoothGattCharacteristic) {
//        val cccdUuid = UUID.fromString(CCCD_DESCRIPTOR_UUID)
//        val payload = when {
//            characteristic.isIndicatable() -> BluetoothGattDescriptor.ENABLE_INDICATION_VALUE
//            characteristic.isNotifiable() -> BluetoothGattDescriptor.ENABLE_NOTIFICATION_VALUE
//            else -> return
//        }
//
//        characteristic.getDescriptor(cccdUuid)?.let { cccdDescriptor ->
//            if (gatt?.setCharacteristicNotification(characteristic, true) == false) {
//                Log.d("BLEReceiveManager", "set characteristics notification failed")
//                return
//            }
//            writeDescription(cccdDescriptor, payload)
//        }
//    }
//
//    private fun writeDescription(descriptor: BluetoothGattDescriptor, payload: ByteArray) {
//        gatt?.let { gatt ->
//            descriptor.value = payload
//            gatt.writeDescriptor(descriptor)
//        } ?: error("Not connected to a BLE device!")
//    }
//
//    private fun findCharacteristics(
//        serviceUUID: String,
//        characteristicsUUID: String
//    ): BluetoothGattCharacteristic? {
//        return gatt?.services?.find { service ->
//            service.uuid.toString() == serviceUUID
//        }?.characteristics?.find { characteristics ->
//            characteristics.uuid.toString() == characteristicsUUID
//        }
//    }
//
//    override fun startReceiving() {
//        coroutineScope.launch {
//            data.emit(Resource.Loading(message = "Scanning Ble devices..."))
//        }
//        isScanning = true
//        bleScanner.startScan(null, scanSettings, scanCallback)
//    }
//
//    override fun reconnect() {
//        gatt?.connect()
//    }
//
//    override fun disconnect() {
//        gatt?.disconnect()
//    }
//
//    override fun closeConnection() {
//        bleScanner.stopScan(scanCallback)
//        val characteristic = findCharacteristics(SERVICE_UUID, CHAR_UUID)
//        if (characteristic != null) {
//            disconnectCharacteristic(characteristic)
//        }
//        gatt?.close()
//    }
//
//    private fun disconnectCharacteristic(characteristic: BluetoothGattCharacteristic) {
//        val cccdUuid = UUID.fromString(CCCD_DESCRIPTOR_UUID)
//        characteristic.getDescriptor(cccdUuid)?.let { cccdDescriptor ->
//            if (gatt?.setCharacteristicNotification(characteristic, false) == false) {
//                Log.d("BLEReceiveManager", "set charateristics notification failed")
//                return
//            }
//            writeDescription(cccdDescriptor, BluetoothGattDescriptor.DISABLE_NOTIFICATION_VALUE)
//        }
//    }
//
//}

@SuppressLint("MissingPermission")
class GloveSensorBLEReceiveManager @Inject constructor(
    private val bluetoothAdapter: BluetoothAdapter,
    private val context: Context
) : GloveSensorReceiveManager {

    private val DEVICE_NAME = "NimBLE_GATT"
    private val SERVICE_UUID = "0000180d-0000-1000-8000-00805f9b34fb"
    private val CHAR_UUID = "00002a37-0000-1000-8000-00805f9b34fb"

    override val data: MutableSharedFlow<Resource<GloveResult>> = MutableSharedFlow()

    private val bleScanner by lazy {
        bluetoothAdapter.bluetoothLeScanner
    }

    private val scanSettings = ScanSettings.Builder()
        .setScanMode(ScanSettings.SCAN_MODE_LOW_LATENCY)
        .build()

    private var gatt: BluetoothGatt? = null

    private var isScanning = false

    private val coroutineScope = CoroutineScope(Dispatchers.Default)

    private val scanCallback = object : ScanCallback() {

        override fun onScanResult(callbackType: Int, result: ScanResult) {
            if (result.device.name == DEVICE_NAME) {
                coroutineScope.launch {
                    data.emit(Resource.Loading(message = "Connecting to device..."))
                }
                if (isScanning) {
                    result.device.connectGatt(context, false, gattCallback)
                    isScanning = false
                    bleScanner.stopScan(this)
                }
            }
        }
    }

    private var currentConnectionAttempt = 1
    private var MAXIMUM_CONNECTION_ATTEMPTS = 5

    private val gattCallback = object : BluetoothGattCallback() {
        override fun onConnectionStateChange(gatt: BluetoothGatt, status: Int, newState: Int) {
            if (status == BluetoothGatt.GATT_SUCCESS) {
                if (newState == BluetoothProfile.STATE_CONNECTED) {
                    coroutineScope.launch {
                        data.emit(Resource.Loading(message = "Discovering Services..."))
                    }
                    gatt.discoverServices()
                    this@GloveSensorBLEReceiveManager.gatt = gatt
                } else if (newState == BluetoothProfile.STATE_DISCONNECTED) {
                    coroutineScope.launch {
                        data.emit(
                            Resource.Success(
                                data = GloveResult(
                                    0f,
                                    0f,
                                    0f,
                                    ConnectionState.Disconnected
                                )
                            )
                        )
                    }
                    gatt.close()
                }
            } else {
                gatt.close()
                currentConnectionAttempt += 1
                coroutineScope.launch {
                    data.emit(
                        Resource.Loading(
                            message = "Attempting to connect $currentConnectionAttempt/$MAXIMUM_CONNECTION_ATTEMPTS"
                        )
                    )
                }
                if (currentConnectionAttempt <= MAXIMUM_CONNECTION_ATTEMPTS) {
                    startReceiving()
                } else {
                    coroutineScope.launch {
                        data.emit(Resource.Error(errorMessage = "Could not connect to ble device"))
                    }
                }
            }
        }

        override fun onServicesDiscovered(gatt: BluetoothGatt, status: Int) {
            with(gatt) {
                printGattTable()
                coroutineScope.launch {
                    data.emit(Resource.Loading(message = "Adjusting MTU space..."))
                }
                gatt.requestMtu(517)
            }
        }

        override fun onMtuChanged(gatt: BluetoothGatt, mtu: Int, status: Int) {
            val characteristic = findCharacteristics(SERVICE_UUID, CHAR_UUID)
//            Toast.makeText(context, "ServiceID: $SERVICE_UUID CharID: $CHAR_UUID", Toast.LENGTH_SHORT).show()
            if (characteristic == null) {
                coroutineScope.launch {
                    data.emit(Resource.Error(errorMessage = "Could not find temp and humidity publisher"))
                }
                gatt.close()
                return
            }
            enableNotification(characteristic)
        }

        override fun onCharacteristicChanged(
            gatt: BluetoothGatt,
            characteristic: BluetoothGattCharacteristic
        ) {
            with(characteristic) {
                when (uuid) {
                    UUID.fromString(CHAR_UUID) -> {
                        //XX XX XX XX XX XX
//                        val multiplicator = if(value.first().toInt()> 0) -1 else 1
//                        val temperature = value[1].toInt() + value[2].toInt() / 10f
//                        val humidity = value[4].toInt() + value[5].toInt() / 10f
                        val tempGloveResult = GloveResult(
                            0f,
                            0f,
                            0f,
                            ConnectionState.Connected
                        )
                        coroutineScope.launch {
                            data.emit(
                                Resource.Success(data = tempGloveResult)
                            )
                        }
                    }
                    else -> Unit
                }
            }
        }


    }


    private fun enableNotification(characteristic: BluetoothGattCharacteristic) {
        val cccdUuid = UUID.fromString(CCCD_DESCRIPTOR_UUID)
        val payload = when {
            characteristic.isIndicatable() -> BluetoothGattDescriptor.ENABLE_INDICATION_VALUE
            characteristic.isNotifiable() -> BluetoothGattDescriptor.ENABLE_NOTIFICATION_VALUE
            else -> return
        }

        characteristic.getDescriptor(cccdUuid)?.let { cccdDescriptor ->
            if (gatt?.setCharacteristicNotification(characteristic, true) == false) {
                Log.d("BLEReceiveManager", "set characteristics notification failed")
                return
            }
            writeDescription(cccdDescriptor, payload)
        }
    }

    private fun writeDescription(descriptor: BluetoothGattDescriptor, payload: ByteArray) {
        gatt?.let { gatt ->
            descriptor.value = payload
            gatt.writeDescriptor(descriptor)
        } ?: error("Not connected to a BLE device!")
    }

    private fun findCharacteristics(
        serviceUUID: String,
        characteristicsUUID: String
    ): BluetoothGattCharacteristic? {
        return gatt?.services?.find { service ->
            service.uuid.toString() == serviceUUID
        }?.characteristics?.find { characteristics ->
            characteristics.uuid.toString() == characteristicsUUID
        }
    }

    override fun startReceiving() {
        coroutineScope.launch {
            data.emit(Resource.Loading(message = "Scanning Ble devices..."))
        }
        isScanning = true
        bleScanner.startScan(null, scanSettings, scanCallback)
    }

    override fun reconnect() {
        gatt?.connect()
    }

    override fun disconnect() {
        gatt?.disconnect()
    }


    override fun closeConnection() {
        bleScanner.stopScan(scanCallback)
        val characteristic = findCharacteristics(SERVICE_UUID, CHAR_UUID)
        if (characteristic != null) {
            disconnectCharacteristic(characteristic)
        }
        gatt?.close()
    }

    private fun disconnectCharacteristic(characteristic: BluetoothGattCharacteristic) {
        val cccdUuid = UUID.fromString(CCCD_DESCRIPTOR_UUID)
        characteristic.getDescriptor(cccdUuid)?.let { cccdDescriptor ->
            if (gatt?.setCharacteristicNotification(characteristic, false) == false) {
                Log.d("TempHumidReceiveManager", "set charateristics notification failed")
                return
            }
            writeDescription(cccdDescriptor, BluetoothGattDescriptor.DISABLE_NOTIFICATION_VALUE)
        }
    }

}