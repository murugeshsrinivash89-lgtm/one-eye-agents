/*
 * ONE EYE CORE v1.0
 * AgentCard.kt
 *
 * DEMO IMPLEMENTATION - Reusable Jetpack Compose agent card.
 */

package com.oneeyecore.demo

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

@Composable
fun AgentCard(
    agent: AgentType,
    isSelected: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val accent = when (agent) {
        AgentType.ADA -> Color(0xFF6C8CFF)
        AgentType.ANA -> Color(0xFFFF8FB8)
        AgentType.RIVER -> Color(0xFF55C9BE)
        AgentType.ROO -> Color(0xFFFFB84D)
    }

    val description = when (agent) {
        AgentType.ADA -> "Logic Unit • Productivity"
        AgentType.ANA -> "Resonance Unit • Well-being"
        AgentType.RIVER -> "Recovery Unit • Balance"
        AgentType.ROO -> "Core Guardian • Focus"
    }

    Row(
        modifier = modifier
            .fillMaxWidth()
            .background(
                color = Color(0xFF151B2B).copy(alpha = 0.94f),
                shape = RoundedCornerShape(20.dp)
            )
            .border(
                width = if (isSelected) 2.dp else 1.dp,
                color = if (isSelected) accent else Color.White.copy(alpha = 0.12f),
                shape = RoundedCornerShape(20.dp)
            )
            .clickable(onClick = onClick)
            .padding(14.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(14.dp)
    ) {
        RooCharacter(
            agent = agent,
            emotion = if (isSelected) {
                CharacterEmotion.EXCITED
            } else {
                CharacterEmotion.NEUTRAL
            },
            characterSize = 64.dp
        )

        Column(
            modifier = Modifier.weight(1f),
            verticalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            Text(
                text = agent.displayName,
                style = MaterialTheme.typography.titleMedium,
                color = Color.White
            )
            Text(
                text = description,
                style = MaterialTheme.typography.bodySmall,
                color = Color(0xFFB9C2D5)
            )
        }

        if (isSelected) {
            Text(
                text = "ACTIVE",
                style = MaterialTheme.typography.labelSmall,
                color = accent
            )
        }
    }
}
