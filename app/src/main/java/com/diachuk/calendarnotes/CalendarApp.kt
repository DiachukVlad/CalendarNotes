package com.diachuk.calendarnotes

import android.app.Application
import com.diachuk.calendarnotes.data.di.dataModule
import com.diachuk.calendarnotes.di.AppModule
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin
import org.koin.ksp.generated.*

class CalendarApp: Application() {
    override fun onCreate() {
        super.onCreate()

        startKoin {
            androidLogger()
            androidContext(this@CalendarApp)
            modules(
                AppModule().module,
                dataModule
            )
        }
    }
}