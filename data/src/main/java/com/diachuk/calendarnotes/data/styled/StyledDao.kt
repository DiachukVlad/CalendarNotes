package com.diachuk.calendarnotes.data.styled

import androidx.room.Dao
import androidx.room.Query
import com.diachuk.calendarnotes.data.STYLED_ENTITY_TABLE_NAME
import com.diachuk.calendarnotes.data.StyledEntity
import com.diachuk.calendarnotes.data.base.BaseDao

@Dao
abstract class StyledDao: BaseDao<StyledEntity>(STYLED_ENTITY_TABLE_NAME) {
    @Query("select * from StyledEntity where noteId = :noteId")
    abstract fun getAllForNoteId(noteId: Long): List<StyledEntity>
}