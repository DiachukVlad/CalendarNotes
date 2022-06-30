package com.diachuk.calendarnotes.main

import androidx.lifecycle.ViewModel
import com.diachuk.calendarnotes.AppState
import com.diachuk.calendarnotes.note.NoteRoute
import org.koin.android.annotation.KoinViewModel

@KoinViewModel
class MainViewModel(val appState: AppState): ViewModel(){
    fun createNote() {
        appState.routing.push(NoteRoute)
    }
}