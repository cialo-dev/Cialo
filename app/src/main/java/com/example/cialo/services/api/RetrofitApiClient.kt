package com.example.cialo.services.api

import com.example.cialo.utils.Operation
import com.example.cialo.utils.ValueOperation
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.Header
import retrofit2.http.Headers
import retrofit2.http.POST


interface RetrofitApiClient {
    suspend fun getRegions(): List<RegionDto>

    @POST("authentication/login")
    @Headers("Content-Type: application/json")
    suspend fun login(@Body model: LoginApiModel): Response<Unit>

    @POST("beacon-events")
    @Headers("Content-Type: application/json")
    suspend fun sendBeaconEvents(
        @Header("provider_id") providerId: String,
        @Body model: BeaconEventsApiModel,
    ): Response<Unit>
}