package me.gblrod.mavira.features.game.difficulty.model

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Bolt
import androidx.compose.material.icons.filled.EmojiEvents
import androidx.compose.material.icons.filled.LocalFireDepartment
import androidx.compose.material.icons.filled.SentimentVerySatisfied
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import me.gblrod.mavira.R
import me.gblrod.mavira.ui.theme.LightGreen
import me.gblrod.mavira.ui.theme.LightRed
import me.gblrod.mavira.ui.theme.PurpleStyle
import me.gblrod.mavira.ui.theme.Yellow

enum class DifficultyOptions(
    val label: Int,
    val icon: ImageVector,
    val color: Color,
    val description: Int,
    val columns: Int,
    val pairs: Int
) {
    EASY(
        label = R.string.difficulty_easy_label,
        icon = Icons.Default.SentimentVerySatisfied,
        description = R.string.difficulty_easy_description,
        columns = 4,
        pairs = 6,
        color = LightGreen
    ),
    MEDIUM(
        label = R.string.difficulty_medium_label,
        icon = Icons.Default.Bolt,
        description = R.string.difficulty_medium_description,
        columns = 4,
        pairs = 8,
        color = Yellow
    ),
    HARD(
        label = R.string.difficulty_hard_label,
        icon = Icons.Default.LocalFireDepartment,
        description = R.string.difficulty_hard_description,
        columns = 4,
        pairs = 12,
        color = LightRed
    ),
    EXPERT(
        label = R.string.difficulty_expert_label,
        icon = Icons.Default.EmojiEvents,
        description = R.string.difficulty_expert_description,
        columns = 6,
        pairs = 18,
        color = PurpleStyle
    )
}