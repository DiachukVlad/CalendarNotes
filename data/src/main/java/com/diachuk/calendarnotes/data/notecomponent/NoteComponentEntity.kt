package com.diachuk.calendarnotes.data.notecomponent

import androidx.room.Entity
import com.diachuk.calendarnotes.data.CheckListEntity
import com.diachuk.calendarnotes.data.StyledEntity
import com.diachuk.calendarnotes.data.base.BaseEntity

const val NOTE_COMPONENT_ENTITY_TABLE_NAME = "NoteComponentEntity"


open class NoteComponentEntity(
    val noteId: Long,
    val position: Int
): BaseEntity()