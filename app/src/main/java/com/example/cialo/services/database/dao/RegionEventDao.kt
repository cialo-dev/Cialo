package com.example.cialo.services.database.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import com.example.cialo.services.database.entities.RegionEventEntity

@Dao
interface RegionEventDao {
    @Query("SELECT * FROM RegionEvents")
    fun getAll(): List<RegionEventEntity>
    @Insert
    fun insert(regionEvent: RegionEventEntity)
    @Query("DELETE FROM RegionEvents")
    fun removeAll()
}