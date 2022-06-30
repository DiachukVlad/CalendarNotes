package com.diachuk.calendarnotes

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import com.diachuk.calendarnotes.ui.theme.CalendarNotesTheme
import com.diachuk.routing.RoutingHost
import org.koin.android.ext.android.get
import org.koin.android.scope.AndroidScopeComponent
import org.koin.androidx.scope.activityRetainedScope
import org.koin.core.scope.Scope


class MainActivity : ComponentActivity(), AndroidScopeComponent {
    override val scope: Scope by activityRetainedScope()
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

