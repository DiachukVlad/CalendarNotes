package com.diachuk.calendarnotes

import com.diachuk.calendarnotes.main.MainRoute
import com.diachuk.routing.Routing
import org.koin.core.annotation.Single

@Single
class AppState {
    val routing = Routing(MainRoute)
}