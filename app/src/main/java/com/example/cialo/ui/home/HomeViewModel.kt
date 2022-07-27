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
    private val _databaseContext: DatabaseContext by KoinJavaComponent.inject(DatabaseContext::class.java)
    private val _apiClient: IApiClient by KoinJavaComponent.inject(IApiClient::class.java)
    private val _authService: IAuthenticationService by KoinJavaComponent.inject(
        IAuthenticationService::class.java)

    private val _text = MutableLiveData<String>().apply {
        value = "This is home Fragment"
    }
    val text: LiveData<String> = _text

    @OptIn(DelicateCoroutinesApi::class)
    fun cleanDatabase() {
        GlobalScope.launch(Dispatchers.IO) {
            val events = _databaseContext.regionEventsDao().getAll();

            val eventsData =
                events.map {
                    BeaconEventApiModel("Test",
                        getDateTime(it.dateTimeTicks),
                        it.eventType.value)
                }
                    .toList()

            val apiModel = BeaconEventsApiModel(eventsData);

            val result = _apiClient.sendBeaconEvents(_authService.getCurrentUser()!!.id, apiModel);

            val a = result.isSuccess;
        }
    }

    private fun getDateTime(s: Long): String {

        val sdf: DateFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS")
        sdf.timeZone = TimeZone.getTimeZone("UTC")
        val calendar = Calendar.getInstance()
        calendar.timeInMillis = s
        val tz = TimeZone.getDefault()
        sdf.timeZone = tz
        return sdf.format(calendar.time)
    }
}