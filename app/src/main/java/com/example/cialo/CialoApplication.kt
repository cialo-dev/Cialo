package com.example.cialo

import android.app.AlarmManager
import android.app.Application
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.SystemClock
import android.util.Log
import androidx.room.Room
import com.estimote.proximity_sdk.api.*
import com.example.cialo.services.api.CialoApiClient
import com.example.cialo.services.api.IApiClient
import com.example.cialo.services.api.RegionDto
import com.example.cialo.services.auth.AuthService
import com.example.cialo.services.auth.IAuthenticationService
import com.example.cialo.services.database.DatabaseContext
import com.example.cialo.services.eventsscheduler.EventsSenderService
import com.example.cialo.services.notifications.INotificationService
import com.example.cialo.services.notifications.NotificationService
import com.example.cialo.services.proximity.ProximityContentManager
import com.example.cialo.utils.RequestCodes
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin
import org.koin.dsl.module


class CialoApplication : Application() {

    private lateinit var _proximityManager: ProximityContentManager
    private lateinit var _proximityObserver: ProximityObserver

    private lateinit var _alarmManager: AlarmManager;
    private lateinit var _eventSchedulerIntent: PendingIntent;

    companion object {
        lateinit var instance: CialoApplication private set
        lateinit var databaseContext: DatabaseContext private set
    }

    override fun onCreate() {
        super.onCreate()
        instance = this;

        initDependencies();
        initEventScheduler();
        startEstimote()
    }


    fun runEventsScheduler() {
        _alarmManager.setRepeating(AlarmManager.ELAPSED_REALTIME,
            SystemClock.elapsedRealtime(),
            BuildConfig.SendEventsIntervalMinutes.toLong() * 60000,
            _eventSchedulerIntent)
    }

    fun stopEventsScheduler() {
        _alarmManager.cancel(_eventSchedulerIntent);
    }

    fun startEstimote() {
        _proximityObserver = createObserver()
        _proximityManager = ProximityContentManager(this)
        _proximityManager.start(_proximityObserver, listOf(RegionDto("tag-grunwald", "identifier")))
    }

    private fun createObserver(): ProximityObserver {
        val notificationService = NotificationService()
        val notification = notificationService.createProximityNotification(this)

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

    private fun initEventScheduler() {
        _alarmManager = this.getSystemService(Context.ALARM_SERVICE) as AlarmManager
        val eventServiceIntent = Intent(applicationContext, EventsSenderService::class.java)

        _eventSchedulerIntent = PendingIntent.getService(applicationContext,
            RequestCodes.schedulerIntentRequestCode,
            eventServiceIntent,
            PendingIntent.FLAG_UPDATE_CURRENT)

    }

    private fun initDependencies() {
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
    }
}