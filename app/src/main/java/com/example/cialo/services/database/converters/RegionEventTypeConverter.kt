package com.example.cialo.services.database.converters

import androidx.room.TypeConverter
import com.example.cialo.services.database.RegionEventType
import java.util.*

class RegionEventTypeConverter {

    @TypeConverter
    fun toEventType(value: Int) = enumValues<RegionEventType>()[value]

    @TypeConverter
    fun fromEventType(value: RegionEventType) = value.value
}
