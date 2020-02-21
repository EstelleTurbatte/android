package fr.isen.turbatte.androidtoolbox

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import com.google.android.material.textfield.TextInputLayout
import kotlinx.android.synthetic.main.activity_login.*
import kotlinx.android.synthetic.main.activity_login.view.*

class LoginActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        val login:String = "estelle"
        val mot_passe:String = "estelle"



        valider_button.setOnClickListener {
            //Toast.makeText(this, "Identification : ${identifiant.text.toString()}", Toast.LENGTH_LONG).show()
            val userlogin = identifiant.text.toString()
            val userpassword = password.text.toString()
            if (userlogin == login && userpassword == mot_passe)
            {
                Toast.makeText(this, "Identification réussie", Toast.LENGTH_LONG).show()
            }else{
                Toast.makeText(this, "Accès refusé", Toast.LENGTH_LONG).show()
            }

        }

    }
}
