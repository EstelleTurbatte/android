package fr.isen.turbatte.androidtoolbox


import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.activity_web_service_cell.view.*
import kotlinx.android.synthetic.main.activity_web_user.view.*

class WebAdapter(
    private val randomUser: User,
    val context: Context,
    val peopleClickListener: (User) -> Unit
) : RecyclerView.Adapter<WebAdapter.WebViewHolder>() {

    class WebViewHolder(
        webView: View,
        private val randomUser: User,
        val context: Context,
        val peopleClickListener: (User) -> Unit
    ):
        RecyclerView.ViewHolder(webView){

        private val nameUser: TextView = webView.nameUser
        private val userAdress: TextView = webView.userAddress
        private val userMailAdres: TextView = webView.userMailAdress
        private val picture: ImageView = webView.userImageView
        private val layout = webView.userLayout

        fun pushInfo(position: Int){
            val nomUser = randomUser.results[position].name.first + "" + randomUser.results[position].name.last
            val adresse =randomUser.results[position].location.street.number.toString() + " " + randomUser.results[position].location.street.name + "" + randomUser.results[position].location.city + "" + randomUser.results[position].location.state
            val email = randomUser.results[position].email

            Picasso.get()
                .load(randomUser.results[position].picture.medium)
                .fit().centerInside()
                .into(picture)

            nameUser.text = nomUser
            userAdress.text = adresse
            userMailAdres.text = email


            layout.setOnClickListener {
                peopleClickListener.invoke(randomUser)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): WebViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val view: View = inflater.inflate(R.layout.activity_web_service_cell, parent, false)
        return WebViewHolder(view, randomUser, context, peopleClickListener)
    }

    override fun getItemCount(): Int = randomUser.results.size

    override fun onBindViewHolder(holder: WebViewHolder, position: Int) {
        holder.pushInfo(position)
    }

}


