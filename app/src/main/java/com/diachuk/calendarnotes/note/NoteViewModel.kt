package com.diachuk.calendarnotes.note

import androidx.lifecycle.ViewModel
import com.diachuk.calendarnotes.styledText.StyledController
import kotlinx.coroutines.flow.MutableStateFlow
import org.koin.android.annotation.KoinViewModel

@KoinViewModel
class NoteViewModel: ViewModel(){
    val title = MutableStateFlow("Untitled")
    val dateText = MutableStateFlow("Date")
    val styledController = StyledController()

    fun changeTitle(string: String) {
        title.tryEmit(string)
    }

    fun onDoneClick() {

    }
}