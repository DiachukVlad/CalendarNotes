package com.diachuk.calendarnotes.data.di

import androidx.room.Room
import com.diachuk.calendarnotes.data.CalendarNotesDB
import com.diachuk.calendarnotes.data.note.NoteRepo
import org.koin.android.ext.koin.androidApplication
import org.koin.dsl.module

val dataModule = module {
    single {
        Room.databaseBuilder(
            androidApplication(),
            CalendarNotesDB::class.java,
            "app_database")
            .build()
    }

    factory {
        get<CalendarNotesDB>().noteDao()
    }

    factory { NoteRepo(get()) }
}