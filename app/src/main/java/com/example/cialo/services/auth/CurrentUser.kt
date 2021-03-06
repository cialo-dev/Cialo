package com.example.cialo.services.auth

import com.example.cialo.models.AuthenticationProvider

class CurrentUser(
    val id: String,
    val provider: AuthenticationProvider,
    val email: String?,
    val firstName: String?,
    val secondName: String?,
)