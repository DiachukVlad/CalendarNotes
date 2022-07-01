package com.diachuk.calendarnotes.note

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
    val title = MutableStateFlow("Untitled")
    val dateText = MutableStateFlow("Date")
    val styledController = StyledController()

    fun changeTitle(string: String) {
        title.tryEmit(string)
    }

    fun onDoneClick() {
        viewModelScope.launch(Dispatchers.IO) {
            noteRepo.insertNote(
                Note(
                    title = title.value,
                    text = styledController.textField.annotatedString.text,
                    date = 0,
                    styles = styledController.styles
                )
            )

            withContext(Dispatchers.Main) {
                appState.routing.pop()
            }
        }
    }
}