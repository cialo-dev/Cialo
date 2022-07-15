package com.example.cialo

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.facebook.CallbackManager
import com.facebook.FacebookCallback
import com.facebook.FacebookException
import com.facebook.login.LoginManager
import com.facebook.login.LoginResult
import com.facebook.login.widget.LoginButton
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.SignInButton
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.tasks.Task


class LoginActivity : AppCompatActivity() {

    private lateinit var facebookCallbackManager: CallbackManager
    private lateinit var facebookLoginButton: LoginButton

    private lateinit var googleLoginButton: SignInButton

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        val isLoggedIn = this.isLoggedIn();

        if(isLoggedIn){
            startActivity(Intent(this, MainActivity::class.java))
        }

        this.initFacebook()
        this.initGoogle()
    }

    private fun isLoggedIn(): Boolean {

        val account = GoogleSignIn.getLastSignedInAccount(this)

        if(account != null){
            return true;
        }

        //TODO: Check fb


        return false;
    }


    private fun initGoogle(){
        val gso: GoogleSignInOptions = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestEmail()
            .build()

        val mGoogleSignInClient = GoogleSignIn.getClient(this, gso)

        googleLoginButton = findViewById<SignInButton>(R.id.sign_in_button)
        googleLoginButton.setOnClickListener({
            val signInIntent: Intent = mGoogleSignInClient.getSignInIntent()
            startActivityForResult(signInIntent, 555)
        })
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode === 555) {
            // The Task returned from this call is always completed, no need to attach
            // a listener.
            val task: Task<GoogleSignInAccount> =
                GoogleSignIn.getSignedInAccountFromIntent(data)
            handleSignInResult(task)
        }
    }

    private fun handleSignInResult(completedTask: Task<GoogleSignInAccount>) {
        try {
            val account = completedTask.getResult(ApiException::class.java)

            val wtf = account.email;
            var elo = account.id;

            var e = "asd";
            // Signed in successfully, show authenticated UI.
            //TODO:
        } catch (e: ApiException) {
            // The ApiException status code indicates the detailed failure reason.
            // Please refer to the GoogleSignInStatusCodes class reference for more information.
            Log.w("TAG", "signInResult:failed code=" + e.statusCode)

            //TODO:
        }
    }

    private fun initFacebook(){

        facebookCallbackManager = CallbackManager.Factory.create()
        facebookLoginButton = findViewById<LoginButton>(R.id.login_button);


        LoginManager.getInstance().registerCallback(facebookCallbackManager, object :
            FacebookCallback<LoginResult> {
            override fun onSuccess(loginResult: LoginResult) {
                val d = Log.d("letsSee", "Facebook token: " + loginResult.accessToken.token)

            }

            override fun onCancel() {
                Log.d("letsSee", "Facebook onCancel.")

            }

            override fun onError(error: FacebookException) {
                Log.d("letsSee", "Facebook onError.")

            }
        })
    }
}