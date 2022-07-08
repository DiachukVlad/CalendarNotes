package com.diachuk.calendarnotes.note

import androidx.compose.runtime.mutableStateListOf
import androidx.compose.ui.text.input.TextFieldValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.diachuk.calendarnotes.AppState
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
    var id: Int? = null
        set(value) {
            field = value
            setupNote()
        }

    val isEditing get() = id != null

    val dateText = MutableStateFlow("Date")
    val controllers = mutableStateListOf(
        StyledController(),
        CheckListController(),
        StyledController(),
    )

    fun addCheckList() {
        controllers.add(CheckListController())
    }

    fun onDoneClick() {
        viewModelScope.launch(Dispatchers.IO) {
//            val note = Note(
//                text = styledController.textField.value.annotatedString.text,
//                date = 0,
//                styles = styledController.styles,
//                id = id ?: 0
//            )
//
//            if (isEditing) {
//                noteRepo.update(note)
//            } else {
//                noteRepo.insertNote(note)
//            }
            withContext(Dispatchers.Main) {
                appState.routing.pop()
            }
        }
    }

    private fun setupNote() {
        viewModelScope.launch(Dispatchers.IO) {
//            if (id == null) {
//                styledController.styles.clear()
//                styledController.textField.emit(TextFieldValue())
//                return@launch
//            }

//
//            val note = noteRepo.getById(id!!)
//            styledController.styles.run {
//                clear()
//                addAll(note.styles)
//            }
//            styledController.textField.emit(TextFieldValue(text = note.text))
        }
    }
}