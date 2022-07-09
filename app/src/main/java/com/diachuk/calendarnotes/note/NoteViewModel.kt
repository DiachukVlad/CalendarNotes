package com.diachuk.calendarnotes.note

import androidx.compose.runtime.mutableStateListOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.diachuk.calendarnotes.AppState
import com.diachuk.calendarnotes.data.CheckList
import com.diachuk.calendarnotes.data.Styled
import com.diachuk.calendarnotes.data.note.Note
import com.diachuk.calendarnotes.data.note.NoteRepo
import com.diachuk.calendarnotes.list.CheckListController
import com.diachuk.calendarnotes.styledText.StyledController
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.koin.android.annotation.KoinViewModel

@KoinViewModel
class NoteViewModel(private val noteRepo: NoteRepo, private val appState: AppState) : ViewModel() {
    var id: Long? = null
        set(value) {
            field = value
            setupNote()
        }

    val isEditing get() = id != null

    val dateText = MutableStateFlow("Date")
    val controllers = mutableStateListOf<Any>()

    fun addCheckList() {
        controllers.add(CheckListController().apply { focusIndex.tryEmit(0) })
        controllers.add(StyledController())
    }

    fun onDoneClick() {
        viewModelScope.launch(Dispatchers.IO) {
            val note = Note(
                date = 0,
                id = id ?: 0,
                components = controllers
                    .filter { it is StyledController || it is CheckListController }
                    .map {
                        when (it) {
                            is StyledController -> it.generateStyled()
                            is CheckListController -> it.generateCheckList()
                            else -> CheckList(items = listOf())
                        }
                    }
            )

            if (isEditing) {
                noteRepo.update(note)
            } else {
                noteRepo.insert(note)
            }
            withContext(Dispatchers.Main) {
                appState.routing.pop()
            }
        }
    }

    private fun setupNote() {
        viewModelScope.launch(Dispatchers.IO) {
            controllers.clear()

            if (id == null) {
                controllers.add(StyledController())
                return@launch
            }

            val note = noteRepo.getById(id!!) ?: return@launch
            note.components.forEach {
                @Suppress("NON_EXHAUSTIVE_WHEN_STATEMENT")
                when (it) {
                    is Styled -> controllers.add(StyledController(it))
                    is CheckList -> controllers.add(CheckListController(it))
                }
            }
        }
    }
}