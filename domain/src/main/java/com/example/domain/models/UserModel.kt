package com.example.domain.models

data class UserModel(
    val name: String? = "",
    val email: String? = "",
    val latitude: Double? = 0.0,
    val longitude: Double? = 0.0
)