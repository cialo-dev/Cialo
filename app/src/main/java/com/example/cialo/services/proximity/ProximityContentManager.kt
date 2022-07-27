package com.example.cialo.services.proximity

import android.content.Context
import android.util.Log
import com.estimote.proximity_sdk.api.*
import com.example.cialo.models.BeaconModel
import com.example.cialo.services.database.DatabaseContext
import com.example.cialo.services.database.RegionEventType
import com.example.cialo.services.database.entities.RegionEventEntity
import com.example.cialo.services.notifications.INotificationService
import com.example.cialo.services.notifications.NotificationService
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import org.koin.java.KoinJavaComponent.inject

class ProximityContentManager {

    private var _proximityObserverHandler: ProximityObserver.Handler? = null
    private var _notificationService: INotificationService = NotificationService();
    private val databaseContext: DatabaseContext by inject(DatabaseContext::class.java)

    fun start(observer: ProximityObserver, regions: List<BeaconModel>) {
        val zones = mutableListOf<ProximityZone>();
        for (region in regions) {
            zones.add(createZone(region))
        }
        _proximityObserverHandler = observer.startObserving(zones)
    }

    fun stop() {
        _proximityObserverHandler?.stop()
    }

    private fun createZone(region: BeaconModel): ProximityZone {
        return ProximityZoneBuilder()
            .forTag(region.tag)
            .inFarRange()
            .onEnter { ctx ->
                //_notificationService.display(context, "Enter ${ctx.tag}");


                Log.i("Proximity", "Enter")
                GlobalScope.launch(Dispatchers.IO) {
                    databaseContext.regionEventsDao().insert(
                        RegionEventEntity(
                            0,
                            System.currentTimeMillis(),
                            RegionEventType.Enter,
                            ctx.tag
                        )
                    )
                }
            }
            .onExit { ctx ->
                //_notificationService.display(context, "Exit ${ctx.tag}");
                Log.i("Proximity", "Exit")
                GlobalScope.launch(Dispatchers.IO) {
                    databaseContext.regionEventsDao().insert(
                        RegionEventEntity(
                            0,
                            System.currentTimeMillis(),
                            RegionEventType.Exit,
                            ctx.tag
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