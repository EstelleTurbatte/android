package fr.isen.turbatte.androidtoolbox

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Street(
    val name: String,
    val number: Int
) : Parcelable