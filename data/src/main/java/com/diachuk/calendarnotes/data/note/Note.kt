package com.diachuk.calendarnotes.data.note

import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverters
import com.diachuk.calendarnotes.data.*
import com.diachuk.calendarnotes.data.base.BaseEntity


data class Note(
    val date: Long,
    val id: Long = 0,
    val components: List<NoteComponent>
)

const val NOTE_ENTITY_TABLE_NAME = "NoteEntity"

@Entity(tableName = NOTE_ENTITY_TABLE_NAME)
data class NoteEntity(
    val date: Long,
): BaseEntity()