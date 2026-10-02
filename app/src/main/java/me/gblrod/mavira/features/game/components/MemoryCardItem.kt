package me.gblrod.mavira.features.game.components

import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.InfiniteTransition
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.QuestionMark
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import me.gblrod.mavira.features.game.model.MemoryCard
import me.gblrod.mavira.ui.theme.Yellow

@Composable
fun MemoryCardItem(
    card: MemoryCard,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val rotation by animateFloatAsState(
        targetValue = if (card.isRevealed || card.isMatched) 180f else 0f
    )

    val infiniteTransition = rememberInfiniteTransition()
    val glowAlpha by infiniteTransition.animateGlow(
        initialValue = 0.4f,
        targetValue = 1.0f
    )
    val glowScale by infiniteTransition.animateGlow(
        initialValue = 1.0f,
        targetValue = 1.05f
    )

    val borderStroke = if (card.isGlowing) {
        BorderStroke(
            width = 2.dp,
            color = Yellow.copy(alpha = glowAlpha)
        )
    } else null

    Card(
        modifier = modifier
            .graphicsLayer {
                rotationY = rotation
                cameraDistance = 8 * density
                if (card.isGlowing) {
                    scaleX = glowScale
                    scaleY = glowScale
                }
            }
            .clickable(
                enabled = !card.isMatched,
                onClick = { onClick() }
            ),
        border = borderStroke,
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(
            containerColor = when {
                card.isGlowing && !card.isRevealed -> Yellow.copy(alpha = 0.25f * glowAlpha)
                card.isRevealed || card.isMatched -> MaterialTheme.colorScheme.surface
                else -> MaterialTheme.colorScheme.primary
            }
        )
    ) {
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            if (rotation <= 90f) {
                Icon(
                    imageVector = Icons.Default.QuestionMark,
                    contentDescription = null
                )
            } else {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .graphicsLayer {
                            rotationY = 180f
                        },
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        painter = painterResource(id = card.symbol.icon),
                        contentDescription = null,
                        modifier = Modifier.size(40.dp)
                    )
                }
            }
        }
    }
}

@Composable
private fun InfiniteTransition.animateGlow(
    initialValue: Float,
    targetValue: Float
): State<Float> {
    return animateFloat(
        initialValue = initialValue,
        targetValue = targetValue,
        animationSpec = infiniteRepeatable(
            animation = tween(
                durationMillis = 400,
                easing = FastOutSlowInEasing
            ),
            repeatMode = RepeatMode.Reverse
        )
    )
}