package fr.isen.turbatte.androidtoolbox

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import kotlinx.android.synthetic.main.activity_web_user.*

class WebUserActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_web_user)

        val people: User = intent.getSerializableExtra("people") as User

        val titreUser = people.results

    }
}
