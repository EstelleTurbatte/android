package fr.isen.turbatte.androidtoolbox

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
class Results(
    val email: String,
    val location: Location,
    val name: Name,
    val picture: Picture,
    val dob: Dob,
    val phone: String,
    val cell: String
) : Parcelable

