package com.example.cialo.services.proximity

import android.content.Context
import android.util.Log
import com.estimote.proximity_sdk.api.*
import com.example.cialo.services.api.CialoApiClient
import com.example.cialo.services.api.IApiClient
import com.example.cialo.services.api.RegionDto
import com.example.cialo.services.database.DatabaseContext
import com.example.cialo.services.database.RegionEventType
import com.example.cialo.services.database.entities.RegionEventEntity
import com.example.cialo.services.notifications.INotificationService
import com.example.cialo.services.notifications.NotificationService
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.koin.java.KoinJavaComponent.inject

class ProximityContentManager(
    private val context: Context,
    private val appId: String,
    private val appToken: String,
) {

    private lateinit var _proximityTriggerHandler: ProximityTrigger.Handler
    private var _proximityObserverHandler: ProximityObserver.Handler? = null
    private var _notificationService: INotificationService = NotificationService();
    private var apiClient: IApiClient = CialoApiClient();
    private val databaseContext: DatabaseContext by inject(DatabaseContext::class.java)

    suspend fun start() {
        withContext(Dispatchers.IO) {
            val regions = apiClient.getRegions();
            val zones = mutableListOf<ProximityZone>();
            val observer = createObserver();
            for (region in regions) {
                zones.add(createZone(region))
            }
            _proximityObserverHandler = observer.startObserving(zones)
        }
    }

    fun stop() {
        _proximityObserverHandler?.stop()
        _proximityTriggerHandler.stop()
    }

    private fun createObserver(): ProximityObserver {

        val notification = _notificationService.createProximityNotification(context)

        _proximityTriggerHandler = ProximityTriggerBuilder(context)
            .displayNotificationWhenInProximity(notification)
            .build()
            .start()

        return ProximityObserverBuilder(context, EstimoteCloudCredentials(appId, appToken))
            .withTelemetryReportingDisabled()
            .withEstimoteSecureMonitoringDisabled()
            .onError { throwable ->
                Log.e("app", "proximity observer error: $throwable")
            }
            .withBalancedPowerMode()
            //.withScannerInForegroundService(_notificationService.createProximityNotification(context))
            .build()
    }

    private fun createZone(region: RegionDto): ProximityZone {
        return ProximityZoneBuilder()
            .forTag(region.tag)
            .inFarRange()
            .onEnter { ctx ->
                _notificationService.display(context, "Enter ${ctx.tag}");

                GlobalScope.launch(Dispatchers.IO) {
                    databaseContext.regionEventsDao().insert(
                        RegionEventEntity(
                            0,
                            System.currentTimeMillis(),
                            RegionEventType.Enter
                        )
                    )
                }
            }
            .onExit { ctx ->
                _notificationService.display(context, "Exit ${ctx.tag}");

                GlobalScope.launch(Dispatchers.IO) {
                    databaseContext.regionEventsDao().insert(
                        RegionEventEntity(
                            0,
                            System.currentTimeMillis(),
                            RegionEventType.Exit
                        )
                    )
                }
            }
            .onContextChange { contexts ->
                val nearbyContent = ArrayList<ProximityContent>(contexts.size)
                val stringBuilder = StringBuilder();
                for (context in contexts) {
                    val title: String = context.attachments["cialokotlin-7y8/title"] ?: "unknown"
                    nearbyContent.add(ProximityContent(title, context.deviceId))
                    stringBuilder.appendLine(title);
                }
                //notificationService.display(context, stringBuilder.toString());
            }
            .build()

    }
}