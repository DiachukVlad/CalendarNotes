package com.diachuk.calendarnotes.data.note

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update

@Dao
interface NoteDao {
    @Query("SELECT * FROM NoteEntity")
    fun getAll(): List<NoteEntity>

    @Query("SELECT * from NoteEntity where id = :id")
    fun getById(id: Int): NoteEntity

    @Insert
    fun insertAll(vararg notes: NoteEntity)

    @Update
    fun update(note: NoteEntity)
}