package com.diachuk.calendarnotes

import android.app.Application
import com.diachuk.calendarnotes.di.AppModule
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
                AppModule().module
            )
        }
    }

    
}