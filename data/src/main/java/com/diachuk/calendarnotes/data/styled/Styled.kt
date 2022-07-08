@file:Suppress("PackageDirectoryMismatch")

package com.diachuk.calendarnotes.data

import androidx.room.Entity
import com.diachuk.calendarnotes.data.notecomponent.NoteComponentEntity

class Styled(
    val id: Long = -1,
    val text: String,
    val styles: List<Byte>,
    position: Int = 0
) : NoteComponent(position) {
    override fun toString(): String {
        return "Styled(id=$id, text='$text', styles=$styles)"
    }
}

const val STYLED_ENTITY_TABLE_NAME = "StyledEntity"

@Entity(tableName = STYLED_ENTITY_TABLE_NAME)
class StyledEntity(
    val text: String,
    val styles: ByteArray,
    noteId: Long,
    position: Int
) : NoteComponentEntity(noteId, position)

//
//fun StyledEntity.toStyled() = Styled(id, text, styles.toList())
//fun Styled.toStyledEntity() = StyledEntity(text, styles.toByteArray()).also { it.id = id }