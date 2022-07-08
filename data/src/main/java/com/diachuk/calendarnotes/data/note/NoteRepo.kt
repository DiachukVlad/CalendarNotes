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

                Note(
                    date = entity.date,
                    id = entity.id,
                    components = components
                )
            }
        }

    fun insertNote(note: Note) {
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
                        checkListItemDao.insert(CheckListItemEntity(
                            checked = it.checked,
                            text = it.text,
                            checkListId = newCheckListId
                        ))
                    }
                }
            }
        }
    }

//    fun insertNote(note: Note) {
//        noteDao.insertAll(note.toNoteEntity())
//    }
//
//    fun getById(id: Int): Note {
//        return noteDao.getById(id).toNote()
//    }
//
//    fun update(note: Note) {
//        noteDao.update(note.toNoteEntity())
//    }
}