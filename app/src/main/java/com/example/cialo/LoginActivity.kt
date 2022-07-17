package com.example.cialo

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.example.cialo.services.auth.CurrentUser
import com.example.cialo.services.auth.IAuthenticationService
import com.facebook.*
import com.facebook.login.LoginManager
import com.facebook.login.LoginResult
import com.facebook.login.widget.LoginButton
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.SignInButton
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.tasks.Task
import org.koin.java.KoinJavaComponent


//TODO: Call finish() after move to MainActivity
class LoginActivity : AppCompatActivity() {

    private val googleRequestCode = 555;

    private lateinit var _facebookCallbackManager: CallbackManager
    private lateinit var _facebookLoginButton: LoginButton
    private lateinit var _googleLoginButton: SignInButton
    private val _authService: IAuthenticationService by KoinJavaComponent.inject(
        IAuthenticationService::class.java)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        this.init()
    }

    private fun init() {
        if (_authService.isLoggedIn(this)) {
            this.goToHome()
            return
        }

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

        _googleLoginButton = findViewById<SignInButton>(R.id.sign_in_button)
        _googleLoginButton.setOnClickListener({
            val signInIntent: Intent = mGoogleSignInClient.getSignInIntent()
            startActivityForResult(signInIntent, googleRequestCode)
        })
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode === googleRequestCode) {
            val task: Task<GoogleSignInAccount> =
                GoogleSignIn.getSignedInAccountFromIntent(data)
            handleGoogleResult(task)
        }
    }

    private fun handleGoogleResult(completedTask: Task<GoogleSignInAccount>) {
        try {
            val account = completedTask.getResult(ApiException::class.java)

            if (account.id == null) {
                /*TODO HANDLE*/
                return;
            }

            this._authService.setUser(this,
                CurrentUser(account.id!!, account.email, account.givenName, account.familyName))
            this.goToHome()

        } catch (e: ApiException) {
            // The ApiException status code indicates the detailed failure reason.
            // Please refer to the GoogleSignInStatusCodes class reference for more information.
            Log.w("TAG", "signInResult:failed code=" + e.statusCode)
            //TODO:
        }
    }

    private fun initFacebook() {

        _facebookCallbackManager = CallbackManager.Factory.create()
        _facebookLoginButton = findViewById(R.id.login_button);
        _facebookLoginButton.setPermissions(listOf("email", "public_profile", "user_birthday"))

        AccessToken.getCurrentAccessToken()

        LoginManager.getInstance().registerCallback(_facebookCallbackManager, object :
            FacebookCallback<LoginResult> {
            override fun onSuccess(loginResult: LoginResult) {
                val d = Log.d("letsSee", "Facebook token: " + loginResult.accessToken.token)

                loginResult.accessToken.userId;

            }

            override fun onCancel() {
                Log.d("letsSee", "Facebook onCancel.")
            }

            override fun onError(error: FacebookException) {
                Log.d("letsSee", "Facebook onError.")
            }
        })
    }

    private fun getUserDataFromFacebook(loginResult: LoginResult) {
        val request = GraphRequest.newMeRequest(
            loginResult.accessToken
        ) { obj, response ->
            Log.v("LoginActivity", response.toString())

            // Application code
            val email = obj!!.getString("email")
            val birthday = obj!!.getString("birthday") // 01/31/1980 format
        }
        val parameters = Bundle()
        parameters.putString("fields", "id,name,email,gender,birthday")
        request.parameters = parameters
        request.executeAsync()
    }
}