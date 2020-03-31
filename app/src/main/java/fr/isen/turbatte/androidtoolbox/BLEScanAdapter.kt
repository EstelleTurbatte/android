package fr.isen.turbatte.androidtoolbox

import android.bluetooth.BluetoothDevice
import android.bluetooth.le.ScanResult
import android.content.Context
import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.activity_ble_cell.view.*

class BLEScanAdapter(
    private val scanResults: ArrayList<ScanResult>,
    val deviceClickListener: (BluetoothDevice) -> Unit
) :
    RecyclerView.Adapter<BLEScanAdapter.BLEScanViewHolder>() {

    class BLEScanViewHolder(
        scanView: View,
        private val scanResults: ArrayList<ScanResult>,
        val deviceClickListener: (BluetoothDevice) -> Unit
    ) :
        RecyclerView.ViewHolder(scanView) {

        val distance: TextView = scanView.distanceTextView
        private val nameDevice: TextView = scanView.nameDeviceTextView
        private val MACAdress: TextView = scanView.MACAdressTextView
        private val layout = scanView.cellBleLayout

        fun pushInfo(position: Int) {
            distance.text = scanResults[position].rssi.toString()
            nameDevice.text = scanResults[position].device.name
            MACAdress.text = scanResults[position].device.address

            layout.setOnClickListener {
                deviceClickListener.invoke(scanResults[position].device)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BLEScanViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val view: View = inflater.inflate(R.layout.activity_ble_cell, parent, false)
        return BLEScanAdapter.BLEScanViewHolder(view, scanResults, deviceClickListener)
    }

    override fun getItemCount(): Int = scanResults.size

    override fun onBindViewHolder(holder: BLEScanViewHolder, position: Int) {
        holder.pushInfo(position)
        val distance = scanResults[position].rssi

        if (distance < -80) {
            holder.distance.setBackgroundColor(Color.parseColor("#EC1515"))
        } else if (distance >= -80 && distance < -60) {
            holder.distance.distanceTextView.setBackgroundColor(Color.parseColor("#EE4747"))
        } else if (distance > -40) {
            holder.distance.distanceTextView.setBackgroundColor(Color.parseColor("#F37979"))
        }
}

fun addDeviceToList(result: ScanResult) {
    val index = scanResults.indexOfFirst { it.device.address == result.device.address }
    if (index != -1) {
        scanResults[index] = result
    } else {
        scanResults.add(result)
    }
}
}

