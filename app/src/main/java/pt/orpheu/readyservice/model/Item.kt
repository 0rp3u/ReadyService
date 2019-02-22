package pt.orpheu.readyservice.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize


@Parcelize
data class Item(
        val id: Long,
        val name: String,
        val description: String,
        val img_urls: List<String>,
        val price: Double
): Parcelable