package me.gblrod.mavira.app

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import me.gblrod.mavira.navigation.graph.NavigationGraph
import me.gblrod.mavira.shared.components.topbar.MaviraAppBar
import me.gblrod.mavira.shared.components.topbar.TopBarState
import me.gblrod.mavira.shared.components.topbar.topBarConfig

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MaviraApp() {
    val navHostController = rememberNavController()

    val backStackEntry by navHostController.currentBackStackEntryAsState()
    val destination = backStackEntry?.destination
    val topBarConfig = destination.topBarConfig()

    var topBarState by remember { mutableStateOf(TopBarState()) }

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        containerColor = MaterialTheme.colorScheme.background,
        topBar = {
            topBarConfig?.let { config ->
                MaviraAppBar(
                    config = config,
                    onBackClick = { navHostController.popBackStack() },
                    timer = topBarState.timer
                )
            }
        }
    ) { paddingValues ->
        NavigationGraph(
            navHostController = navHostController,
            paddingValues = paddingValues,
            onTopBarStateChange = { topBarState = it }
        )
    }
}