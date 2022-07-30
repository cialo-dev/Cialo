package com.example.cialo.services.apiimport com.example.cialo.BuildConfigimport com.example.cialo.models.BeaconModelimport com.example.cialo.utils.HttpOperationimport com.example.cialo.utils.HttpValueOperationimport com.example.cialo.utils.Operationimport com.example.cialo.utils.ValueOperationimport com.google.gson.Gsonimport com.jakewharton.retrofit2.adapter.rxjava2.RxJava2CallAdapterFactoryimport okhttp3.MediaTypeimport okhttp3.RequestBodyimport okhttp3.ResponseBodyimport org.json.JSONObjectimport retrofit2.Responseimport retrofit2.Retrofitimport retrofit2.converter.gson.GsonConverterFactoryclass CialoApiClient : IApiClient {    companion object {        private val jsonSerializer: Gson = Gson()        private fun retrofit(): RetrofitApiClient {            return Retrofit.Builder()                .baseUrl(BuildConfig.CialoApiUrl)                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())                .addConverterFactory(GsonConverterFactory.create())                .build()                .create(RetrofitApiClient::class.java);        }    }    override suspend fun login(model: LoginApiModel): HttpOperation {        return try {            readResponse(retrofit().login(model))        } catch (ex: Exception) {            HttpOperation.createFailed(-1, ex.toString());        }    }    override suspend fun sendBeaconEvents(        providerId: String,        events: BeaconEventsApiModel,    ): HttpOperation {        return try {            readResponse(retrofit().sendBeaconEvents(providerId, events));        } catch (ex: Exception) {            HttpOperation.createFailed(-1, ex.toString());        }    }    override suspend fun getBeacons(): HttpValueOperation<List<BeaconModel>> {        return try {            readResponse(retrofit().getBeacons()) {                val response = mutableListOf<BeaconModel>()                for (beacon in it) {                    response.add(BeaconModel(beacon.tag, beacon.name))                }                response.toList();            }        } catch (ex: Exception) {            HttpValueOperation.createFailed(-1, ex.toString());        }    }    private fun <T, T1> readResponse(response: Response<T>, map: (T) -> T1): HttpValueOperation<T1> {        return try {            if (response.isSuccessful && response.body() != null) {                HttpValueOperation.createSuccess(map(response.body()!!))            } else {                HttpValueOperation.createFailed(response.code(), readError(response.errorBody(), response.message()))            }        } catch (ex: Exception) {            HttpValueOperation.createFailed(-1, ex.toString());        }    }    private fun readResponse(response: Response<Unit>): HttpOperation {        return if (response.isSuccessful) {            HttpOperation.createSuccess();        } else {            HttpOperation.createFailed(response.code(), readError(response.errorBody(), response.message()));        }    }    private fun readError(response: ResponseBody?, default: String): String {        if (response == null) {            return default;        }        return try {            val responseString = response.string();            JSONObject(responseString).toString()        } catch (e: java.lang.Exception) {            default;        }    }}