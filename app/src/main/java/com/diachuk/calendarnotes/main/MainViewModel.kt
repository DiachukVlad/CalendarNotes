package com.diachuk.calendarnotes.main

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.diachuk.calendarnotes.AppState
import com.diachuk.calendarnotes.data.note.Note
import com.diachuk.calendarnotes.data.note.NoteRepo
import com.diachuk.calendarnotes.note.NoteRoute
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import org.koin.android.annotation.KoinViewModel

@KoinViewModel
class MainViewModel(private val appState: AppState, private val noteRepo: NoteRepo): ViewModel(){
    val notes = MutableStateFlow(listOf<Note>())

    fun onLaunch() {
        viewModelScope.launch(Dispatchers.IO) {
            notes.emit(noteRepo.notes)
        }
    }

    fun createNote() {
        appState.routing.push(NoteRoute(null))
    }

    fun noteClicked(index: Int) {
        appState.routing.push(NoteRoute(notes.value[index].id))
    }
}