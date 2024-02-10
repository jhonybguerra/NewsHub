package com.jbgcomposer.newshub.models

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Source(
    val name: String?
) : Parcelable