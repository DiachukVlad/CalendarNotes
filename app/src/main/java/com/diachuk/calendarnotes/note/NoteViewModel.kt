package com.diachuk.calendarnotes.note

import com.diachuk.calendarnotes.styledText.StyledController
import kotlinx.coroutines.flow.MutableStateFlow

class NoteViewModel {
    val title = MutableStateFlow("Untitled")
    val dateText = MutableStateFlow("Date")
    val styledController = StyledController()

    fun changeTitle(string: String) {
        title.tryEmit(string)
    }

    fun onDoneClick() {

    }
}