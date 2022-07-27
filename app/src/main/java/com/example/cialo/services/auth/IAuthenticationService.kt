package com.example.cialo.services.auth

import android.content.Context

interface IAuthenticationService {
    fun getCurrentUser(): CurrentUser?
    fun setUser(currentUser: CurrentUser)
    fun removeUser()
    fun isLoggedIn(): Boolean
}