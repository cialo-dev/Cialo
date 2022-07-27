package com.example.cialo.services.proximity

import android.util.Log
import com.estimote.proximity_sdk.api.ProximityObserver
import com.example.cialo.services.api.IApiClient
import com.example.cialo.utils.Operation
import kotlinx.coroutines.*
import org.koin.java.KoinJavaComponent

class ProximityRunner(
    private val _observer: ProximityObserver,
    private val _manager: ProximityContentManager,
) : IProximityRunner {
    private val _apiClient: IApiClient by KoinJavaComponent.inject(IApiClient::class.java)

    @OptIn(DelicateCoroutinesApi::class)
    override suspend fun run(): Operation {

        GlobalScope.launch(Dispatchers.IO) {
            val beaconsOperation = _apiClient.getBeacons();

            if (beaconsOperation.succeed) {
                _manager.start(_observer, beaconsOperation.value!!);
            } else {
                Log.e("ProximityRunner", beaconsOperation.message!!);
            }
        }
        return Operation.createSuccess();
    }

    override fun stop() {
        TODO("Not yet implemented")
    }
}