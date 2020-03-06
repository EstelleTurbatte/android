package fr.isen.turbatte.androidtoolbox

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import kotlinx.android.synthetic.main.activity_cycle_vie.*
import kotlinx.android.synthetic.main.activity_home.*

class CycleVieActivity : AppCompatActivity() {

    private var texteglobal:String = ""
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_cycle_vie)

        texteglobal += "application -> onCreate()\n"
        etatText.text = texteglobal

    }

    override fun onStart() {
        super.onStart()
        texteglobal += "application -> onStart()\n"
        etatText.text = texteglobal
    }

   override fun onResume() {
        super.onResume()
        texteglobal += "application -> onResume()\n"
        etatText.text = texteglobal
    }

    override fun onDestroy() {
        super.onDestroy()
        Log.d("", "application -> onDestroy()")
    }
}
