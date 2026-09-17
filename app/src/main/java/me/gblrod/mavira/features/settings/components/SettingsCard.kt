package me.gblrod.mavira.features.settings.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
import me.gblrod.mavira.features.settings.model.SettingsItemData

@Composable
fun SettingsCard(
    icon: ImageVector,
    title: String,
    items: List<SettingsItemData>
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceContainerLow),
        border = BorderStroke(
            width = 1.dp,
            color = MaterialTheme.colorScheme.outlineVariant
        )
    ) {
        Column {
            SettingsSectionHeader(
                title = title,
                icon = icon
            )

            HorizontalDivider()

            items.forEachIndexed { index, item ->
                SettingsItem(
                    title = item.title,
                    description = item.description,
                    icon = item.icon,
                    onClick = item.onClick,
                    trailingContent = item.trailingContent
                )

                if (index < items.lastIndex) {
                    HorizontalDivider()
                }
            }
        }
    }
}