package fr.isen.turbatte.androidtoolbox

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import kotlinx.android.synthetic.main.activity_home.*

class HomeActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)

        var texteentree:String = "Bonjour "
        val login:String = intent.getStringExtra("login")
        texteentree += login
        bonjour.text = texteentree


        cycle_de_vie_button.setOnClickListener {
            val intent = Intent(this, CycleVieActivity::class.java)
            startActivity(intent)
        }

        deconnexion_button.setOnClickListener{
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
        }

        sauvegarde_button.setOnClickListener{
            val intent = Intent(this, FormulaireActivity::class.java)
            startActivity(intent)
        }

        permission_button.setOnClickListener{
            val intent = Intent(this, PermissionActivity::class.java)
            startActivity(intent)
        }
    }
}
