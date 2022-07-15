package com.example.cialo.ui.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.cialo.services.database.DatabaseContext
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import org.koin.java.KoinJavaComponent

class HomeViewModel : ViewModel() {
    private val _databaseContext: DatabaseContext by KoinJavaComponent.inject(DatabaseContext::class.java)

    private val _text = MutableLiveData<String>().apply {
        value = "This is home Fragment"
    }
    val text: LiveData<String> = _text

    fun cleanDatabase() {
        GlobalScope.launch(Dispatchers.IO) {
            _databaseContext.regionEventsDao().removeAll()
        }
    }
}