package com.diachuk.calendarnotes.data

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.diachuk.calendarnotes.data.note.NoteDao
import com.diachuk.calendarnotes.data.note.NoteEntity

@Database(entities = [NoteEntity::class], version = 1)
@TypeConverters(Converters::class)
abstract class CalendarNotesDB: RoomDatabase() {
    abstract fun noteDao(): NoteDao
}