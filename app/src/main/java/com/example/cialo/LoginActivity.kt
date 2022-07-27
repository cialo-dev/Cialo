package com.example.cialo

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.example.cialo.utils.RequestCodes
import com.facebook.*
import com.facebook.login.widget.LoginButton
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.SignInButton
import com.google.android.gms.tasks.Task


class LoginActivity : AppCompatActivity() {

    private lateinit var _facebookCallbackManager: CallbackManager
    private lateinit var _facebookLoginButton: LoginButton
    private lateinit var _googleLoginButton: SignInButton
    private lateinit var _viewModel: LoginViewModel;

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        _viewModel = ViewModelProvider(this).get(LoginViewModel::class.java)

        if (_viewModel.isInitiallyLoggedIn()) {
            goToHome();
            return;
        }

        _viewModel.isLoggedIn.observe(this, Observer {
            if (it) {
                goToHome()
            }
        })
        initFacebook()
        initGoogle()
    }

    private fun goToHome() {
        startActivity(Intent(this, MainActivity::class.java))
        finish()
    }

    private fun initGoogle() {
        val gso: GoogleSignInOptions =
            GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build()

        val mGoogleSignInClient = GoogleSignIn.getClient(this, gso)

        _googleLoginButton = findViewById(R.id.sign_in_button)
        _googleLoginButton.setOnClickListener {
            val signInIntent: Intent = mGoogleSignInClient.signInIntent
            startActivityForResult(signInIntent, RequestCodes.googleRequestCode)
        }
    }

    @Deprecated("Deprecated in Java")
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode != RequestCodes.googleRequestCode) {
            return;
        }

        val task: Task<GoogleSignInAccount> = GoogleSignIn.getSignedInAccountFromIntent(data)
        _viewModel.loginWithGoogle(task);
    }

    private fun initFacebook() {

        _facebookCallbackManager = CallbackManager.Factory.create()
        _facebookLoginButton = findViewById(R.id.login_button);
        _facebookLoginButton.setPermissions(listOf("email", "public_profile", "user_birthday"))

        AccessToken.getCurrentAccessToken()
    }
}