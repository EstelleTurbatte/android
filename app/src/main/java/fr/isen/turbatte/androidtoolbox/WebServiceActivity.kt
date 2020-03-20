package fr.isen.turbatte.androidtoolbox

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import kotlinx.android.synthetic.main.activity_permission.*
import kotlinx.android.synthetic.main.activity_web_service.*

class WebServiceActivity : AppCompatActivity() {

    private val url = "https://randomuser.me/api/?inc=name%2Cpicture%2Clocation%2Cemail&noinfo=&nat=fr&format=pretty&results=10"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_web_service)

        /*contactRecycler.adapter = ContactAdapter(listOf("Juliette", "Melvin"))
        contactRecycler.layoutManager = LinearLayoutManager(this)*/
        getUserFromApi()
    }

    private fun getUserFromApi(): User {

        val queue = Volley.newRequestQueue(this)
        var jsonString = User()

        val stringRequest = StringRequest(
            Request.Method.GET, url,
            Response.Listener<String> { response ->
                val gson = GsonBuilder().create()
                jsonString = gson.fromJson(response.toString(), User::class.java)
                webRecycler.adapter = WebAdapter(jsonString, this)
                webRecycler.layoutManager = LinearLayoutManager(this)
                webRecycler.visibility = View.VISIBLE

            },
            Response.ErrorListener { Toast.makeText(this, "That didn't work", Toast.LENGTH_SHORT).show()})

        queue.add(stringRequest)
        return jsonString

    }
}
