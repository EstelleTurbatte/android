package fr.isen.turbatte.androidtoolbox

import android.app.AlertDialog
import android.bluetooth.BluetoothGattCharacteristic
import android.content.Context
import android.content.DialogInterface
import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
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
    val context: Context
    /*private val readCharacteristic: (BluetoothGattCharacteristic) -> Unit,
    private val writeCharacteristic: (BluetoothGattCharacteristic) -> Unit,
    private val notifyCharacteristic: (BluetoothGattCharacteristic) -> Unit*/
) :
    ExpandableRecyclerViewAdapter<BLEServiceAdapter.ServiceViewHolder, BLEServiceAdapter.CharasteristicViewHolder>(
        serviceList
    ) {

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

        val title = BLEUUIDAttributes.getBLEAttributeFromUUID(characteristic.uuid.toString()).title

        holder.ReadAction.setBackgroundColor(Color.parseColor("#A0A3A2"))
        holder.NotificationAction.setBackgroundColor(Color.parseColor("#A0A3A2"))
        holder.WriteAction.setBackgroundColor(Color.parseColor("#A0A3A2"))

        val propriete = characteristic.properties
        var currentPropriete: String = "Propriétés : "

        if (getProperties(propriete).contains("WRITE")){
            holder.WriteAction.setBackgroundColor(Color.parseColor("#F10B0B"))
            currentPropriete += " Ecriture"
        }
        if (getProperties(propriete).contains("READ")){
            holder.ReadAction.setBackgroundColor(Color.parseColor("#F10B0B"))
            currentPropriete += " Lecture"
        }
        if (getProperties(propriete).contains("NOTIFY")){
            holder.NotificationAction.setBackgroundColor(Color.parseColor("#F10B0B"))
            currentPropriete += " Notifier"
        }

        holder.characteristicPropreties.text = currentPropriete
        var value: String = ""
        //var test: ByteArray = 00

        holder.WriteAction.setOnClickListener {
            val builder = AlertDialog.Builder(context)
            val editView = View.inflate(context, R.layout.alert_dialog_builder_bluetooth, null)
            builder.setView(editView)
            builder.setTitle("ECRITURE")
            builder.setNegativeButton("Annuler", DialogInterface.OnClickListener { dialog, which ->  })
            builder.setPositiveButton("Ecrire", DialogInterface.OnClickListener {
                    _, _ ->
                value =  editView.valeurInputText.text.toString()
                //test = editView.valeurInputText.toString().toByteArray(Charsets.UTF_8)
                //val text = "valeur envoyée :  $value $test"
                //holder.characteristicValueSend.text = text
            })
            builder.show()
        }

        //characteristic.value = json.toString().toByteArray()

        holder.ReadAction.setOnClickListener {
            //test = test.plus(1)
            //val reponse = String(test, Charsets.UTF_8)
            //val text = "valeur reçue : $test"
            //holder.characteristicValueReceived.text = reponse
        }

        holder.NotificationAction.setOnClickListener {
            val builder = AlertDialog.Builder(context)
            val editView = View.inflate(context, R.layout.alert_dialog_builder_bluetooth, null)
            builder.setView(editView)
            builder.setNegativeButton("Annuler", DialogInterface.OnClickListener { dialog, which ->  })
            builder.setPositiveButton("Notifier", DialogInterface.OnClickListener {
                    _, _ ->
                val text = editView.valeurInputText.text.toString()
                characteristic.setValue(text)
            })
            builder.show()
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

    /*
    la classe StringBuilder permet d’améliorer les performances quand il s’agit de concaténer un grand nombre de chaînes dans une boucle.
     */

    private fun  getProperties (propriete: Int): StringBuilder {
        val valeur = StringBuilder()

        if (propriete and BluetoothGattCharacteristic.PROPERTY_WRITE != 0){
            valeur.append("WRITE")
        }
        if (propriete and BluetoothGattCharacteristic.PROPERTY_READ != 0){
            valeur.append("READ")
        }
        if (propriete and BluetoothGattCharacteristic.PROPERTY_NOTIFY != 0){
            valeur.append("NOTIFY")
        }
        return valeur
    }


}
