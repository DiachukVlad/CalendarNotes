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
import com.diachuk.calendarnotes.styledText.StyleType
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

    private val note
        get() = Note(
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

    /**
     * Event handlers
     */

    fun addCheckList() {
        controllers.add(CheckListController()
            .apply {
                focusIndex.tryEmit(0)
                onRemovedLastElement = {
                    println("remove")
                    controllers.remove(this@apply)
                    mergeStyled()
                }
            })
        controllers.add(StyledController())
    }

    fun removeNote() {
        viewModelScope.launch(Dispatchers.IO) {
            noteRepo.delete(note)
            navigateBack()
        }
    }

    fun onDoneClick() {
        viewModelScope.launch(Dispatchers.IO) {
            if (isEditing) {
                noteRepo.update(note)
            } else {
                noteRepo.insert(note)
            }
            navigateBack()
        }
    }

    /**
     * Private methods
     */

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
                    is CheckList -> controllers.add(CheckListController(it).apply {
                        onRemovedLastElement = {
                            controllers.remove(this@apply)
                            mergeStyled()
                        }
                    })
                }
            }

            mergeStyled()
        }
    }

    private fun mergeStyled() {
        controllers.removeIf { it is CheckListController && it.items.isEmpty() }

        var i = 0
        while (i < controllers.size - 1) {
            val current = controllers[i]
            val next = controllers[i + 1]
            if (current is StyledController && next is StyledController) {
                current.textField.tryEmit(
                    current.textField.value.copy(
                        text = current.textField.value.text + "\n" + next.textField.value.text
                    )
                )
                current.styles.apply {
                    add(StyleType.None.byte)
                    addAll(next.styles)
                }

                controllers[i] = StyledController(current.generateStyled())
                controllers.remove(next)
            } else {
                i++
            }
        }
    }

    private suspend fun navigateBack() = withContext(Dispatchers.Main) {
        appState.routing.pop()
    }
}