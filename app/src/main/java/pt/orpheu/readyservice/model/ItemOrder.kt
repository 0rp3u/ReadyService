package pt.orpheu.readyservice.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class ItemOrder(
    val count: Int,
    val item: Item
) : Parcelable