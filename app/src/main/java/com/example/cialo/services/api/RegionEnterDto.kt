package com.example.cialo.services.api
import org.threeten.bp.LocalDateTime;

data class RegionEnterDto(var deviceDto: DeviceDto, var dateTime: LocalDateTime, var beaconId: String)