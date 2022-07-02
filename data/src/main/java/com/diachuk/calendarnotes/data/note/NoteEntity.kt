package com.diachuk.calendarnotes.data.note

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class NoteEntity(
    val date: Long,
    val text: String,
    val styles: ByteArray
) {
    @PrimaryKey(autoGenerate = true)
    var id: Int = 0


    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as NoteEntity

        if (id != other.id) return false

        return true
    }

    override fun hashCode(): Int {
        return id
    }
}