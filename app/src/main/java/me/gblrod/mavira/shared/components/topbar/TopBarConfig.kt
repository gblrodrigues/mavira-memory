package me.gblrod.mavira.shared.components.topbar

import androidx.navigation.NavDestination
import androidx.navigation.NavDestination.Companion.hasRoute
import me.gblrod.mavira.R
import me.gblrod.mavira.navigation.routes.Route

data class TopBarConfig(
    val title: Int
)

fun NavDestination?.topBarConfig(): TopBarConfig? {
    return when {
        this?.hasRoute<Route.Game>() == true -> {
            TopBarConfig(
                title = R.string.route_game
            )
        }

        else -> null
    }
}