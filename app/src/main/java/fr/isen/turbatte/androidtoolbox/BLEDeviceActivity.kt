package fr.isen.turbatte.androidtoolbox

import android.bluetooth.*
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import kotlinx.android.synthetic.main.activity_ble_cell.*
import kotlinx.android.synthetic.main.activity_bledevice.*
import kotlinx.android.synthetic.main.activity_bluetooth.*

class BLEDeviceActivity : AppCompatActivity() {

    private var bluetoothGatt: BluetoothGatt? = null
    private var TAG:String = "MyActivity";

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_bledevice)

        val device: BluetoothDevice = intent.getParcelableExtra("ble_device")
        //bluetoothGatt = device.connectGatt(this, true, gattCallback)
        connectToDevice(device)
        deviceNameTextView.text = device?.name
    }

    /*private val gattCallback = object : BluetoothGattCallback() {
        override fun onConnectionStateChange(
            gatt: BluetoothGatt,
            status: Int,
            newState: Int
        ) {
            when (newState) {
                BluetoothProfile.STATE_CONNECTED -> {
                    runOnUiThread {
                        statutDeviceTextView.text = STATE_CONNECTED
                    }
                    bluetoothGatt?.discoverServices()
                    Log.i(TAG, "Connected to GATT server.")
                }
                BluetoothProfile.STATE_DISCONNECTED -> {
                    runOnUiThread {
                        statutDeviceTextView.text = STATE_DISCONNECTED
                    }
                    Log.i(TAG, "Disconnected from GATT server.")
                }
            }
        }


    }*/

    //faire notre méthode et récupérer l'état et l'afficher ! mettre à jour le statut.

    private fun connectToDevice(device: BluetoothDevice?) {
        bluetoothGatt = device?.connectGatt(this, true, object : BluetoothGattCallback() {
            override fun onConnectionStateChange(gatt: BluetoothGatt?, status: Int, newState: Int) {
                super.onConnectionStateChange(gatt, status, newState)
                if(newState == BluetoothProfile.STATE_CONNECTED) {
                    runOnUiThread {
                       // progressBar.visibility = View.INVISIBLE
                        //DividerView.visibility = View.VISIBLE
                        statutDeviceTextView.text = STATE_CONNECTED
                    }
                    gatt?.discoverServices()
                }else{
                    statutDeviceTextView.text = STATE_DISCONNECTED
                }
            }

            override fun onServicesDiscovered(gatt: BluetoothGatt?, status: Int) {
                super.onServicesDiscovered(gatt, status)
                runOnUiThread {
                    bleDeviceRecyclerView.adapter = BLEServiceAdapter(
                    gatt?.services?.map {
                        BLEService(
                            it.uuid.toString(),
                            it.characteristics
                        )
                    }?.toMutableList() ?: arrayListOf(), this@BLEDeviceActivity
                        /*{ characteristic -> gatt?.readCharacteristic((characteristic))},
                        { charactristic -> writeIntoCharacterisitic(gatt, charactristic)},
                        { characterisitic, enable ->
                            toggleNotificationOnCharacteristic(gatt, characterisitic, enable)
                        }*/
                    )
                    bleDeviceRecyclerView.layoutManager = LinearLayoutManager(this@BLEDeviceActivity)
                }

            }
        })
        bluetoothGatt?.connect()
    }

    private fun toggleNotificationOnCharacteristic(
        gatt: BluetoothGatt,
        characterisitic: BluetoothGattCharacteristic,
        enable: Boolean
    ){
        characterisitic.descriptors.forEach {

        }
    }

    override fun onStop() {
        super.onStop()
        bluetoothGatt?.close()
    }

    companion object {
        private const val STATE_DISCONNECTED = "Disconnected"
        private const val STATE_CONNECTING = "Connecting"
        private const val STATE_CONNECTED = "Connected"
        const val ACTION_GATT_CONNECTED = "com.example.bluetooth.le.ACTION_GATT_CONNECTED"
        const val ACTION_GATT_DISCONNECTED = "com.example.bluetooth.le.ACTION_GATT_DISCONNECTED"
        const val ACTION_GATT_SERVICES_DISCOVERED = "com.example.bluetooth.le.ACTION_GATT_SERVICES_DISCOVERED"
        const val ACTION_DATA_AVAILABLE = "com.example.bluetooth.le.ACTION_DATA_AVAILABLE"
        const val EXTRA_DATA = "com.example.bluetooth.le.EXTRA_DATA"
    }

}


