package fr.isen.turbatte.androidtoolbox

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
class User() : Parcelable {
    val results: ArrayList<Results> = ArrayList()
}
