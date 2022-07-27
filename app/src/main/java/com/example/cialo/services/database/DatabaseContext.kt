package com.example.cialo.services.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.cialo.services.database.dao.RegionEventDao
import com.example.cialo.services.database.entities.RegionEventEntity

@Database(entities = [RegionEventEntity::class], version = 2)
abstract class DatabaseContext : RoomDatabase(){
    abstract fun regionEventsDao() : RegionEventDao
}