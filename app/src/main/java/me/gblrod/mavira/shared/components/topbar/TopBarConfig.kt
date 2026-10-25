package me.gblrod.mavira.shared.components.topbar

import androidx.navigation.NavDestination
import androidx.navigation.NavDestination.Companion.hasRoute
import me.gblrod.mavira.R
import me.gblrod.mavira.features.game.config.HintConfig
import me.gblrod.mavira.navigation.routes.Route

data class TopBarConfig(
    val title: Int,
    val showHint: Boolean = false,
    val showRestartGame: Boolean = false,
    val showMusicBackground: Boolean = false
)

data class TopBarState(
    val isRestartEnabled: Boolean = false,
    val onRestartGame: () -> Unit = {},
    val hintsRemaining: Int = HintConfig.INITIAL_HINTS,
    val isHintEnabled: Boolean = false,
    val onHintGame: () -> Unit = {}
)

fun NavDestination?.topBarConfig(): TopBarConfig? {
    return when {
        this?.hasRoute<Route.Game>() == true -> {
            TopBarConfig(
                title = R.string.route_game,
                showHint = true,
                showRestartGame = true,
                showMusicBackground = true
            )
        }

        this?.hasRoute<Route.Stats>() == true -> {
            TopBarConfig(
                title = R.string.route_statistics
            )
        }

        this?.hasRoute<Route.Config>() == true -> {
            TopBarConfig(
                title = R.string.route_settings
            )
        }

        this?.hasRoute<Route.Difficulty>() == true -> {
            TopBarConfig(
                title = R.string.route_difficulty
            )
        }

        this?.hasRoute<Route.Challenge>() == true -> {
            TopBarConfig(
                title = R.string.route_challenge,
                showMusicBackground = true
            )
        }

        else -> null
    }
}