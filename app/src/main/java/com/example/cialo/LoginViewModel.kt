package com.example.cialo

import android.os.Bundle
import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.cialo.models.AuthenticationProvider
import com.example.cialo.services.api.IApiClient
import com.example.cialo.services.api.RetrofitApiClient
import com.example.cialo.services.api.LoginApiModel
import com.example.cialo.services.auth.CurrentUser
import com.example.cialo.services.auth.IAuthenticationService
import com.facebook.CallbackManager
import com.facebook.FacebookCallback
import com.facebook.FacebookException
import com.facebook.GraphRequest
import com.facebook.login.LoginManager
import com.facebook.login.LoginResult
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.tasks.Task
import kotlinx.coroutines.*
import org.koin.java.KoinJavaComponent

class LoginViewModel : ViewModel() {
    private val _apiClient: IApiClient by KoinJavaComponent.inject(IApiClient::class.java)
    private val _authService: IAuthenticationService by KoinJavaComponent.inject(
        IAuthenticationService::class.java)
    private val _facebookCallbackManager = CallbackManager.Factory.create();
    private val exceptionHandler = CoroutineExceptionHandler { _, throwable ->
        onError("Exception handled: ${throwable.localizedMessage}")
    }
    private var _loginApiJob: Job? = null;

    var isLoggedIn: MutableLiveData<Boolean> = MutableLiveData(false);
    var errorState: MutableLiveData<String> = MutableLiveData(null);


    init {
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

    fun isInitiallyLoggedIn() : Boolean{
        return _authService.isLoggedIn();
    }

    fun loginWithGoogle(data: Task<GoogleSignInAccount>) {
        try {
            val account = data.getResult(ApiException::class.java)

            if (account.id == null) {
                //TODO:
                return;
            }

            this.login(CurrentUser(
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
        } catch (e: Exception) {
            //TODO:
        }

    }

    private fun onError(message: String) {

    }

    @OptIn(DelicateCoroutinesApi::class)
    private fun login(user: CurrentUser) {
        _loginApiJob = GlobalScope.launch(Dispatchers.IO + exceptionHandler) {
            val result = _apiClient.login(LoginApiModel(
                user.provider.value,
                user.id,
                user.email,
                user.firstName,
                user.secondName
            ));

            withContext(Dispatchers.Main) {
                if (result.isSuccess) {
                    _authService.setUser(user)
                    isLoggedIn.value = true;
                } else {
                    errorState = MutableLiveData(result.message);
                };
            }
        }
    }

    override fun onCleared() {
        super.onCleared()
        _loginApiJob?.cancel()
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