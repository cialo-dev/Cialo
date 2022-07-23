package com.example.cialo.services.auth

import android.content.Context
import android.content.Context.MODE_PRIVATE
import com.facebook.AccessToken
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.gson.Gson


class AuthService : IAuthenticationService {

    private val _thisSharedPreferencesKey = "cialo_auth"
    private val _currentUserKey = "current_user";

    override fun getCurrentUser(context: Context): CurrentUser? {
        val currentUserJson = context.getSharedPreferences(_thisSharedPreferencesKey, MODE_PRIVATE)
            .getString(_currentUserKey, null) ?: return null

        return try {
            Gson().fromJson(currentUserJson, CurrentUser::class.java)
        } catch (ex: Exception) {
            null
        }
    }

    override fun setUser(context: Context, currentUser: CurrentUser) {
        context.getSharedPreferences(_thisSharedPreferencesKey, MODE_PRIVATE).edit()
            .putString(_currentUserKey, Gson().toJson(currentUser))
    }

    override fun removeUser(context: Context) {
        context.getSharedPreferences(_thisSharedPreferencesKey, MODE_PRIVATE).edit()
            .remove(_currentUserKey);
    }

    override fun isLoggedIn(context: Context): Boolean {
        this.getCurrentUser(context) ?: return false

        if (isLoggedInWithFacebook())
            return true

        if (isLoggedInWithGoogle(context))
            return true

        return false
    }

    private fun isLoggedInWithFacebook(): Boolean {
        val accessToken = AccessToken.getCurrentAccessToken()

        return accessToken != null && !accessToken.isExpired;
    }

    private fun isLoggedInWithGoogle(context: Context): Boolean {
        val user = GoogleSignIn.getLastSignedInAccount(context)

        return user != null && !user.isExpired
    }
}