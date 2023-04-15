package com.example.presentation.extensions

import androidx.fragment.app.Fragment

val Fragment.bothAsk: String
    get() = "BOTH"
val Fragment.accessFineLocationAsk: String
    get() = "ACCESS_FINE_LOCATION"
val Fragment.writeExternalStorageAsk: String
    get() = "WRITE_EXTERNAL_STORAGE"