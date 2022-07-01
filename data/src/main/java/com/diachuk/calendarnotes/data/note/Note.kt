package com.diachuk.calendarnotes.data.note

data class Note(
    val title: String,
    val text: String,
    val styles: ArrayList<Byte>,
    val date: Long,
    val id: Int = 0
)

fun NoteEntity.toNote(): Note {
    return Note(
        title = title,
        text = text,
        styles = ArrayList(styles.asList()),
        date = date,
        id = id,
    )
}

fun Note.toNoteEntity(): NoteEntity {
    return NoteEntity(
        title = title,
        text = text,
        styles = styles.toByteArray(),
        date = date
    ).also { it.id = id }
}
