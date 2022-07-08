@file:Suppress("PackageDirectoryMismatch")

package com.diachuk.calendarnotes.data

import androidx.room.Entity
import androidx.room.PrimaryKey

data class CheckList(
    val id: Int = -1,
    val items: List<CheckListItem>
) : NoteComponent()

data class CheckListItem(
    val id: Int = -1,
    val checked: Boolean,
    val text: String
)

@Entity
class CheckListEntity(
    val items: List<CheckListItemEntity>
) {
    @PrimaryKey(autoGenerate = true)
    var id: Int = 0
}

@Entity
class CheckListItemEntity(
    val checked: Boolean,
    val text: String
) {
    @PrimaryKey(autoGenerate = true)
    var id: Int = 0
}

fun CheckListEntity.toCheckList() = CheckList(id, items.map { it.toCheckListItem() })
fun CheckListItemEntity.toCheckListItem() = CheckListItem(id, checked, text)

fun CheckList.toCheckListEntity() =
    CheckListEntity(items = items.map { it.toCheckListItemEntity() }).also { it.id = id }

fun CheckListItem.toCheckListItemEntity() =
    CheckListItemEntity(text = text, checked = checked).also { it.id = id }