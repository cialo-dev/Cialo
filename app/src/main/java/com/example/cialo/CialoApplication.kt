package com.example.cialo

import android.app.Application
import android.util.Log
import androidx.room.Room
import com.estimote.proximity_sdk.api.*
import com.example.cialo.services.api.CialoApiClient
import com.example.cialo.services.api.IApiClient
import com.example.cialo.services.auth.AuthService
import com.example.cialo.services.auth.IAuthenticationService
import com.example.cialo.services.database.DatabaseContext
import com.example.cialo.services.eventsscheduler.EventsSenderRunner
import com.example.cialo.services.eventsscheduler.IEventSenderRunner
import com.example.cialo.services.logging.DebugLogger
import com.example.cialo.services.logging.ILog
import com.example.cialo.services.notifications.INotificationService
import com.example.cialo.services.notifications.NotificationService
import com.example.cialo.services.proximity.IProximityRunner
import com.example.cialo.services.proximity.ProximityContentManager
import com.example.cialo.services.proximity.ProximityRunner
import com.microsoft.appcenter.AppCenter
import com.microsoft.appcenter.analytics.Analytics
import com.microsoft.appcenter.crashes.Crashes
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin
import org.koin.dsl.module


class CialoApplication : Application() {

    private lateinit var _proximityManager: ProximityContentManager
    private lateinit var _proximityObserver: ProximityObserver
    private lateinit var _proximityRunner: IProximityRunner;
    private lateinit var _eventSenderManager: IEventSenderRunner;

    companion object {
        lateinit var instance: CialoApplication private set
    }

    override fun onCreate() {
        super.onCreate()
        instance = this;

        initDependencies()
        initAppCenter()
        initEstimote()
    }

    private fun initAppCenter() {
        //TODO: Store varaible
        AppCenter.start(this, "8005083f-8947-4619-9061-41f6fef51a0c",
            Analytics::class.java, Crashes::class.java)
    }

    private fun initEstimote() {
        _proximityObserver = createObserver()
        _proximityManager = ProximityContentManager()
        _proximityRunner = ProximityRunner(_proximityObserver, _proximityManager);
    }

    private fun createObserver(): ProximityObserver {
        val notificationService = NotificationService()

        return ProximityObserverBuilder(this,
            EstimoteCloudCredentials(
                getString(R.string.estimoteAppId),
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

    private fun initDependencies() {

        _eventSenderManager = EventsSenderRunner(this, NotificationService())

        val appModule = module {
            single<INotificationService> { NotificationService() }
            single<IApiClient> { CialoApiClient() }
            single<IAuthenticationService> { AuthService() }
            single<IProximityRunner> { _proximityRunner }
            single<IEventSenderRunner> { _eventSenderManager }
            //TODO:
            single<ILog> { DebugLogger() }
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
    }
}