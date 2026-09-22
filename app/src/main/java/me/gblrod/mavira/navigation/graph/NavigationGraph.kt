package me.gblrod.mavira.navigation.graph

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import me.gblrod.mavira.features.game.difficulty.screen.DifficultyScreen
import me.gblrod.mavira.features.game.screen.GameScreen
import me.gblrod.mavira.features.game.viewmodel.GameViewModel
import me.gblrod.mavira.features.home.screen.HomeScreen
import me.gblrod.mavira.features.settings.screen.SettingsScreen
import me.gblrod.mavira.features.settings.viewmodel.SettingsViewModel
import me.gblrod.mavira.features.statistics.screen.StatisticsScreen
import me.gblrod.mavira.features.statistics.viewmodel.StatisticsViewModel
import me.gblrod.mavira.navigation.routes.Route
import me.gblrod.mavira.shared.components.topbar.TopBarState

@Composable
fun NavigationGraph(
    paddingValues: PaddingValues,
    navHostController: NavHostController,
    onTopBarStateChange: (TopBarState) -> Unit
) {
    NavHost(
        navController = navHostController,
        startDestination = Route.Home,
        modifier = Modifier.padding(paddingValues = paddingValues)
    ) {
        composable<Route.Home> {
            LaunchedEffect(Unit) {
                onTopBarStateChange(TopBarState())
            }

            HomeScreen(
                onNavigatePlay = { navHostController.navigate(Route.Game) },
                onNavigateStats = { navHostController.navigate(Route.Stats) },
                onNavigateConfig = { navHostController.navigate(Route.Config) },
            )
        }

        composable<Route.Game> {
            val gameViewModel: GameViewModel = hiltViewModel()
            val uiState by gameViewModel.uiState.collectAsStateWithLifecycle()

            LaunchedEffect(uiState.timer) {
                onTopBarStateChange(
                    TopBarState(
                        timer = uiState.timer
                    )
                )
            }

            GameScreen(
                gameViewModel = gameViewModel,
                onNavigateHome = { navHostController.popBackStack() }
            )
        }

        composable<Route.Stats> {
            val statisticsViewModel: StatisticsViewModel = hiltViewModel()

            StatisticsScreen(
                statisticsViewModel = statisticsViewModel,
                onNavigateGameScreen = { navHostController.navigate(Route.Game) }
            )
        }

        composable<Route.Config> {
            val settingsViewModel: SettingsViewModel = hiltViewModel()

            SettingsScreen(
                settingsViewModel = settingsViewModel,
                onNavigateDifficulty = { navHostController.navigate(Route.Difficulty) }
            )
        }

        composable<Route.Difficulty> {
            val settingsViewModel: SettingsViewModel = hiltViewModel()
            val uiState by settingsViewModel.uiState.collectAsStateWithLifecycle()
            DifficultyScreen(
                currentDifficulty = uiState.difficultyOptions,
                onSaveDifficulty = { difficulty ->
                    settingsViewModel.setDifficulty(difficulty = difficulty)
                    navHostController.popBackStack()
                }
            )
        }
    }
}