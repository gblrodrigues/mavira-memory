package me.gblrod.mavira.features.home.screen

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.GridView
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import me.gblrod.mavira.R
import me.gblrod.mavira.features.home.components.HomeMenu
import me.gblrod.mavira.features.home.model.ButtonItems
import me.gblrod.mavira.features.home.viewmodel.HomeViewModel

@Composable
fun HomeScreen(
    homeViewModel: HomeViewModel,
    onNavigatePlay: () -> Unit,
    onNavigateStats: () -> Unit,
    onNavigateConfig: () -> Unit,
    onNavigateChallenge: () -> Unit
) {
    val uiState by homeViewModel.uiState.collectAsStateWithLifecycle()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Top
    ) {
        Spacer(modifier = Modifier.height(48.dp))

        Icon(
            imageVector = Icons.Default.GridView,
            contentDescription = null,
            modifier = Modifier.size(70.dp)
        )

        Spacer(modifier = Modifier.height(8.dp))

        Text(
            text = stringResource(id = R.string.home_app_name),
            style = MaterialTheme.typography.headlineMedium,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.onSurface
        )

        Spacer(modifier = Modifier.height(8.dp))

        Text(
            text = stringResource(id = R.string.home_app_description_one),
            style = MaterialTheme.typography.titleSmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )

        Text(
            text = stringResource(id = R.string.home_app_description_two),
            style = MaterialTheme.typography.titleSmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )

        Spacer(modifier = Modifier.height(24.dp))

        HomeMenu(
            timeUntilAvailable = uiState.timeUntilChallenge,
            isChallengeAvailable = uiState.isChallengeAvailable,
            onItemClick = { item ->
                when (item) {
                    ButtonItems.PLAY -> onNavigatePlay()
                    ButtonItems.CHALLENGE -> onNavigateChallenge()
                    ButtonItems.STATS -> onNavigateStats()
                    ButtonItems.CONFIG -> onNavigateConfig()
                }
            }
        )
    }
}