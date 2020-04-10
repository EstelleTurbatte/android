package fr.isen.turbatte.androidtoolbox

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.activity_web_user.*

class WebUserActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_web_user)

        val people: Results = intent.getParcelableExtra("People")

        nomTitreTextView.text = people.name.title
        nomFirstTextView.text = people.name.first
        nomLastTextView.text = people.name.last
        val street: String =
            people.location.street.number.toString() + " " + people.location.street.name
        locationStreetTextView.text = street
        locationCityTextView.text = people.location.city
        locationStateTextView.text = people.location.state
        locationPostCodeTextView.text = people.location.postcode.toString()
        eMailTextView.text = people.email
        val dob = "Date de Naissance : " + people.dob.date
        textView3.text = dob
        val phone = "Numéros de téléphone fixe : " + people.phone
        phoneTextView.text = phone
        val cell = "Numéros de téléphone portable : " + people.cell
        cellTextView.text = cell
        val age = "Age : " + people.dob.age + "ans"
        ageTextView.text = age


        Picasso.get()
            .load(people.picture.large)
            .fit().centerInside()
            .into(pictureImageView)


    }


}
