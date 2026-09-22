package me.gblrod.mavira.features.game.difficulty.screen

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Save
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import me.gblrod.mavira.R
import me.gblrod.mavira.features.game.difficulty.model.DifficultyOptions
import me.gblrod.mavira.features.settings.components.DifficultyOptionCard
import me.gblrod.mavira.ui.theme.LightGreen

@Composable
fun DifficultyScreen(
    currentDifficulty: DifficultyOptions,
    onSaveDifficulty: (DifficultyOptions) -> Unit
) {
    var selectedDifficulty by remember(currentDifficulty) { mutableStateOf(currentDifficulty) }

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        item {
            Text(
                text = stringResource(id = R.string.difficulty_header),
                style = MaterialTheme.typography.titleMedium,
                color = MaterialTheme.colorScheme.onSurface
            )
        }

        item {
            Spacer(modifier = Modifier.height(24.dp))
        }

        items(
            items = DifficultyOptions.entries,
            key = { it.name }
        ) { difficulty ->
            DifficultyOptionCard(
                difficulty = difficulty,
                selected = selectedDifficulty == difficulty,
                onClick = {
                    selectedDifficulty = difficulty
                }
            )

            Spacer(modifier = Modifier.height(8.dp))
        }

        item {
            Spacer(modifier = Modifier.height(24.dp))
        }

        item {
            Button(
                onClick = { onSaveDifficulty(selectedDifficulty) },
                modifier = Modifier.fillMaxWidth(),
                colors = ButtonDefaults.buttonColors(containerColor = LightGreen)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(
                        space = 8.dp,
                        alignment = Alignment.CenterHorizontally
                    )
                ) {
                    Icon(
                        imageVector = Icons.Default.Save,
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.onSurface
                    )

                    Text(
                        text = stringResource(id = R.string.difficulty_save),
                        color = MaterialTheme.colorScheme.onSurface
                    )
                }
            }
        }
    }
}