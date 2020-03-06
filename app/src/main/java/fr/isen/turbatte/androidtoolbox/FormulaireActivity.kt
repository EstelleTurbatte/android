package fr.isen.turbatte.androidtoolbox

import android.app.DatePickerDialog
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.DatePicker
import kotlinx.android.synthetic.main.activity_formulaire.*
import org.json.JSONObject
import java.util.*

class FormulaireActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_formulaire)

        val userNom = nom.text.toString()
        val userPrenom = prenom.text.toString()
        //val userDate = dateUtilisateur.text.to

        /*val jsonobject = JSONObject()

        jsonobject.put("nom", userNom)
        jsonobject.put("prénom", userPrenom)*/

        val c = Calendar.getInstance()
        val annee = c.get(Calendar.YEAR)
        val mois = c.get(Calendar.MONTH)
        val jour = c.get(Calendar.DAY_OF_MONTH)

        pickDateBtn.setOnClickListener {
            val dpd = DatePickerDialog(this, DatePickerDialog.OnDateSetListener {view: DatePicker?, year: Int, month: Int, dayOfMonth: Int ->  dateUser.setText(""+ dayOfMonth +"/"+ month +"/"+ year )}, annee, mois, jour)
            dpd.show()
        }
    }
}
