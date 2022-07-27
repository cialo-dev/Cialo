package com.example.cialo.services.api

import com.example.cialo.utils.Operation
import com.example.cialo.utils.ValueOperation

interface IApiClient {
    suspend fun login(model: LoginApiModel): Operation
    suspend fun sendBeaconEvents(providerId: String, events: BeaconEventsApiModel): Operation
    suspend fun getRegions(): List<RegionDto>;
}