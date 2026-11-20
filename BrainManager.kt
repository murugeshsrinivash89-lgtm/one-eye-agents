/*
 * ONE EYE CORE v1.0
 * BrainManager.kt
 *
 * DEMO IMPLEMENTATION
 *
 * This file demonstrates the event-driven neural-agent routing concept.
 * It is a standalone Kotlin demo: MediaPipe, Room Database, Android
 * notifications, app-blocking overlays, and wake-word detection are not
 * connected in this file.
 */

package com.oneeyecore.demo

/**
 * The four specialized agents available in ONE EYE CORE.
 */
enum class AgentType(
    val displayName: String,
    val responsibility: String
) {
    ADA("ADA", "Logic and productivity"),
    ANA("ANA", "Emotional well-being"),
    RIVER("RIVER", "Balance and recovery"),
    ROO("ROO", "Core guardian")
}

/**
 * Events that can be sent to the central BrainManager.
 */
sealed class BrainEvent {
    data object AppOpened : BrainEvent()
    data class TaskCreated(val taskName: String) : BrainEvent()
    data class TaskCompleted(val taskName: String) : BrainEvent()
    data object FocusStarted : BrainEvent()
    data object FocusExited : BrainEvent()
    data class UserCheckIn(
        val mood: String,
        val stressLevel: Int,
        val fatigueLevel: Int
    ) : BrainEvent()
    data object NightTimeReminder : BrainEvent()
}

/**
 * A response produced by an agent.
 *
 * In this demo, the response is rule-based placeholder text.
 * A real implementation can pass the context to an on-device LLM.
 */
data class AgentThought(
    val agent: AgentType,
    val message: String,
    val event: BrainEvent
)

/**
 * Central event-driven logic hub for ONE EYE CORE.
 *
 * Example:
 * val brain = BrainManager()
 * val thought = brain.processEvent(BrainEvent.FocusStarted)
 */
class BrainManager {

    private var completedTaskCount: Int = 0
    private var isFocusActive: Boolean = false

    /**
     * Routes an incoming event to the appropriate agent.
     */
    fun processEvent(event: BrainEvent): AgentThought {
        val selectedAgent = selectAgent(event)
        val response = generateDemoThought(selectedAgent, event)

        return AgentThought(
            agent = selectedAgent,
            message = response,
            event = event
        )
    }

    private fun selectAgent(event: BrainEvent): AgentType {
        return when (event) {
            is BrainEvent.AppOpened -> AgentType.ROO
            is BrainEvent.TaskCreated,
            is BrainEvent.TaskCompleted -> AgentType.ADA
            is BrainEvent.FocusStarted,
            is BrainEvent.FocusExited -> AgentType.ROO
            is BrainEvent.UserCheckIn -> AgentType.ANA
            is BrainEvent.NightTimeReminder -> AgentType.RIVER
        }
    }

    private fun generateDemoThought(
        agent: AgentType,
        event: BrainEvent
    ): String {
        return when (agent) {
            AgentType.ADA -> handleAda(event)
            AgentType.ANA -> handleAna(event)
            AgentType.RIVER -> handleRiver(event)
            AgentType.ROO -> handleRoo(event)
        }
    }

    private fun handleAda(event: BrainEvent): String {
        return when (event) {
            is BrainEvent.TaskCreated ->
                "Task added to your Logic Stack: ${event.taskName}"

            is BrainEvent.TaskCompleted -> {
                completedTaskCount++
                "Task completed: ${event.taskName}. " +
                    "Demo EXP total: $completedTaskCount"
            }

            else -> "Let's organize your next task."
        }
    }

    private fun handleAna(event: BrainEvent): String {
        return when (event) {
            is BrainEvent.UserCheckIn -> {
                val stressMessage = when {
                    event.stressLevel >= 8 ->
                        "Your stress level sounds high. Take a gentle pause."
                    event.stressLevel >= 5 ->
                        "You may be carrying some stress. Check in with yourself."
                    else ->
                        "Thanks for checking in. Keep listening to yourself."
                }

                "$stressMessage Mood: ${event.mood}. " +
                    "Fatigue level: ${event.fatigueLevel}/10."
            }

            else -> "Your well-being matters. How are you feeling?"
        }
    }

    private fun handleRiver(event: BrainEvent): String {
        return when (event) {
            is BrainEvent.NightTimeReminder ->
                "Recovery protocol: slow down, unwind, and prepare for rest."

            else -> "Balance is part of progress. Remember to take a break."
        }
    }

    private fun handleRoo(event: BrainEvent): String {
        return when (event) {
            is BrainEvent.AppOpened ->
                "Oi! ROO is online. Your core interface is ready."

            is BrainEvent.FocusStarted -> {
                isFocusActive = true
                "Focus Protocol initiated. Stay on task!"
            }

            is BrainEvent.FocusExited -> {
                isFocusActive = false
                "Focus Protocol ended. Take a moment before your next task."
            }

            else -> "I'm here. Let's keep your day on track."
        }
    }

    /**
     * Exposes the current demo focus state.
     * This does not block other apps; it only tracks the demo state.
     */
    fun isFocusActive(): Boolean = isFocusActive

    /**
     * Exposes the demo completed-task count.
     * A real app should persist this through Room Database.
     */
    fun getCompletedTaskCount(): Int = completedTaskCount
}

/**
 * Simple console-style demonstration.
 *
 * Remove this function or move it into a test/demo module when integrating
 * BrainManager into the Android application.
 */
fun main() {
    val brainManager = BrainManager()

    val demoEvents = listOf(
        BrainEvent.AppOpened,
        BrainEvent.TaskCreated("Prepare project documentation"),
        BrainEvent.TaskCompleted("Prepare project documentation"),
        BrainEvent.UserCheckIn(
            mood = "Tired",
            stressLevel = 7,
            fatigueLevel = 6
        ),
        BrainEvent.FocusStarted,
        BrainEvent.NightTimeReminder,
        BrainEvent.FocusExited
    )

    demoEvents.forEach { event ->
        val thought = brainManager.processEvent(event)
        println("[${thought.agent.displayName}] ${thought.message}")
    }
}
