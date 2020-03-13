package fr.isen.turbatte.androidtoolbox

import android.Manifest
import android.app.Activity
import android.app.Activity.RESULT_OK
import android.app.AlertDialog
import android.content.Intent
import android.content.pm.PackageManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.ContactsContract
import android.provider.MediaStore
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.ListAdapter
import androidx.core.app.ActivityCompat
import androidx.recyclerview.widget.LinearLayoutManager
import kotlinx.android.synthetic.main.activity_home.*
import kotlinx.android.synthetic.main.activity_permission.*

class PermissionActivity : AppCompatActivity() {

    val contacts = mutableListOf<String>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_permission)

        /*var texteentree:String = "Bonjour "
        val login:String = intent.getStringExtra("login")
        texteentree += login
        accesTextView2.text =texteentree*/


        photoUtilisateurView.setOnClickListener{
            /*selectImageInAlbum()
            takePhoto()*/
            val intent = Intent(Intent.ACTION_GET_CONTENT)
            intent.type = "image/*"

            val pictureDialog = AlertDialog.Builder(this)
            pictureDialog.setTitle("Select Action")
            val pictureDialogItems = arrayOf("Select photo from gallery", "Capture photo from camera")
            pictureDialog.setItems(pictureDialogItems
            ) { dialog, which ->
                when (which) {
                    0 -> startActivityForResult(intent, REQUEST_SELECT_IMAGE_IN_ALBUM)
                    1 -> startActivityForResult(intent, REQUEST_TAKE_PHOTO)
                }
            }
            pictureDialog.show()
        }

        /*contactRecycler.adapter = ContactAdapter(listOf("Julette", "Melvin"))
        contactRecycler.layoutManager = LinearLayoutManager(this)*/

        if(ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_CONTACTS) != PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.READ_CONTACTS), REQUEST_PERMISSION)
            }else{
            getContact()
            contactRecycler.adapter = ContactAdapter(contacts.sorted())
            contactRecycler.layoutManager = LinearLayoutManager(this)
        }

    }

    private fun selectImageInAlbum() {
        val intent = Intent(Intent.ACTION_GET_CONTENT)
        intent.type = "image/*"
        startActivityForResult(intent, REQUEST_SELECT_IMAGE_IN_ALBUM)
    }
    private fun takePhoto() {
        val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        startActivityForResult(intent, REQUEST_TAKE_PHOTO)
    }


    companion object {
        private const val REQUEST_TAKE_PHOTO = 0
        private const val REQUEST_SELECT_IMAGE_IN_ALBUM = 1
        private const val REQUEST_PERMISSION = 123
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (resultCode == Activity.RESULT_OK && requestCode == REQUEST_SELECT_IMAGE_IN_ALBUM){
            photoUtilisateurView.setImageURI(data?.data) // handle chosen image
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if(requestCode == REQUEST_PERMISSION)getContact()
    }

    private fun getContact() {
        val adapter = ContactAdapter(getContactData())
        contactRecycler.adapter = adapter
    }

    private fun getContactData(): ArrayList<String> {
        val contactList = ArrayList<String>()
        val contactCursor = contentResolver.query(ContactsContract.Contacts.CONTENT_URI,null, null, null, null)

        if(contactCursor != null && contactCursor.count > 0){
            while (contactCursor.moveToNext()){
                val name = contactCursor.getString(contactCursor.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME))
                contacts.add(" Nom : $name")
            }
            contactCursor.close()
        }
    return contactList
    }


}



