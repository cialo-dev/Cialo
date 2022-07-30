package com.example.cialo

import com.example.cialo.exceptionHandling.AppError
import com.example.cialo.services.database.DatabaseContext
import com.example.cialo.services.eventsscheduler.IEventSenderRunner
import com.example.cialo.services.proximity.IProximityRunner
import com.example.cialo.ui.abstraction.CialoViewModel
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import org.koin.java.KoinJavaComponent

@OptIn(DelicateCoroutinesApi::class)
class MainViewModel : CialoViewModel() {
    private val _eventsSenderRunner: IEventSenderRunner by KoinJavaComponent.inject(
        IEventSenderRunner::class.java)
    private val _proximityRunner: IProximityRunner by KoinJavaComponent.inject(IProximityRunner::class.java)

    fun initServices() {
        GlobalScope.launch(Dispatchers.IO) {
            _proximityRunner.run();
            _eventsSenderRunner.run();
        }
    }

    override fun onError(error: AppError) {
        super.onError(error)
    }
}