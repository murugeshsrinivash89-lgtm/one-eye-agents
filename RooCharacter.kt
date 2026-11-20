/*
 * ONE EYE CORE v1.0
 * RooCharacter.kt
 *
 * DEMO IMPLEMENTATION
 *
 * A lightweight, path-based character renderer for the four ONE EYE CORE
 * agents. The character is drawn with Jetpack Compose Canvas rather than
 * loading image assets.
 *
 * This is a visual demo. It does not include a full animation engine,
 * production character rig, or live connection to BrainManager.
 */

package com.oneeyecore.demo

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

/**
 * Emotional states that can be displayed by an agent character.
 */
enum class CharacterEmotion {
    NEUTRAL,
    THINKING,
    HAPPY,
    DISAPPOINTED,
    EXCITED
}

/**
 * Draws a simple vector-style agent character.
 *
 * Example:
 * RooCharacter(
 *     agent = AgentType.ROO,
 *     emotion = CharacterEmotion.HAPPY
 * )
 */
@Composable
fun RooCharacter(
    agent: AgentType,
    emotion: CharacterEmotion = CharacterEmotion.NEUTRAL,
    modifier: Modifier = Modifier,
    characterSize: Dp = 120.dp
) {
    Box(
        modifier = modifier.size(characterSize),
        contentAlignment = Alignment.Center
    ) {
        Canvas(modifier = Modifier.size(characterSize)) {
            val center = Offset(size.width / 2f, size.height / 2f)
            val unit = size.minDimension

            val mainColor = agentColor(agent)
            val darkColor = Color(0xFF111522)
            val eyeColor = Color(0xFFF7F8FC)
            val accentColor = Color(0xFFFFC857)

            // Soft outer glow / background disc.
            drawCircle(
                color = mainColor.copy(alpha = 0.16f),
                radius = unit * 0.47f,
                center = center
            )

            // Character silhouette: rounded geometric body.
            val bodyPath = Path().apply {
                moveTo(center.x, center.y - unit * 0.39f)
                cubicTo(
                    center.x + unit * 0.34f, center.y - unit * 0.39f,
                    center.x + unit * 0.40f, center.y - unit * 0.08f,
                    center.x + unit * 0.34f, center.y + unit * 0.20f
                )
                cubicTo(
                    center.x + unit * 0.28f, center.y + unit * 0.43f,
                    center.x - unit * 0.28f, center.y + unit * 0.43f,
                    center.x - unit * 0.34f, center.y + unit * 0.20f
                )
                cubicTo(
                    center.x - unit * 0.40f, center.y - unit * 0.08f,
                    center.x - unit * 0.34f, center.y - unit * 0.39f,
                    center.x, center.y - unit * 0.39f
                )
                close()
            }

            drawPath(path = bodyPath, color = mainColor)
            drawPath(
                path = bodyPath,
                color = Color.White.copy(alpha = 0.55f),
                style = Stroke(width = unit * 0.018f)
            )

            // Small agent-specific accent mark.
            when (agent) {
                AgentType.ADA -> {
                    val mark = Path().apply {
                        moveTo(center.x, center.y - unit * 0.48f)
                        lineTo(center.x + unit * 0.10f, center.y - unit * 0.37f)
                        lineTo(center.x, center.y - unit * 0.26f)
                        lineTo(center.x - unit * 0.10f, center.y - unit * 0.37f)
                        close()
                    }
                    drawPath(mark, accentColor)
                }

                AgentType.ANA -> {
                    drawCircle(
                        color = Color(0xFFFFD5E5),
                        radius = unit * 0.055f,
                        center = Offset(center.x, center.y - unit * 0.39f)
                    )
                }

                AgentType.RIVER -> {
                    val wave = Path().apply {
                        moveTo(center.x - unit * 0.12f, center.y - unit * 0.39f)
                        quadraticTo(
                            center.x, center.y - unit * 0.49f,
                            center.x + unit * 0.12f, center.y - unit * 0.39f
                        )
                    }
                    drawPath(
                        path = wave,
                        color = Color(0xFFB9F2F0),
                        style = Stroke(width = unit * 0.035f, cap = StrokeCap.Round)
                    )
                }

                AgentType.ROO -> {
                    drawCircle(
                        color = accentColor,
                        radius = unit * 0.045f,
                        center = Offset(center.x, center.y - unit * 0.40f)
                    )
                }
            }

            // Eyes.
            val eyeY = center.y - unit * 0.035f
            val eyeOffset = unit * 0.12f
            val eyeRadius = unit * 0.045f

            val eyesClosed = emotion == CharacterEmotion.DISAPPOINTED
            if (eyesClosed) {
                drawLine(
                    color = darkColor,
                    start = Offset(center.x - eyeOffset - eyeRadius, eyeY),
                    end = Offset(center.x - eyeOffset + eyeRadius, eyeY),
                    strokeWidth = unit * 0.025f,
                    cap = StrokeCap.Round
                )
                drawLine(
                    color = darkColor,
                    start = Offset(center.x + eyeOffset - eyeRadius, eyeY),
                    end = Offset(center.x + eyeOffset + eyeRadius, eyeY),
                    strokeWidth = unit * 0.025f,
                    cap = StrokeCap.Round
                )
            } else {
                drawCircle(eyeColor, eyeRadius, Offset(center.x - eyeOffset, eyeY))
                drawCircle(eyeColor, eyeRadius, Offset(center.x + eyeOffset, eyeY))
                drawCircle(
                    darkColor,
                    eyeRadius * 0.45f,
                    Offset(center.x - eyeOffset, eyeY)
                )
                drawCircle(
                    darkColor,
                    eyeRadius * 0.45f,
                    Offset(center.x + eyeOffset, eyeY)
                )
            }

            // Mouth expression.
            val mouth = Path().apply {
                when (emotion) {
                    CharacterEmotion.HAPPY, CharacterEmotion.EXCITED -> {
                        moveTo(center.x - unit * 0.10f, center.y + unit * 0.12f)
                        quadraticTo(
                            center.x, center.y + unit * 0.25f,
                            center.x + unit * 0.10f, center.y + unit * 0.12f
                        )
                    }

                    CharacterEmotion.DISAPPOINTED -> {
                        moveTo(center.x - unit * 0.09f, center.y + unit * 0.23f)
                        quadraticTo(
                            center.x, center.y + unit * 0.13f,
                            center.x + unit * 0.09f, center.y + unit * 0.23f
                        )
                    }

                    CharacterEmotion.THINKING -> {
                        moveTo(center.x - unit * 0.045f, center.y + unit * 0.17f)
                        lineTo(center.x + unit * 0.055f, center.y + unit * 0.17f)
                    }

                    CharacterEmotion.NEUTRAL -> {
                        moveTo(center.x - unit * 0.07f, center.y + unit * 0.17f)
                        lineTo(center.x + unit * 0.07f, center.y + unit * 0.17f)
                    }
                }
            }

            drawPath(
                path = mouth,
                color = darkColor,
                style = Stroke(
                    width = unit * 0.025f,
                    cap = StrokeCap.Round
                )
            )

            // Thinking indicator: three small dots above the character.
            if (emotion == CharacterEmotion.THINKING) {
                val dotY = center.y - unit * 0.52f
                for (index in 0..2) {
                    drawCircle(
                        color = accentColor,
                        radius = unit * 0.025f,
                        center = Offset(
                            center.x + (index - 1) * unit * 0.09f,
                            dotY
                        )
                    )
                }
            }

            // Excited indicator: small accent rays.
            if (emotion == CharacterEmotion.EXCITED) {
                val rayStart = unit * 0.40f
                val rayEnd = unit * 0.47f
                drawLine(
                    accentColor,
                    Offset(center.x - rayStart, center.y - rayStart),
                    Offset(center.x - rayEnd, center.y - rayEnd),
                    strokeWidth = unit * 0.025f,
                    cap = StrokeCap.Round
                )
                drawLine(
                    accentColor,
                    Offset(center.x + rayStart, center.y - rayStart),
                    Offset(center.x + rayEnd, center.y - rayEnd),
                    strokeWidth = unit * 0.025f,
                    cap = StrokeCap.Round
                )
            }
        }
    }
}

private fun agentColor(agent: AgentType): Color {
    return when (agent) {
        AgentType.ADA -> Color(0xFF6C8CFF)   // Logic blue
        AgentType.ANA -> Color(0xFFFF8FB8)   // Resonance pink
        AgentType.RIVER -> Color(0xFF55C9BE) // Recovery teal
        AgentType.ROO -> Color(0xFFFFB84D)   // Guardian amber
    }
}
