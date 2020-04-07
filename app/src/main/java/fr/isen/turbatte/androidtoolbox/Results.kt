package fr.isen.turbatte.androidtoolbox

import android.os.Parcelable
import android.provider.ContactsContract
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Results(
    val email: String,
    val location: Location,
    val name: Name,
    val picture: Picture
) : Parcelable

