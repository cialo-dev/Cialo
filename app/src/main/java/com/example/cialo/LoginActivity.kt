package com.example.cialo

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.example.cialo.models.AuthenticationProvider
import com.example.cialo.services.api.IApiClient
import com.example.cialo.services.api.LoginApiModel
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
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.koin.java.KoinJavaComponent


class LoginActivity : AppCompatActivity() {

    private val googleRequestCode = 555;

    private lateinit var _facebookCallbackManager: CallbackManager
    private lateinit var _facebookLoginButton: LoginButton
    private lateinit var _googleLoginButton: SignInButton

    private val _apiClient: IApiClient by KoinJavaComponent.inject(
        IApiClient::class.java)

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

    private fun handleLogin(user: CurrentUser) {

        GlobalScope.launch(Dispatchers.IO) {
            val result = _apiClient.login(LoginApiModel(
                user.provider,
                user.id,
                user.email,
                user.firstName,
                user.secondName
            ));
            if (result)
                _authService.setUser(CialoApplication.instance, user);

        }
        goToHome();
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
        _googleLoginButton.setOnClickListener({
            val signInIntent: Intent = mGoogleSignInClient.getSignInIntent()
            startActivityForResult(signInIntent, googleRequestCode)
        })
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == googleRequestCode) {
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

            this.handleLogin(CurrentUser(
                account.id!!,
                AuthenticationProvider.Google,
                account.email,
                account.givenName,
                account.familyName
            ))

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

                getUserDataFromFacebook(loginResult)
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

            //TODO
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