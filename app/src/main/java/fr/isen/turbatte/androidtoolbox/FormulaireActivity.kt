package fr.isen.turbatte.androidtoolbox

import android.app.AlertDialog
import android.app.DatePickerDialog
import android.os.Bundle
import android.widget.DatePicker
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_formulaire.*
import org.json.JSONObject
import java.io.File
import java.text.SimpleDateFormat
import java.util.*


class FormulaireActivity : AppCompatActivity() {

    val KEY_LOGIN = "NOM"
    val KEY_PASSWORD = "pass"
    val KEY_DATE = "date"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_formulaire)

        val cal: Calendar = Calendar.getInstance()
        val dateSetListener =
            DatePickerDialog.OnDateSetListener { datePicker: DatePicker, year: Int, monthOfYear: Int, dayOfMonth: Int ->
                cal.set(Calendar.YEAR, year)
                cal.set(Calendar.MONTH, monthOfYear)
                cal.set(Calendar.DAY_OF_MONTH, dayOfMonth)

                val sdf = SimpleDateFormat("dd/MM/yyyy", Locale.FRANCE)
                dateUser.text = sdf.format(cal.time)

            }

        pickDateBtn.setOnClickListener {
            showDatePicker(dateSetListener)
        }

        envoyer_button.setOnClickListener {
            val userNom = nom.text.toString()
            val userPrenom = prenom.text.toString()
            val userDate = dateUser.text.toString()
            val answer = JSONObject()
            
            val path = filesDir.absolutePath + "fichier.json"

            answer.put(KEY_LOGIN, userNom)
            answer.put(KEY_PASSWORD, userPrenom)
            answer.put(KEY_DATE, userDate)

            val json = answer.toString()

            File(path).writeText(json)

            showButton.setOnClickListener {
                val builder = AlertDialog.Builder(this@FormulaireActivity)
                val jsonString: String = File(path).readText(Charsets.UTF_8)
                val test = JSONObject(jsonString)
                val age = getAge(cal)

                builder.setTitle("Personne enregistrée")
                builder.setMessage(
                    "Nom : " + test.get(KEY_LOGIN).toString() + "\nPrénom : " + test.get(KEY_PASSWORD).toString() + "\nDate de Naissance : "
                            + test.get(KEY_DATE).toString()+ "\nAge : " +age +" ans")
                val dialog: AlertDialog = builder.create()
                dialog.show()
            }
        }
    }

    private fun showDatePicker(dateSetListener: DatePickerDialog.OnDateSetListener) {
        val cal = Calendar.getInstance()
        DatePickerDialog(
            this@FormulaireActivity, dateSetListener,
            cal.get(Calendar.YEAR),
            cal.get(Calendar.MONTH),
            cal.get(Calendar.DAY_OF_MONTH)
        ).show()
    }


    private fun getAge(dob: Calendar): Int? {
        //val dob = Calendar.getInstance()
        val today = Calendar.getInstance()

        val year = dob.get(Calendar.YEAR)
        val month = dob.get(Calendar.MONTH)
        val day = dob.get(Calendar.DAY_OF_MONTH)

        dob[year!!, month] = day
        var age = today[Calendar.YEAR] - dob[Calendar.YEAR]
        if (month === today[Calendar.MONTH] + 1 && day > today[Calendar.DAY_OF_MONTH]) {
            age--
        }
        return age
    }

}

