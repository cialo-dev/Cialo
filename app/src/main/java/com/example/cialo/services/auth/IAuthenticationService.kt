package com.example.cialo.services.auth

import android.content.Context

interface IAuthenticationService {
    fun getCurrentUser(context: Context): CurrentUser?
    fun setUser(context: Context, currentUser: CurrentUser)
    fun isLoggedIn(context: Context): Boolean
}