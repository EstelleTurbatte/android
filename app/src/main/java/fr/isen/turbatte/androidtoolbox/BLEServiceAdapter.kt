package fr.isen.turbatte.androidtoolbox

import android.app.AlertDialog
import android.bluetooth.BluetoothGatt
import android.bluetooth.BluetoothGattCharacteristic
import android.bluetooth.BluetoothGattDescriptor
import android.content.Context
import android.content.DialogInterface
import android.graphics.Color
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.thoughtbot.expandablerecyclerview.ExpandableRecyclerViewAdapter
import com.thoughtbot.expandablerecyclerview.models.ExpandableGroup
import com.thoughtbot.expandablerecyclerview.viewholders.ChildViewHolder
import com.thoughtbot.expandablerecyclerview.viewholders.GroupViewHolder
import kotlinx.android.synthetic.main.activity_ble_device_cell.view.*
import kotlinx.android.synthetic.main.activity_ble_device_cell.view.UUIDTextView
import kotlinx.android.synthetic.main.activity_recyclerview_expandable_cell.view.*
import kotlinx.android.synthetic.main.alert_dialog_builder_bluetooth.view.*
import okio.Utf8.size


class BLEServiceAdapter(
    private val serviceList: MutableList<BLEService>,
    val context: Context,
    private val gatt: BluetoothGatt?
) :
    ExpandableRecyclerViewAdapter<BLEServiceAdapter.ServiceViewHolder, BLEServiceAdapter.CharasteristicViewHolder>(
        serviceList
    ) {

    private var notity: Boolean = false

    class ServiceViewHolder(itemView: View) : GroupViewHolder(itemView) {
        val serviceName: TextView = itemView.serviceNameTextView
        val serviceUUID: TextView = itemView.UUIDTextView

        override fun expand() {
            super.expand()
            itemView.arrowImageView.setImageResource(R.drawable.ic_keyboard_arrow_up_black_24dp)
        }

        override fun collapse() {
            super.collapse()
            itemView.arrowImageView.setImageResource(R.drawable.ic_keyboard_arrow_down_black_24dp)
        }
    }

    class CharasteristicViewHolder(itemView: View) : ChildViewHolder(itemView) {
        val characteristicUUID: TextView = itemView.UUIDTextView
        val characteristicName: TextView = itemView.CharacteristiqueTextView
        val characteristicPropreties: TextView = itemView.ProprieteTextView
        val characteristicValueSend: TextView = itemView.ValeurEnvoyeeTextView
        val characteristicValueReceived: TextView = itemView.ValeurRecueTextView


        val ReadAction: Button = itemView.LectureButton
        val WriteAction: Button = itemView.EcritureButton
        val NotificationAction: Button = itemView.NotificationButton
    }


    override fun onCreateGroupViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ServiceViewHolder =
        ServiceViewHolder(
            LayoutInflater.from(parent.context).inflate(
                R.layout.activity_ble_device_cell,
                parent,
                false
            )
        )

    override fun onCreateChildViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): CharasteristicViewHolder =
        CharasteristicViewHolder(
            LayoutInflater.from(parent.context).inflate(
                R.layout.activity_recyclerview_expandable_cell,
                parent,
                false
            )
        )

    override fun onBindChildViewHolder(
        holder: CharasteristicViewHolder, flatPosition: Int, group: ExpandableGroup<*>,
        childIndex: Int
    ) {
        val characteristic: BluetoothGattCharacteristic = (group as BLEService).items[childIndex]
        val uuid = "UUID : ${characteristic.uuid}"
        holder.characteristicUUID.text = uuid

        //val title = BLEUUIDAttributes.getBLEAttributeFromUUID(characteristic.uuid.toString()).title

        holder.ReadAction.setBackgroundColor(Color.parseColor("#A0A3A2"))
        holder.NotificationAction.setBackgroundColor(Color.parseColor("#A0A3A2"))
        holder.WriteAction.setBackgroundColor(Color.parseColor("#A0A3A2"))

        val propriete = characteristic.properties
        var currentPropriete: String = "Propriétés : "

        if (getProperties(propriete).contains("WRITE")) {
            holder.WriteAction.setBackgroundColor(Color.parseColor("#F10B0B"))
            currentPropriete += " Ecriture"
        }
        if (getProperties(propriete).contains("READ")) {
            holder.ReadAction.setBackgroundColor(Color.parseColor("#F10B0B"))
            currentPropriete += " Lecture"
        }
        if (getProperties(propriete).contains("NOTIFY")) {
            holder.NotificationAction.setBackgroundColor(Color.parseColor("#F10B0B"))
            currentPropriete += " Notifier"
        }

        holder.characteristicPropreties.text = currentPropriete
        val value: String = ""
        var valeur: ByteArray = value.toByteArray()

        holder.WriteAction.setOnClickListener {
            val builder = AlertDialog.Builder(context)
            val editView = View.inflate(context, R.layout.alert_dialog_builder_bluetooth, null)
            builder.setView(editView)
            builder.setTitle("ECRITURE")
            builder.setNegativeButton(
                "Annuler",
                DialogInterface.OnClickListener { _, _ -> })
            builder.setPositiveButton("Ecrire", DialogInterface.OnClickListener { _, _ ->

                valeur = editView.valeurInputText.text.toString().toByteArray(Charsets.UTF_8)
                val test = String(valeur)
                val text = "valeur envoyée :  $test"
                holder.characteristicValueSend.text = text
                characteristic.setValue(valeur)

                val verif1 = gatt?.writeCharacteristic(characteristic)
                Log.i("erreur écriture: ", verif1.toString())

                val verif2 = gatt?.readCharacteristic(characteristic)
                Log.i("erreur Lecture: ", verif2.toString())
            })
            builder.show()
        }

        holder.ReadAction.setOnClickListener {
            val verif = gatt?.readCharacteristic(characteristic)
            Log.i("erreur Lecture: ", verif.toString())

            val reception = String(characteristic.value)
            if (reception != null) {
                val valeurRecue = "valeur recue : $reception"
                holder.characteristicValueReceived.text = valeurRecue
            }
        }

        holder.NotificationAction.setOnClickListener {

            if (!notity) {
                notity = true
                gatt?.setCharacteristicNotification(characteristic, true)

                if (characteristic.descriptors.size > 0) {

                    val descriptors = characteristic.descriptors
                    for (descriptor in descriptors) {
                        if (characteristic.properties and BluetoothGattCharacteristic.PROPERTY_NOTIFY != 0) {
                            descriptor.value = BluetoothGattDescriptor.ENABLE_NOTIFICATION_VALUE
                        } else if (characteristic.properties and BluetoothGattCharacteristic.PROPERTY_INDICATE != 0) {
                            descriptor.value = BluetoothGattDescriptor.ENABLE_INDICATION_VALUE
                        }
                        gatt?.writeDescriptor(descriptor)
                    }
                }
            } else {
                notity = false
                gatt?.setCharacteristicNotification(characteristic, false)
            }
        }
        if (characteristic.uuid.toString() == BLEUUIDAttributes.getBLEAttributeFromUUID(characteristic.uuid.toString()).uuid && notity) {
            if (characteristic.value == null)
                holder.characteristicValueReceived.text = "Valeur : 0"
            else {
                holder.characteristicValueReceived.text =
                    "Valeur : ${byteArrayToHexString(characteristic.value)}"
            }
        }
    }


    override fun onBindGroupViewHolder(
        holder: ServiceViewHolder, flatPosition: Int,
        group: ExpandableGroup<*>
    ) {
        val title = BLEUUIDAttributes.getBLEAttributeFromUUID(group.title).title
        holder.serviceUUID.text = group.title
        holder.serviceName.text = title

    }


    private fun getProperties(propriete: Int): StringBuilder {
        val valeur = StringBuilder()

        if (propriete and BluetoothGattCharacteristic.PROPERTY_WRITE != 0) {
            valeur.append("WRITE")
        }
        if (propriete and BluetoothGattCharacteristic.PROPERTY_READ != 0) {
            valeur.append("READ")
        }
        if (propriete and BluetoothGattCharacteristic.PROPERTY_NOTIFY != 0) {
            valeur.append("NOTIFY")
        }
        return valeur
    }

    private fun byteArrayToHexString(array: ByteArray): String {
        val result = StringBuilder(array.size * 2)
        for ( byte in array ) {
            val toAppend = String.format("%X", byte)
            result.append(toAppend).append("-")
        }
        result.setLength(result.length - 1)
        return result.toString()
    }

}
