package com.example.cialo.services.api

import com.example.cialo.models.BeaconModel
import com.example.cialo.utils.HttpOperation
import com.example.cialo.utils.HttpValueOperation
import com.example.cialo.utils.Operation
import com.example.cialo.utils.ValueOperation

interface IApiClient {
    suspend fun login(model: LoginApiModel): HttpOperation
    suspend fun sendBeaconEvents(providerId: String, events: BeaconEventsApiModel): HttpOperation
    suspend fun getBeacons(): HttpValueOperation<List<BeaconModel>>;
}