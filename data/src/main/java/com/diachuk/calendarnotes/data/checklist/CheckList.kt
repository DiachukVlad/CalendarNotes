@file:Suppress("PackageDirectoryMismatch")

package com.diachuk.calendarnotes.data

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.diachuk.calendarnotes.data.base.BaseEntity
import com.diachuk.calendarnotes.data.notecomponent.NoteComponentEntity

class CheckList(
    val id: Long = -1,
    val items: List<CheckListItem>,
    position: Int = 0
) : NoteComponent(position) {
    override fun toString(): String {
        return "CheckList(id=$id, items=$items)"
    }
}

data class CheckListItem(
    val id: Long = -1,
    val checked: Boolean,
    val text: String
)

const val CHECK_LIST_ENTITY_TABLE_NAME = "CheckListEntity"

@Entity(tableName = CHECK_LIST_ENTITY_TABLE_NAME)
class CheckListEntity(
    noteId: Long,
    position: Int
): NoteComponentEntity(noteId, position)

const val CHECK_LIST_ITEM_ENTITY_TABLE_NAME = "CheckListItemEntity"

@Entity(tableName = CHECK_LIST_ITEM_ENTITY_TABLE_NAME)
class CheckListItemEntity(
    val checked: Boolean,
    val text: String,
    val checkListId: Long
): BaseEntity()
