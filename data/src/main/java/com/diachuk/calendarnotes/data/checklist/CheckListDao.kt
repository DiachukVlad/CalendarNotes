package com.diachuk.calendarnotes.data.checklist

import androidx.room.Dao
import androidx.room.Query
import com.diachuk.calendarnotes.data.CHECK_LIST_ENTITY_TABLE_NAME
import com.diachuk.calendarnotes.data.CHECK_LIST_ITEM_ENTITY_TABLE_NAME
import com.diachuk.calendarnotes.data.CheckListEntity
import com.diachuk.calendarnotes.data.CheckListItemEntity
import com.diachuk.calendarnotes.data.base.BaseDao

@Dao
abstract class CheckListItemDao : BaseDao<CheckListItemEntity>(CHECK_LIST_ITEM_ENTITY_TABLE_NAME) {
    @Query("select * from CheckListItemEntity where checkListId = :checkListId")
    abstract fun getAllForCheckListId(checkListId: Long): List<CheckListItemEntity>
}

@Dao
abstract class CheckListDao : BaseDao<CheckListEntity>(CHECK_LIST_ENTITY_TABLE_NAME) {
    @Query("select * from CheckListEntity where noteId = :noteId")
    abstract fun getAllForNoteId(noteId: Long): List<CheckListEntity>
}