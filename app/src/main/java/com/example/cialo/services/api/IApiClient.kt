package com.example.cialo.services.api

import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.Headers
import retrofit2.http.POST


interface IApiClient {
    @Headers("Content-Type: application/json")
    @POST("region/rage")
    suspend fun sendEnter(@Body requestBody: RequestBody)

    suspend fun getRegions() : List<RegionDto>
}