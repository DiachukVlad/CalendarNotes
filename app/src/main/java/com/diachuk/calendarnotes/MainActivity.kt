package com.diachuk.calendarnotes

import android.os.Bundle
import android.view.KeyEvent
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Surface
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.diachuk.calendarnotes.main.MainScreen
import com.diachuk.calendarnotes.styledText.StyledTextField
import com.diachuk.calendarnotes.ui.theme.CalendarNotesTheme
import com.diachuk.routing.RoutingHost
import org.koin.android.ext.android.get
import org.koin.android.scope.AndroidScopeComponent
import org.koin.androidx.scope.activityRetainedScope
import org.koin.core.scope.Scope


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

    override fun onKeyDown(keyCode: Int, event: KeyEvent?): Boolean {
        println(keyCode)
        println(event)
        return super.onKeyDown(keyCode, event)
    }
}

