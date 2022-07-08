@file:Suppress("PackageDirectoryMismatch")

package com.diachuk.calendarnotes.data

import androidx.room.Entity
import androidx.room.PrimaryKey

data class Styled(
    val id: Int = -1,
    val text: String,
    val styles: List<Byte>,
): NoteComponent()

@Entity
class StyledEntity(
    val text: String,
    val styles: ByteArray
) {
    @PrimaryKey(autoGenerate = true)
    var id: Int = 0
}


fun StyledEntity.toStyled() = Styled(id, text, styles.toList())
fun Styled.toStyledEntity() = StyledEntity(text, styles.toByteArray()).also { it.id = id }