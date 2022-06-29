package com.diachuk.calendarnotes

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import com.diachuk.calendarnotes.note.NoteScreen
import com.diachuk.calendarnotes.ui.theme.CalendarNotesTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            CalendarNotesTheme {
                NoteScreen()
            }
        }
    }
}

