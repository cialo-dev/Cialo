package com.example.cialo.services.api

import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.Headers
import retrofit2.http.POST


interface IApiClient {
    suspend fun getRegions(): List<RegionDto>

    @POST("authentication/login")
    suspend fun login(@Body model: LoginApiModel): Boolean
}