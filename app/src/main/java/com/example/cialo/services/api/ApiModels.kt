package com.example.cialo.services.api

import com.example.cialo.models.AuthenticationProvider

data class LoginApiModel(
    val provider: AuthenticationProvider,
    val providerId: String,
    val email: String?,
    val firstName: String?,
    val lastName: String?,
);

