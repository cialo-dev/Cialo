package com.example.cialo

import android.app.Application
import android.util.Log
import androidx.room.Room
import com.estimote.proximity_sdk.api.*
import com.example.cialo.services.api.CialoApiClient
import com.example.cialo.services.api.IApiClient
import com.example.cialo.services.api.RegionDto
import com.example.cialo.services.auth.AuthService
import com.example.cialo.services.auth.IAuthenticationService
import com.example.cialo.services.database.DatabaseContext
import com.example.cialo.services.notifications.INotificationService
import com.example.cialo.services.notifications.NotificationService
import com.example.cialo.services.proximity.ProximityContentManager
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin
import org.koin.dsl.module

class CialoApplication : Application() {

    private lateinit var _proximityManager: ProximityContentManager
    private lateinit var _proximityObserver: ProximityObserver
    private lateinit var _proximityTriggerHandler: ProximityTrigger.Handler


    companion object {
        lateinit var instance: CialoApplication private set
        lateinit var databaseContext: DatabaseContext private set
    }

    override fun onCreate() {
        super.onCreate()
        instance = this;

        val appModule = module {
            single<INotificationService> { NotificationService() }
            single<IApiClient> { CialoApiClient() }
            single<IAuthenticationService> { AuthService() }
            single<DatabaseContext> {
                Room.databaseBuilder(
                    applicationContext,
                    DatabaseContext::class.java, "cialodb"
                ).build()
            }
        }
        startKoin {
            androidContext(this@CialoApplication)
            modules(appModule)
        }

        startEstimote()
    }

    fun startEstimote() {

        //if (_proximityManager != null)
        //    return

        _proximityObserver = createObserver()
        _proximityManager = ProximityContentManager(this)
        _proximityManager.start(_proximityObserver, listOf(RegionDto("tag-grunwald", "identifier")))
    }

    private fun createObserver(): ProximityObserver {
        val notificationService = NotificationService()
        val notification = notificationService.createProximityNotification(this)

        /*_proximityTriggerHandler = ProximityTriggerBuilder(this)
            .displayNotificationWhenInProximity(notification)
            .build()
            .start()*/


        return ProximityObserverBuilder(this,
            EstimoteCloudCredentials(getString(R.string.estimoteAppId),
                getString(R.string.estimoteAppToken)))
            .withTelemetryReportingDisabled()
            .withEstimoteSecureMonitoringDisabled()
            .onError { throwable ->
                Log.e("app", "proximity observer error: $throwable")
            }
            .withBalancedPowerMode()
            .withScannerInForegroundService(notificationService.createProximityNotification(this))
            .build()
    }
}