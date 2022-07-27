package com.example.cialo.services.database.entities

import androidx.room.*
import com.example.cialo.services.database.RegionEventType
import com.example.cialo.services.database.converters.RegionEventTypeConverter

@Entity(tableName = "RegionEvents")
class RegionEventEntity(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    @ColumnInfo val dateTimeTicks: Long,
    @TypeConverters(RegionEventTypeConverter::class) val eventType: RegionEventType,
    @ColumnInfo val beaconTag: String
)