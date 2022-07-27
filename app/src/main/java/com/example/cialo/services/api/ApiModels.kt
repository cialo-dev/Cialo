package com.example.cialo.services.api

import com.example.cialo.services.database.RegionEventType

data class LoginApiModel(
    val provider: Int,
    val providerId: String,
    val email: String?,
    val firstName: String?,
    val lastName: String?,
)

data class BeaconEventsApiModel(val events: List<BeaconEventApiModel>)
data class BeaconEventApiModel(
    val beaconTag: String,
    val dateTime: String,
    val type: Int,
)


