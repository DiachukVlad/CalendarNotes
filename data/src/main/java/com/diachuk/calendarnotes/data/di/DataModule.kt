package com.diachuk.calendarnotes.data.di

import androidx.room.Room
import com.diachuk.calendarnotes.data.CalendarNotesDB
import org.koin.android.ext.koin.androidApplication
import org.koin.core.annotation.ComponentScan
import org.koin.core.annotation.Module
import org.koin.dsl.module
import org.koin.ksp.generated.module

@Module
@ComponentScan("com.diachuk.calendarnotes.data")
class DefDataModule

val dataModule = module {
    includes(DefDataModule().module)
    single {
        Room.databaseBuilder(
            androidApplication(),
            CalendarNotesDB::class.java,
            "app_database"
        )
            .build()
    }

    factory { get<CalendarNotesDB>().noteDao() }
    factory { get<CalendarNotesDB>().styledDao() }
    factory { get<CalendarNotesDB>().checkListDao() }
    factory { get<CalendarNotesDB>().checkListItemDao() }
}