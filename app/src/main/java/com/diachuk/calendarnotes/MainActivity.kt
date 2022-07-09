package com.diachuk.calendarnotes

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import com.diachuk.calendarnotes.ui.theme.CalendarNotesTheme
import com.diachuk.routing.RoutingHost
import org.koin.android.ext.android.get


class MainActivity : ComponentActivity() {
    private val appState: AppState = get()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            CalendarNotesTheme {
                RoutingHost(routing = appState.routing)
            }
        }
    }
}

