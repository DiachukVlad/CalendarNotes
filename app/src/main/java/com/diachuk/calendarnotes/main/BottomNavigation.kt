package com.diachuk.calendarnotes.main

import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.Phone
import androidx.compose.runtime.Composable

@Composable
fun CBottomNavigation() {
    BottomNavigation {
        BottomNavigationItem(
            selected = false,
            onClick = {},
            icon = { Icon(Icons.Default.Notifications, contentDescription = null) },
            label = { Text(text = "Global") }
        )
        BottomNavigationItem(
            selected = false,
            onClick = {},
            icon = { Icon(Icons.Default.Home, contentDescription = null) },
            label = { Text(text = "Today") }
        )
        BottomNavigationItem(
            selected = false,
            onClick = {},
            icon = { Icon(Icons.Default.Phone, contentDescription = null) },
            label = { Text(text = "Calendar") }
        )
    }
}