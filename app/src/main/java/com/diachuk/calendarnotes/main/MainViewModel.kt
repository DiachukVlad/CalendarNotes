package com.diachuk.calendarnotes.main

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.diachuk.calendarnotes.AppState
import com.diachuk.calendarnotes.data.CheckList
import com.diachuk.calendarnotes.data.CheckListItem
import com.diachuk.calendarnotes.data.Styled
import com.diachuk.calendarnotes.data.note.Note
import com.diachuk.calendarnotes.data.note.NoteRepo
import com.diachuk.calendarnotes.note.NoteRoute
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import org.koin.android.annotation.KoinViewModel

@KoinViewModel
class MainViewModel(private val appState: AppState, private val noteRepo: NoteRepo) : ViewModel() {
    val notes = MutableStateFlow(listOf<Note>())

    fun onLaunch() {
        viewModelScope.launch(Dispatchers.IO) {
//            noteRepo.insertNote(
//                Note(
//                    date = 56465487L,
//                    components = listOf(
//                        Styled(text = "asdlkasd", styles = arrayListOf(1, 2, 3, 4)),
//                        Styled(text = "rty", styles = arrayListOf(1, 2, 3, 4)),
//                        CheckList(
//                            items = listOf(
//                                CheckListItem(checked = true, text = "qwe"),
//                                CheckListItem(checked = true, text = "dfg"),
//                                CheckListItem(checked = false, text = "asd")
//                            )
//                        ),
//                        Styled(text = "qwe", styles = arrayListOf(1, 2, 3, 4)),
//                    )
//                )
//            )
            println(noteRepo.notes)
//            notes.emit(noteRepo.notes)
        }
    }

    fun createNote() {
        appState.routing.push(NoteRoute(null))
    }

    fun noteClicked(index: Int) {
        appState.routing.push(NoteRoute(notes.value[index].id))
    }
}