package com.speakup.app

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.unit.dp

/** Consistent, scalable bottom tab icons matching the approved English Mint visual system.
 * Pure Compose vectors avoid emoji/font-specific rendering and require no external icon assets.
 */
@Composable
internal fun MintBottomTabIcon(tab: String, tint: Color) {
    Canvas(Modifier.size(25.dp)) {
        val w = size.width
        val h = size.height
        val stroke = Stroke(width = 2.3.dp.toPx(), cap = StrokeCap.Round, join = StrokeJoin.Round)
        when (tab) {
            "Today" -> {
                val roof = Path().apply {
                    moveTo(w * .12f, h * .44f)
                    lineTo(w * .50f, h * .11f)
                    lineTo(w * .88f, h * .44f)
                }
                drawPath(roof, tint, style = stroke)
                val house = Path().apply {
                    moveTo(w * .21f, h * .43f)
                    lineTo(w * .21f, h * .88f)
                    lineTo(w * .79f, h * .88f)
                    lineTo(w * .79f, h * .43f)
                }
                drawPath(house, tint, style = stroke)
                drawLine(tint, androidx.compose.ui.geometry.Offset(w * .43f, h * .88f),
                    androidx.compose.ui.geometry.Offset(w * .43f, h * .64f),
                    strokeWidth = 2.3.dp.toPx(), cap = StrokeCap.Round)
                drawLine(tint, androidx.compose.ui.geometry.Offset(w * .43f, h * .64f),
                    androidx.compose.ui.geometry.Offset(w * .57f, h * .64f),
                    strokeWidth = 2.3.dp.toPx(), cap = StrokeCap.Round)
            }
            "Games" -> {
                val controller = Path().apply {
                    moveTo(w * .22f,h * .33f)
                    cubicTo(w * .12f,h * .40f,w * .06f,h * .78f,w * .20f,h * .82f)
                    cubicTo(w * .33f,h * .85f,w * .38f,h * .69f,w * .50f,h * .69f)
                    cubicTo(w * .62f,h * .69f,w * .67f,h * .85f,w * .80f,h * .82f)
                    cubicTo(w * .94f,h * .78f,w * .88f,h * .40f,w * .78f,h * .33f)
                    cubicTo(w * .65f,h * .23f,w * .35f,h * .23f,w * .22f,h * .33f)
                    close()
                }
                drawPath(controller,tint,style=stroke)
                drawLine(tint,androidx.compose.ui.geometry.Offset(w*.30f,h*.43f),
                    androidx.compose.ui.geometry.Offset(w*.30f,h*.61f),strokeWidth=2.dp.toPx(),cap=StrokeCap.Round)
                drawLine(tint,androidx.compose.ui.geometry.Offset(w*.22f,h*.52f),
                    androidx.compose.ui.geometry.Offset(w*.38f,h*.52f),strokeWidth=2.dp.toPx(),cap=StrokeCap.Round)
                drawCircle(tint,radius=w*.043f,center=androidx.compose.ui.geometry.Offset(w*.70f,h*.44f))
                drawCircle(tint,radius=w*.043f,center=androidx.compose.ui.geometry.Offset(w*.61f,h*.55f))
            }
            "Feed" -> {
                drawRoundRect(color=tint,
                    topLeft=androidx.compose.ui.geometry.Offset(w*.12f,h*.18f),
                    size=androidx.compose.ui.geometry.Size(w*.76f,h*.64f),
                    cornerRadius=androidx.compose.ui.geometry.CornerRadius(w*.09f),
                    style=stroke)
                val play = Path().apply {
                    moveTo(w*.40f,h*.34f)
                    lineTo(w*.66f,h*.50f)
                    lineTo(w*.40f,h*.66f)
                    close()
                }
                drawPath(play,tint,style=stroke)
            }
            "Speak" -> {
                drawRoundRect(color=tint,
                    topLeft=androidx.compose.ui.geometry.Offset(w*.37f,h*.12f),
                    size=androidx.compose.ui.geometry.Size(w*.26f,h*.47f),
                    cornerRadius=androidx.compose.ui.geometry.CornerRadius(w*.13f),
                    style=stroke)
                val cradle=Path().apply {
                    moveTo(w*.22f,h*.50f)
                    cubicTo(w*.22f,h*.88f,w*.78f,h*.88f,w*.78f,h*.50f)
                }
                drawPath(cradle,tint,style=stroke)
                drawLine(tint,androidx.compose.ui.geometry.Offset(w*.50f,h*.76f),
                    androidx.compose.ui.geometry.Offset(w*.50f,h*.91f),strokeWidth=2.3.dp.toPx(),cap=StrokeCap.Round)
                drawLine(tint,androidx.compose.ui.geometry.Offset(w*.36f,h*.91f),
                    androidx.compose.ui.geometry.Offset(w*.64f,h*.91f),strokeWidth=2.3.dp.toPx(),cap=StrokeCap.Round)
            }
            else -> {
                drawCircle(color=tint,radius=w*.16f,
                    center=androidx.compose.ui.geometry.Offset(w*.50f,h*.29f),style=stroke)
                val shoulder=Path().apply {
                    moveTo(w*.17f,h*.85f)
                    cubicTo(w*.18f,h*.53f,w*.82f,h*.53f,w*.83f,h*.85f)
                    close()
                }
                drawPath(shoulder,tint,style=stroke)
            }
        }
    }
}
