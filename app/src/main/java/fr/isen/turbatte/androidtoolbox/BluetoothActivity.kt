package fr.isen.turbatte.androidtoolbox

import android.bluetooth.*
import android.bluetooth.le.ScanCallback
import android.bluetooth.le.ScanResult
import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager

import kotlinx.android.synthetic.main.activity_bluetooth.*

class BluetoothActivity : AppCompatActivity() {

    private var lancerScan: String = "Lancer le Scan BLE"
    private var scanEnCours: String = "Scan BLE en cours"
    private var mScanning: Boolean = false
    private var bluetoothGatt: BluetoothGatt? = null
    private lateinit var handler: Handler
    private lateinit var adapter: BLEScanAdapter


    private val bluetoothAdapter: BluetoothAdapter? by lazy(LazyThreadSafetyMode.NONE) {
        val bluetoothManager = getSystemService(Context.BLUETOOTH_SERVICE) as BluetoothManager
        bluetoothManager.adapter
    }


    private val isBLEEnabled: Boolean
        get() = bluetoothAdapter?.isEnabled == true

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_bluetooth)
        laodingView.text = lancerScan

        DividerView.visibility = View.VISIBLE
        progressBar.visibility = View.GONE
        when {
            isBLEEnabled -> {
                initScan()
            }
            bluetoothAdapter != null -> {
                val enableIntent = Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE)
                startActivityForResult(enableIntent, REQUEST_ENABLE_BT)
            }
            else -> {
                blefailedText.visibility = View.VISIBLE
            }
        }
    }

    private fun initScan() {
        handler = Handler()

        adapter = BLEScanAdapter(arrayListOf(), ::onDeviceClicked)
        bleRecyclerView.adapter = adapter
        bleRecyclerView.layoutManager = LinearLayoutManager(this)

        playPauseImageView.setOnClickListener { scanLeDevice(true) }
    }

    private fun scanLeDevice(enable: Boolean) {
        bluetoothAdapter?.bluetoothLeScanner?.apply {
            DividerView.visibility = View.VISIBLE
            when {
                enable -> {
                    handler.postDelayed({
                        mScanning = false
                        stopScan(leScanCallBack)
                        playPauseImageView.setImageResource(R.drawable.ic_play_arrow_black_24dp)
                        progressBar.visibility = View.GONE
                        DividerView.visibility = View.VISIBLE
                        laodingView.text = lancerScan
                    }, SCAN_PERIOD)
                    mScanning = true
                    startScan(leScanCallBack)
                    playPauseImageView.setImageResource(R.drawable.ic_pause_black_24dp)
                    DividerView.visibility = View.GONE
                    progressBar.visibility = View.VISIBLE
                    laodingView.text = scanEnCours
                }
                else -> {
                    mScanning = false
                    stopScan(leScanCallBack)
                    playPauseImageView.setImageResource(R.drawable.ic_play_arrow_black_24dp)
                    progressBar.visibility = View.GONE
                    DividerView.visibility = View.VISIBLE
                    laodingView.text = lancerScan
                }
            }
        }
    }

    private val leScanCallBack = object : ScanCallback() {
        override fun onScanResult(callbackType: Int, result: ScanResult) {
            super.onScanResult(callbackType, result)
            Log.w("BLEActivity", "${result.device}")
            runOnUiThread {
                adapter.addDeviceToList(result)
                adapter.notifyDataSetChanged()
            }
        }
    }

    override fun onPause() {
        super.onPause()
        if (isBLEEnabled) {
            scanLeDevice(false)
            playPauseImageView.setImageResource(R.drawable.ic_play_arrow_black_24dp)
            progressBar.visibility = View.GONE
            DividerView.visibility = View.VISIBLE
            laodingView.text = lancerScan
        }
    }

    private fun onDeviceClicked(device: BluetoothDevice) {
        val intent = Intent(this, BLEDeviceActivity::class.java)
        intent.putExtra("ble_device", device)
        startActivity(intent)
    }


    companion object {
        private const val REQUEST_ENABLE_BT = 89
        private const val SCAN_PERIOD: Long = 10000
    }
}


