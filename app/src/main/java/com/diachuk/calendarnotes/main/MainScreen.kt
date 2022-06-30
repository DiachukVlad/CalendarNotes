package com.diachuk.calendarnotes.main


import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.graphics.Color
import com.diachuk.calendarnotes.AppState
import com.diachuk.routing.Route
import org.koin.androidx.compose.get
import org.koin.androidx.compose.getViewModel

object MainRoute : Route() {
    @Composable
    override fun Content() {
        MainScreen()
    }
}

@Composable
fun MainScreen(appState: AppState = get(), vm: MainViewModel = getViewModel()) {
    val currentRoute: Route by appState.routing.currentRoute.collectAsState()

    Scaffold(bottomBar = {
        BottomNavigation {
            BottomNavigationItem(
                selected = false,
                onClick = {},
                icon = { Icon(Icons.Default.StickyNote2, contentDescription = null) },
                label = { Text(text = "Global") }
            )
            BottomNavigationItem(
                selected = false,
                onClick = {},
                icon = { Icon(Icons.Default.CalendarToday, contentDescription = null) },
                label = { Text(text = "Today") }
            )
            BottomNavigationItem(
                selected = false,
                onClick = {},
                icon = { Icon(Icons.Default.CalendarToday, contentDescription = null) },
                label = { Text(text = "Calendar") }
            )
        }
    },
        floatingActionButton = {
            FloatingActionButton(onClick = { vm.createNote() }) {
                Icon(imageVector = Icons.Default.Add, contentDescription = null)
            }
        }) {

    }
}