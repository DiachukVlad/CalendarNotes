package com.diachuk.calendarnotes.note

import androidx.compose.ui.text.input.TextFieldValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.diachuk.calendarnotes.AppState
import com.diachuk.calendarnotes.data.note.Note
import com.diachuk.calendarnotes.data.note.NoteRepo
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

    val title = MutableStateFlow("Untitled")
    val dateText = MutableStateFlow("Date")
    val styledController = StyledController()

    fun changeTitle(string: String) {
        title.tryEmit(string)
    }

    fun onDoneClick() {
        viewModelScope.launch(Dispatchers.IO) {
            val note = Note(
                title = title.value,
                text = styledController.textField.value.annotatedString.text,
                date = 0,
                styles = styledController.styles,
                id = id ?: 0
            )

            if (isEditing) {
                noteRepo.update(note)
            } else {
                noteRepo.insertNote(note)
            }
            withContext(Dispatchers.Main) {
                appState.routing.pop()
            }
        }
    }

    private fun setupNote() {
        viewModelScope.launch(Dispatchers.IO) {
            if (id == null) {
                title.emit("Untitled")
                styledController.styles.clear()
                styledController.textField.emit(TextFieldValue())
                return@launch
            }


            val note = noteRepo.getById(id!!)
            title.emit(note.title)
            styledController.styles.run {
                clear()
                addAll(note.styles)
            }
            styledController.textField.emit(TextFieldValue(text = note.text))
        }
    }
}