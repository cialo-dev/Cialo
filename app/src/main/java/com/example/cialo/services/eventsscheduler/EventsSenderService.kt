package com.example.cialo.services.eventsscheduler

import android.app.Service
import android.content.Intent
import android.os.IBinder
import android.util.Log
import com.example.cialo.CialoApplication
import com.example.cialo.services.api.BeaconEventApiModel
import com.example.cialo.services.api.BeaconEventsApiModel
import com.example.cialo.services.api.IApiClient
import com.example.cialo.services.auth.IAuthenticationService
import com.example.cialo.services.database.DatabaseContext
import com.example.cialo.utils.DateTimeParser
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import org.koin.java.KoinJavaComponent

class EventsSenderService : Service() {

    private val _databaseContext: DatabaseContext by KoinJavaComponent.inject(DatabaseContext::class.java)
    private val _apiClient: IApiClient by KoinJavaComponent.inject(IApiClient::class.java)
    private val _authService: IAuthenticationService by KoinJavaComponent.inject(
        IAuthenticationService::class.java)

    @OptIn(DelicateCoroutinesApi::class)
    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        super.onStartCommand(intent, flags, startId)

        Log.i("BackgroundService", "I run")

        GlobalScope.launch(Dispatchers.IO) {
            val events = _databaseContext.regionEventsDao().getAll();

            if (!events.any()) {
                Log.e("BackgroundService", "No events to send")
            } else {
                val eventsData =
                    events.map {
                        BeaconEventApiModel("Test",
                            DateTimeParser.toApiString(it.dateTimeTicks),
                            it.eventType.value)
                    }.toList()

                val apiModel = BeaconEventsApiModel(eventsData);
                val result =
                    _apiClient.sendBeaconEvents(_authService.getCurrentUser()!!.id, apiModel);

                if (!result.isSuccess) {
                    Log.e("BackgroundService", result.message!!);
                } else {
                    _databaseContext.regionEventsDao().removeAll();
                    Log.i("BackgroundService", "events sent")
                }
            }
        }

        return START_NOT_STICKY;
    }

    override fun onTaskRemoved(rootIntent: Intent?) {
        super.onTaskRemoved(rootIntent)



        //CialoApplication.instance.stopEventsScheduler();
    }

    override fun onBind(p0: Intent?): IBinder? {
        return null;
    }
}