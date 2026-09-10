package me.gblrod.mavira.navigation.graph

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import me.gblrod.mavira.features.game.screen.GameScreen
import me.gblrod.mavira.features.home.screen.HomeScreen
import me.gblrod.mavira.navigation.routes.Route

@Composable
fun NavigationGraph(
    paddingValues: PaddingValues,
    navHostController: NavHostController
) {
    NavHost(
        navController = navHostController,
        startDestination = Route.Home,
        modifier = Modifier.padding(paddingValues = paddingValues)
    ) {
        composable<Route.Home> {
            HomeScreen(
                onNavigatePlay = { navHostController.navigate(Route.Game) },
                onNavigateStats = { navHostController.navigate(Route.Stats) },
                onNavigateConfig = { navHostController.navigate(Route.Config) },
            )
        }

        composable<Route.Game> {
            GameScreen(
                onNavigateHome = { navHostController.popBackStack() }
            )
        }
    }
}