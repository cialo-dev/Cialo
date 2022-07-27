package com.example.cialo.services.auth

import android.content.Context
import android.content.Context.MODE_PRIVATE
import com.example.cialo.CialoApplication
import com.facebook.AccessToken
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.gson.Gson


class AuthService : IAuthenticationService {

    private val _thisSharedPreferencesKey = "cialo_auth"
    private val _currentUserKey = "current_user";
    private val context = CialoApplication.instance;

    override fun getCurrentUser(): CurrentUser? {
        val preferences = context.getSharedPreferences(_thisSharedPreferencesKey, MODE_PRIVATE);
        val currentUserJson = preferences.getString(_currentUserKey, "") ?: return null

        return try {
            Gson().fromJson(currentUserJson, CurrentUser::class.java)
        } catch (ex: Exception) {
            null
        }
    }

    override fun setUser(currentUser: CurrentUser) {
        val preferences = context.getSharedPreferences(_thisSharedPreferencesKey, MODE_PRIVATE);
        val editor = preferences.edit();

        editor.putString(_currentUserKey, Gson().toJson(currentUser))
        editor.commit()
    }

    override fun removeUser() {
        context.getSharedPreferences(_thisSharedPreferencesKey, MODE_PRIVATE).edit()
            .remove(_currentUserKey);
    }

    override fun isLoggedIn(): Boolean {
        this.getCurrentUser() ?: return false

        if (isLoggedInWithFacebook())
            return true

        if (isLoggedInWithGoogle(context))
            return true

        return false
    }

    private fun isLoggedInWithFacebook(): Boolean {
        val accessToken = AccessToken.getCurrentAccessToken()

        return accessToken != null //&& !accessToken.isExpired;
    }

    private fun isLoggedInWithGoogle(context: Context): Boolean {
        val user = GoogleSignIn.getLastSignedInAccount(context)

        return user != null //&& !user.isExpired
    }
}