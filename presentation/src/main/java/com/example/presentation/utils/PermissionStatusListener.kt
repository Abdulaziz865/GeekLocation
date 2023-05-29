package com.example.presentation.utils

import android.app.Activity
import android.content.Context
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.content.PermissionChecker
import androidx.lifecycle.LiveData

class PermissionStatusListener(
    private val context: Context, private val permissionToListen: String
) : LiveData<PermissionStatus>() {

    override fun onActive() = handlePermissionCheck()

    private fun handlePermissionCheck() {
        val isPermissionGranted = ContextCompat.checkSelfPermission(context, permissionToListen) == PermissionChecker.PERMISSION_GRANTED

        if (isPermissionGranted) {
            postValue(PermissionStatus.Granted())
        }
        else {
            if (ActivityCompat.shouldShowRequestPermissionRationale(context as Activity, permissionToListen)) {
                postValue(PermissionStatus.Denied())
            }
            else {
                postValue(PermissionStatus.Blocked())
            }
        }
    }
}