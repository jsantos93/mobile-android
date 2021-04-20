package com.example.mobileav01

import android.net.Uri
import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Fruit(var imageResource: Uri?, var fruitName: String, var fruitBenefits: String):Parcelable