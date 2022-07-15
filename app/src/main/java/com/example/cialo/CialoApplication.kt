package com.example.cialo

import android.app.Application
import androidx.room.Database
import androidx.room.Room
import com.example.cialo.services.api.CialoApiClient
import com.example.cialo.services.api.IApiClient
import com.example.cialo.services.database.DatabaseContext
import com.example.cialo.services.notifications.INotificationService
import com.example.cialo.services.notifications.NotificationService
import com.example.cialo.services.proximity.ProximityContentManager
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin
import org.koin.dsl.module

class CialoApplication : Application() {

    private lateinit var _proximtiyManager: ProximityContentManager;

    companion object {
        lateinit var instance: CialoApplication private set
        lateinit var databaseContext: DatabaseContext private set
    }

    override fun onCreate() {
        super.onCreate()
        instance = this;

        val appModule = module {
            single<INotificationService> { NotificationService() }
            single<IApiClient> { CialoApiClient() }
            single<DatabaseContext> {
                Room.databaseBuilder(
                    applicationContext,
                    DatabaseContext::class.java, "cialodb"
                ).build()
            }
        }
        startKoin {
            androidContext(this@CialoApplication)
            modules(appModule)
        }

        startEstimote()
    }

    fun startEstimote() {




        _proximtiyManager = ProximityContentManager(
            this,
            getString(R.string.estimoteAppId),
            getString(R.string.estimoteAppToken)
        )
        GlobalScope.launch(Dispatchers.IO) {
            _proximtiyManager.start()
        }
    }
}