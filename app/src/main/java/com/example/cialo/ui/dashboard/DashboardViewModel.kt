package com.example.cialo.ui.dashboard

import android.graphics.Region
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.liveData
import com.example.cialo.models.RegionEventModel
import com.example.cialo.services.database.DatabaseContext
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.koin.java.KoinJavaComponent
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.ZoneId
import java.util.*

class DashboardViewModel : ViewModel() {

    private val databaseContext: DatabaseContext by KoinJavaComponent.inject(DatabaseContext::class.java)
    val regionEvents = MutableLiveData<MutableList<RegionEventModel>>()

    init {
        loadEvents();
    }

    private fun loadEvents() {
        GlobalScope.launch(Dispatchers.IO) {
            val result = databaseContext.regionEventsDao().getAll().map {
                RegionEventModel(it.dateTimeTicks, it.eventType)
            }
            withContext(Dispatchers.Main) {
                regionEvents.value = result.toMutableList();
            }
        }
    }
}