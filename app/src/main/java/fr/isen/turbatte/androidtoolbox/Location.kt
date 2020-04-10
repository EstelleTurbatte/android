package fr.isen.turbatte.androidtoolbox

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Location(
    val street: Street,
    val city: String,
    val state: String,
    val postcode: Int
) : Parcelable