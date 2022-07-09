package com.diachuk.calendarnotes.data.note

import com.diachuk.calendarnotes.data.*
import com.diachuk.calendarnotes.data.checklist.CheckListDao
import com.diachuk.calendarnotes.data.checklist.CheckListItemDao
import com.diachuk.calendarnotes.data.styled.StyledDao
import org.koin.core.annotation.Factory

@Factory
class NoteRepo(
    private val noteDao: NoteDao,
    private val styledDao: StyledDao,
    private val checkListDao: CheckListDao,
    private val checkListItemDao: CheckListItemDao
) {
    val notes: List<Note>
        get() {
            val entities = noteDao.getAll() ?: return listOf()
            return entities.map { entity ->
                noteEntityToNote(entity)
            }
        }

    fun insert(note: Note) {
        val newId = noteDao.insert(NoteEntity(note.date))

        note.components.forEachIndexed { index, it ->
            @Suppress("NON_EXHAUSTIVE_WHEN_STATEMENT")
            when (it) {
                is Styled -> {
                    styledDao.insert(
                        StyledEntity(
                            text = it.text,
                            styles = it.styles.toByteArray(),
                            noteId = newId,
                            position = index
                        )
                    )
                }
                is CheckList -> {
                    val newCheckListId = checkListDao.insert(
                        CheckListEntity(
                            noteId = newId,
                            position = index
                        )
                    )
                    it.items.forEach {
                        checkListItemDao.insert(
                            CheckListItemEntity(
                                checked = it.checked,
                                text = it.text,
                                checkListId = newCheckListId
                            )
                        )
                    }
                }
            }
        }
    }

    fun getById(id: Long): Note? {
        return noteDao.getEntityById(id)?.let { noteEntityToNote(it) }
    }

    fun delete(note: Note) {
        noteDao.delete(NoteEntity(note.date).also { it.id = note.id })

        note.components.forEachIndexed { index, it ->
            @Suppress("NON_EXHAUSTIVE_WHEN_STATEMENT")
            when (it) {
                is Styled -> {
                    styledDao.delete(
                        StyledEntity(
                            text = it.text,
                            styles = it.styles.toByteArray(),
                            noteId = note.id,
                            position = index
                        ).apply { id = it.id }
                    )
                }
                is CheckList -> {
                    checkListDao.delete(
                        CheckListEntity(
                            noteId = note.id,
                            position = index
                        ).apply { id = it.id }
                    )
                    it.items.forEach {
                        checkListItemDao.delete(
                            CheckListItemEntity(
                                checked = it.checked,
                                text = it.text,
                                checkListId = 0
                            ).apply { id = it.id }
                        )
                    }
                }
            }
        }
    }

    fun update(note: Note) {
        delete(note)
        insert(note)
    }

    /**
     * Private methods
     */

    private fun noteEntityToNote(entity: NoteEntity): Note {
        val components = arrayListOf<NoteComponent>()

        val styledEntities = styledDao.getAllForNoteId(entity.id)
        styledEntities.forEach {
            components.add(
                Styled(
                    id = it.id,
                    text = it.text,
                    styles = it.styles.toList(),
                    position = it.position
                )
            )
        }

        val checkListEntities = checkListDao.getAllForNoteId(entity.id)
        checkListEntities.forEach {
            val itemsEntities = checkListItemDao.getAllForCheckListId(it.id)
            val checkList = CheckList(
                id = it.id,
                position = it.position,
                items = itemsEntities.map { item ->
                    CheckListItem(
                        id = item.id,
                        checked = item.checked,
                        text = item.text
                    )
                }
            )
            components.add(checkList)
        }

        components.sortBy { it.position }

        return Note(
            date = entity.date,
            id = entity.id,
            components = components
        )
    }
}