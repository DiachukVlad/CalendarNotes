package com.diachuk.calendarnotes.data.note

class NoteRepo(private val noteDao: NoteDao) {
    val notes: List<Note> get() = noteDao.getAll().map { it.toNote() }

    fun insertNote(note: Note) {
        noteDao.insertAll(note.toNoteEntity())
    }

    fun getById(id: Int): Note {
        return noteDao.getById(id).toNote()
    }

    fun update(note: Note) {
        noteDao.update(note.toNoteEntity())
    }
}