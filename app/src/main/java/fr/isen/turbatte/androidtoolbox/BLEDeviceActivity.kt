package fr.isen.turbatte.androidtoolbox

import android.bluetooth.*
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.recyclerview.widget.LinearLayoutManager
import kotlinx.android.synthetic.main.activity_bledevice.*

class BLEDeviceActivity : AppCompatActivity() {

    private var bluetoothGatt: BluetoothGatt? = null
    private var TAG: String = "MyActivity"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_bledevice)

        val device: BluetoothDevice = intent.getParcelableExtra("ble_device")
        connectToDevice(device)
        deviceNameTextView.text = device.name
    }

    private fun connectToDevice(device: BluetoothDevice?) {
        bluetoothGatt = device?.connectGatt(this, true, object : BluetoothGattCallback() {
            override fun onConnectionStateChange(gatt: BluetoothGatt?, status: Int, newState: Int) {
                super.onConnectionStateChange(gatt, status, newState)
                if (newState == BluetoothProfile.STATE_CONNECTED) {
                    runOnUiThread {
                        statutDeviceTextView.text = STATE_CONNECTED
                    }
                    gatt?.discoverServices()
                } else {
                    statutDeviceTextView.text = STATE_DISCONNECTED
                }
            }

            override fun onCharacteristicRead(
                gatt: BluetoothGatt?,
                characteristic: BluetoothGattCharacteristic?,
                status: Int
            ) {
                super.onCharacteristicRead(gatt, characteristic, status)
                runOnUiThread {
                    bleDeviceRecyclerView.adapter?.notifyDataSetChanged()

                }
            }

            override fun onCharacteristicWrite(
                gatt: BluetoothGatt?,
                characteristic: BluetoothGattCharacteristic?,
                status: Int
            ) {
                super.onCharacteristicWrite(gatt, characteristic, status)
            }

            override fun onCharacteristicChanged(
                gatt: BluetoothGatt?,
                characteristic: BluetoothGattCharacteristic?
            ) {
                Log.i(
                    TAG,
                    "**THIS IS A NOTIFY MESSAGE" + characteristic?.value + characteristic?.uuid
                )
                runOnUiThread {
                    bleDeviceRecyclerView.adapter?.notifyDataSetChanged()

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
                        }?.toMutableList() ?: arrayListOf(), this@BLEDeviceActivity, gatt

                    )
                    bleDeviceRecyclerView.layoutManager =
                        LinearLayoutManager(this@BLEDeviceActivity)
                }

            }
        })
        bluetoothGatt?.connect()
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
        const val ACTION_GATT_SERVICES_DISCOVERED =
            "com.example.bluetooth.le.ACTION_GATT_SERVICES_DISCOVERED"
        const val ACTION_DATA_AVAILABLE = "com.example.bluetooth.le.ACTION_DATA_AVAILABLE"
        const val EXTRA_DATA = "com.example.bluetooth.le.EXTRA_DATA"
    }


}



