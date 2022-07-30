package com.example.cialo.ui.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.cialo.services.api.BeaconEventApiModel
import com.example.cialo.services.api.BeaconEventsApiModel
import com.example.cialo.services.api.IApiClient
import com.example.cialo.services.auth.IAuthenticationService
import com.example.cialo.services.database.DatabaseContext
import com.example.cialo.ui.abstraction.CialoViewModel
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import org.koin.java.KoinJavaComponent
import java.text.DateFormat
import java.text.SimpleDateFormat
import java.util.*

class HomeViewModel : CialoViewModel() {

    private val _text = MutableLiveData<String>().apply {
        value = "This is home Fragment"
    }
}