package com.diachuk.calendarnotes.data

import androidx.room.Database
import androidx.room.RoomDatabase
import com.diachuk.calendarnotes.data.checklist.CheckListDao
import com.diachuk.calendarnotes.data.checklist.CheckListItemDao
import com.diachuk.calendarnotes.data.note.NoteDao
import com.diachuk.calendarnotes.data.note.NoteEntity
import com.diachuk.calendarnotes.data.styled.StyledDao

@Database(
    entities = [
        NoteEntity::class,
        StyledEntity::class,
        CheckListEntity::class,
        CheckListItemEntity::class
    ],
    version = 1
)
abstract class CalendarNotesDB : RoomDatabase() {
    abstract fun noteDao(): NoteDao
    abstract fun styledDao(): StyledDao
    abstract fun checkListDao(): CheckListDao
    abstract fun checkListItemDao(): CheckListItemDao
}