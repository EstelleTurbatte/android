package fr.isen.turbatte.androidtoolbox

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.activity_permission_cell.view.*

class ContactAdapter(val contact: List<String>) :
    RecyclerView.Adapter<ContactAdapter.ContactViewHolder>() {

    class ContactViewHolder(contactView: View) : RecyclerView.ViewHolder(contactView) {
        val contactName: TextView = contactView.contactName
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ContactViewHolder =
        ContactViewHolder(
            LayoutInflater.from(parent.context)
                .inflate(R.layout.activity_permission_cell, parent, false)
        )

    override fun getItemCount(): Int = contact.size

    override fun onBindViewHolder(holder: ContactViewHolder, position: Int) {
        holder.contactName.text = contact[position]
    }
}




