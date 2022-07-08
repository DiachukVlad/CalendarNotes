package com.diachuk.calendarnotes.data

import androidx.room.Entity
import androidx.room.PrimaryKey

sealed class NoteComponent

@Entity
class NoteComponentEntity(
    val styled: StyledEntity?,
    val checkList: CheckListEntity?
) {
    @PrimaryKey(autoGenerate = true)
    var id: Int = 0
}

fun NoteComponent.toNoteComponentEntity() = NoteComponentEntity(
        styled = if (this is Styled) toStyledEntity() else null,
        checkList = if (this is CheckList) toCheckListEntity() else null
    )