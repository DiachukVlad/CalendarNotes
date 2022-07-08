package com.diachuk.calendarnotes.data.note

import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverters
import com.diachuk.calendarnotes.data.*


data class Note(
    val date: Long,
    val id: Int = 0,
    val components: List<NoteComponent>
)

@Entity
data class NoteEntity(
    val date: Long,
    val components: List<NoteComponentEntity>
) {
    @PrimaryKey(autoGenerate = true)
    var id: Int = 0
}

fun NoteEntity.toNote() = Note(
    date = date,
    id = id,
    components = components
        .filter { it.styled != null || it.checkList != null }
        .map {
            if (it.styled != null) {
                it.styled.toStyled()
            } else {
                it.checkList!!.toCheckList()
            }
        }
)

fun Note.toNoteEntity() = NoteEntity(
    date = date,
    components = components.map { it.toNoteComponentEntity() }
).also { it.id = id }
