/*
 * ONE EYE CORE v1.0
 * AssistantScreen.kt
 *
 * DEMO IMPLEMENTATION - Main Compose screen.
 * Uses the demo BrainManager for local rule-based responses.
 * It does not yet connect the UI to MediaPipe, Room, wake-word listening,
 * app-blocking overlays, or background event services.
 */

package com.oneeyecore.demo

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

@Composable
fun AssistantScreen(
    modifier: Modifier = Modifier
) {
    val brainManager = remember { BrainManager() }

    var selectedAgent by remember { mutableStateOf(AgentType.ROO) }
    var latestMessage by remember {
        mutableStateOf("ROO is online. Your core interface is ready.")
    }
    var focusActive by remember { mutableStateOf(false) }

    val agents = listOf(
        AgentType.ADA,
        AgentType.ANA,
        AgentType.RIVER,
        AgentType.ROO
    )

    Surface(
        modifier = modifier
            .fillMaxSize()
            .background(Color(0xFF090D18)),
        color = Color(0xFF090D18)
    ) {
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 18.dp, vertical = 22.dp),
            verticalArrangement = Arrangement.spacedBy(14.dp)
        ) {
            item {
                Text(
                    text = "👁 ONE EYE CORE",
                    style = MaterialTheme.typography.headlineMedium,
                    color = Color.White
                )
                Text(
                    text = "NEURAL INTERFACE • DEMO v1.0",
                    style = MaterialTheme.typography.labelSmall,
                    color = Color(0xFF91A4C7)
                )
            }

            item {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(
                            Color(0xFF151B2B),
                            RoundedCornerShape(20.dp)
                        )
                        .padding(16.dp)
                ) {
                    Text(
                        text = "NEURAL STATUS",
                        style = MaterialTheme.typography.labelMedium,
                        color = Color(0xFFFFC857)
                    )
                    Spacer(Modifier.height(8.dp))
                    Text(
                        text = latestMessage,
                        style = MaterialTheme.typography.bodyLarge,
                        color = Color.White
                    )
                }
            }

            item {
                Text(
                    text = "SELECT YOUR AGENT",
                    style = MaterialTheme.typography.titleSmall,
                    color = Color(0xFFB9C2D5)
                )
            }

            items(agents) { agent ->
                AgentCard(
                    agent = agent,
                    isSelected = selectedAgent == agent,
                    onClick = {
                        selectedAgent = agent
                        latestMessage = when (agent) {
                            AgentType.ADA -> "ADA selected. Ready to organize your Logic Stack."
                            AgentType.ANA -> "ANA selected. Your well-being matters."
                            AgentType.RIVER -> "RIVER selected. Let's make room for recovery."
                            AgentType.ROO -> "Oi! ROO is here. Let's stay on track."
                        }
                    }
                )
            }

            item {
                Text(
                    text = "DEMO CONTROLS",
                    style = MaterialTheme.typography.titleSmall,
                    color = Color(0xFFB9C2D5)
                )
            }

            item {
                Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                    Button(
                        onClick = {
                            val thought = brainManager.processEvent(
                                BrainEvent.TaskCreated("Complete project README")
                            )
                            latestMessage = "[${thought.agent.displayName}] ${thought.message}"
                        },
                        modifier = Modifier.fillMaxWidth(),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = Color(0xFF344B83)
                        )
                    ) {
                        Text("Create Demo Task")
                    }

                    Button(
                        onClick = {
                            val event = if (focusActive) {
                                BrainEvent.FocusExited
                            } else {
                                BrainEvent.FocusStarted
                            }
                            val thought = brainManager.processEvent(event)
                            focusActive = brainManager.isFocusActive()
                            latestMessage = "[${thought.agent.displayName}] ${thought.message}"
                        },
                        modifier = Modifier.fillMaxWidth(),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = Color(0xFF79552A)
                        )
                    ) {
                        Text(if (focusActive) "End Focus Demo" else "Initiate Focus Demo")
                    }

                    Button(
                        onClick = {
                            val thought = brainManager.processEvent(
                                BrainEvent.NightTimeReminder
                            )
                            latestMessage = "[${thought.agent.displayName}] ${thought.message}"
                        },
                        modifier = Modifier.fillMaxWidth(),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = Color(0xFF276C68)
                        )
                    ) {
                        Text("Recovery Reminder Demo")
                    }
                }
            }

            item {
                Text(
                    text = "Completed demo tasks: ${brainManager.getCompletedTaskCount()}",
                    style = MaterialTheme.typography.bodySmall,
                    color = Color(0xFF91A4C7)
                )
            }
        }
    }
}
