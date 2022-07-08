package com.diachuk.calendarnotes.data.note

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.diachuk.calendarnotes.data.base.BaseDao

@Dao
abstract class NoteDao: BaseDao<NoteEntity>(NOTE_ENTITY_TABLE_NAME)