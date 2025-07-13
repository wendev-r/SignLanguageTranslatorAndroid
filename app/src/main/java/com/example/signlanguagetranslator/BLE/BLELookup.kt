package com.example.signlanguagetranslator.BLE
import android.Manifest
import android.R
import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothManager
import android.content.Context
import androidx.annotation.RequiresPermission
import android.bluetooth.le.ScanCallback
import android.bluetooth.le.ScanResult
import android.bluetooth.BluetoothDevice
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.TextView
import androidx.core.content.ContextCompat.getSystemService

class LeDeviceListAdapter(context: Context) :
    ArrayAdapter<BluetoothDevice>(context, 0, ArrayList()) {

    private val devices = ArrayList<BluetoothDevice>()

    fun addDevice(device: BluetoothDevice) {
        if (!devices.contains(device)) {
            devices.add(device)
            clear()
            addAll(devices)
        }
    }

    override fun getCount(): Int = devices.size

    override fun getItem(position: Int): BluetoothDevice = devices[position]

    @RequiresPermission(Manifest.permission.BLUETOOTH_CONNECT)
    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        val view = convertView ?: LayoutInflater.from(context)
            .inflate(R.layout.simple_list_item_2, parent, false)

        val device = getItem(position)
        val deviceName = device.name ?: "Unknown Device"
        val deviceAddress = device.address

        view.findViewById<TextView>(R.id.text1).text = deviceName
        view.findViewById<TextView>(R.id.text2).text = deviceAddress

        return view
    }
}
class FindBLE(context: Context) {
    private val bluetoothAdapter: BluetoothAdapter? by lazy {
        val bluetoothManager = context.getSystemService(Context.BLUETOOTH_SERVICE) as BluetoothManager
        bluetoothManager.adapter
    }
    private val leDeviceListAdapter = LeDeviceListAdapter(context)

    private val leScanCallback = object : ScanCallback() {
        override fun onScanResult(callbackType: Int, result: ScanResult) {
            super.onScanResult(callbackType, result)
            leDeviceListAdapter.addDevice(result.device)
            leDeviceListAdapter.notifyDataSetChanged()        }

        override fun onBatchScanResults(results: List<ScanResult>) {
            super.onBatchScanResults(results)
            // Handle batch of scan results here
        }

        override fun onScanFailed(errorCode: Int) {
            super.onScanFailed(errorCode)
            // Handle scan failure here
        }
    }


    private val bluetoothLeScanner = bluetoothAdapter?.bluetoothLeScanner
    private var scanning = false
    private val handler = android.os.Handler()


    // Stops scanning after 20 seconds.
    private val SCAN_PERIOD: Long = 20000

    @RequiresPermission(Manifest.permission.BLUETOOTH_SCAN)
    private fun scanLeDevice() {

        if (!scanning) {
            handler.postDelayed({
                scanning = false
                bluetoothLeScanner?.stopScan(leScanCallback)
            }, SCAN_PERIOD)
            scanning = true
            bluetoothLeScanner?.startScan(leScanCallback)
        } else {
            scanning = false
            bluetoothLeScanner?.stopScan(leScanCallback)
        }
    }

}