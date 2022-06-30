package com.diachuk.routing

import androidx.activity.compose.BackHandler
import androidx.compose.animation.*
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class Routing(start: Route) {
    private val backStack = ArrayList<Route>()

    private val _currentRoute = MutableStateFlow(start)
    val currentRoute = _currentRoute.asStateFlow()

    var forceExitTransition: ExitTransition? = null
    var forceEnterTransition: EnterTransition? = null

    val prevRoute get() = backStack.lastOrNull()
    val backStackSize get() = backStack.size

    enum class DoOnNext {
        Push, ChangeCurrent, PushWithoutRepeating
    }
//
//    constructor(start: Route, flowRoute: Flow<Route>, doOnNext: DoOnNext) : this(start) {
//        CoroutineScope(Dispatchers.Default).launch {
//            flowRoute.collect { screen ->
//                when (doOnNext) {
//                    DoOnNext.Push -> push(screen)
//                    DoOnNext.ChangeCurrent -> changeCurrent(screen)
//                    DoOnNext.PushWithoutRepeating -> pushWithoutRepeating(screen)
//                }
//            }
//        }
//    }

    fun changeCurrent(
        nextRoute: Route,
        forceExitTransition: ExitTransition? = null,
        forceEnterTransition: EnterTransition? = null
    ) {
        this.forceEnterTransition = forceEnterTransition
        this.forceExitTransition = forceExitTransition

        _currentRoute.value = nextRoute
    }

    fun push(
        nextRoute: Route,
        forceExitTransition: ExitTransition? = null,
        forceEnterTransition: EnterTransition? = null
    ) {
        backStack.add(_currentRoute.value)
        changeCurrent(nextRoute, forceExitTransition, forceEnterTransition)
    }

    fun pushWithoutRepeating(
        nextRoute: Route,
        forceExitTransition: ExitTransition? = null,
        forceEnterTransition: EnterTransition? = null
    ) {
        if (_currentRoute.value.javaClass.name == nextRoute.javaClass.name) return

        backStack.removeIf { it.javaClass.name == nextRoute.javaClass.name }
        backStack.add(_currentRoute.value)
        changeCurrent(nextRoute, forceExitTransition, forceEnterTransition)
    }

    fun pop() {
        prevRoute?.let {
            changeCurrent(it)
            backStack.removeLast()
        }
    }

    fun clearBackStack() {
        backStack.clear()
    }
}

object EmptyRoute : Route() {
    @Composable
    override fun Content() {
        Box(
            Modifier
                .fillMaxSize()
        ) {
        }
    }
}

val LocalRoutingComposition = compositionLocalOf { Routing(EmptyRoute) }
val LocalRouting @Composable get() = LocalRoutingComposition.current
val LocalRoute @Composable get() = LocalRoutingComposition.current.currentRoute
val LocalPrevRoute @Composable get() = LocalRoutingComposition.current.prevRoute
val LocalBackStackSize @Composable get() = LocalRoutingComposition.current.backStackSize

@OptIn(ExperimentalAnimationApi::class)
@Composable
fun RoutingHost(
    modifier: Modifier = Modifier,
    routing: Routing = remember { Routing(EmptyRoute) }
) {

    CompositionLocalProvider(LocalRoutingComposition provides routing) {
        val currentRouting = LocalRouting
        BackHandler(LocalBackStackSize > 0) {
            currentRouting.pop()
        }
        val targetState = currentRouting.currentRoute.collectAsState()

        AnimatedContent(
            targetState = targetState.value,
            modifier = modifier,
            transitionSpec = {
                (currentRouting.forceEnterTransition ?: targetState.value.enterTransition) with
                        (currentRouting.forceExitTransition ?: initialState.exitTransition)
            }

        ) { route ->
            route.Content()
        }
    }
}