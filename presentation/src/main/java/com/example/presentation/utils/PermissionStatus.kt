package com.example.presentation.utils

import com.example.presentation.R

sealed class PermissionStatus {
    data class Granted(val message: Int = R.string.permission_status_granted) : PermissionStatus()
    data class Denied(val message: Int = R.string.permission_status_denied) : PermissionStatus()
    data class Blocked(val message: Int = R.string.permission_status_blocked) : PermissionStatus()
}