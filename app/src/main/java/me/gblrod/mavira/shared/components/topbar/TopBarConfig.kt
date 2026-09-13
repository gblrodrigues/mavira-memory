package me.gblrod.mavira.shared.components.topbar

import androidx.navigation.NavDestination
import androidx.navigation.NavDestination.Companion.hasRoute
import me.gblrod.mavira.R
import me.gblrod.mavira.navigation.routes.Route

data class TopBarConfig(
    val title: Int,
    val showTimer: Boolean = false
)

data class TopBarState(
    val timer: Long = 0L
)

fun NavDestination?.topBarConfig(): TopBarConfig? {
    return when {
        this?.hasRoute<Route.Game>() == true -> {
            TopBarConfig(
                title = R.string.route_game,
                showTimer = true
            )
        }

        this?.hasRoute<Route.Stats>() == true -> {
            TopBarConfig(
                title = R.string.route_statistics
            )
        }

        else -> null
    }
}