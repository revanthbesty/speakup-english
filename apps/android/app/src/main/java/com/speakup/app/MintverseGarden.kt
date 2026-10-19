package com.speakup.app

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp

/** Display-only, offline Mintverse scene derived from already-earned lesson completions. */
internal object MintverseGrowth {
    fun stage(completed: Int): Int = when {
        completed <= 0 -> 0
        completed < 3 -> 1
        completed < 7 -> 2
        completed < 14 -> 3
        else -> 4
    }
    fun description(stage: Int): String = when (stage) {
        0 -> "Your learning garden is ready for its first seed."
        1 -> "A new sprout is growing with every lesson."
        2 -> "Your learning garden is starting to bloom."
        3 -> "Your progress is making the whole garden flourish."
        else -> "Your learning garden is glowing with growth."
    }
}

@Composable
internal fun MintverseGarden(completedLessons: Int, reducedMotion: Boolean) {
    val stage = MintverseGrowth.stage(completedLessons)
    val bloom = animateFloatAsState(
        targetValue = stage.toFloat(),
        animationSpec = tween(durationMillis = if (reducedMotion) 0 else 420),
        label = "mintverse-growth"
    )
    Surface(
        modifier = Modifier.fillMaxWidth(),
        color = MaterialTheme.colorScheme.primaryContainer,
        shape = RoundedCornerShape(24.dp)
    ) {
        Column(Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
            Text("Your Mintverse garden", style = MaterialTheme.typography.titleMedium,
                color = MaterialTheme.colorScheme.onPrimaryContainer,
                fontWeight = FontWeight.Bold)
            Canvas(Modifier.fillMaxWidth().height(110.dp)) {
                val center = size.width / 2f
                val baseline = size.height * .83f
                drawLine(Color(0xFF047857), Offset(size.width * .10f, baseline),
                    Offset(size.width * .90f, baseline), strokeWidth = 6.dp.toPx(), cap = StrokeCap.Round)
                val branches = bloom.value
                drawLine(Color(0xFF059669), Offset(center, baseline),
                    Offset(center, baseline - (35f + branches * 9f).dp.toPx()),
                    strokeWidth = 7.dp.toPx(), cap = StrokeCap.Round)
                val leafCount = 2 + (branches.toInt() * 2)
                for (i in 0 until leafCount) {
                    val direction = if (i % 2 == 0) -1f else 1f
                    val y = baseline - (18f + i * 8f).dp.toPx()
                    drawCircle(Color(0xFF10B981),
                        radius = (9f + branches).dp.toPx(),
                        center = Offset(center + direction * 18.dp.toPx(), y))
                }
                if (branches >= 2f) {
                    drawCircle(Color(0xFFF59E0B), radius = 11.dp.toPx(),
                        center = Offset(center, baseline - (47f + branches * 9f).dp.toPx()))
                }
                if (branches >= 4f) {
                    drawCircle(Color(0xFF8B5CF6), radius = 6.dp.toPx(),
                        center = Offset(center + 42.dp.toPx(), baseline - 49.dp.toPx()))
                }
            }
            Text(MintverseGrowth.description(stage),
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onPrimaryContainer)
            Text("$completedLessons lessons completed • Your garden grows with real learning",
                style = MaterialTheme.typography.labelMedium,
                color = MaterialTheme.colorScheme.onPrimaryContainer)
        }
    }
}
