package com.example.cialo.services.apiimport android.util.Logimport com.beust.klaxon.Klaxonimport com.google.gson.Gsonimport com.google.gson.GsonBuilderimport com.google.gson.JsonParserimport kotlinx.coroutines.CoroutineScopeimport kotlinx.coroutines.Dispatchersimport kotlinx.coroutines.launchimport kotlinx.coroutines.withContextimport okhttp3.MediaTypeimport okhttp3.RequestBodyimport retrofit2.Callimport retrofit2.Callbackimport retrofit2.Responseimport retrofit2.Retrofitimport retrofit2.http.Bodyimport javax.inject.Singletonpublic class CialoApiClient : IApiClient {    override suspend fun sendEnter(@Body requestBody: RequestBody) {        val retrofit = Retrofit.Builder()            .baseUrl("https://cialo-platform.azurewebsites.net")            .build()        val gson = Gson()        val body = gson.toJson(requestBody)        var requestBody = RequestBody.create(MediaType.parse("application/json"), body)        return retrofit.create(IApiClient::class.java).sendEnter(requestBody)    }    override suspend fun getRegions(): List<RegionDto> {        return listOf(RegionDto("tag-grunwald", "identifier"))    }}