package com.diachuk.routing

import androidx.compose.animation.EnterTransition
import androidx.compose.animation.ExitTransition
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.runtime.Composable

abstract class Route(
    val enterTransition: EnterTransition = fadeIn(),
    val exitTransition: ExitTransition = fadeOut(),
) {
    @Composable
    abstract fun Content()
}