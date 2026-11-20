/*
 * ONE EYE CORE v1.0
 * NotificationHelper.kt
 *
 * DEMO IMPLEMENTATION - Android notification channel and notifications.
 * Android 13+ apps must obtain POST_NOTIFICATIONS runtime permission
 * before notifications can be shown.
 */

package com.oneeyecore.demo

import android.Manifest
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.content.pm.PackageManager
import android.os.Build
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.core.content.ContextCompat

class NotificationHelper(private val context: Context) {

    companion object {
        const val CHANNEL_ID = "one_eye_agent_updates"
        private const val CHANNEL_NAME = "ONE EYE Agent Updates"
    }

    init {
        createNotificationChannel()
    }

    private fun createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                CHANNEL_ID,
                CHANNEL_NAME,
                NotificationManager.IMPORTANCE_DEFAULT
            ).apply {
                description = "Updates and reminders from ONE EYE CORE agents"
            }

            val manager = context.getSystemService(
                NotificationManager::class.java
            )
            manager.createNotificationChannel(channel)
        }
    }

    /**
     * Returns false when notification permission is unavailable.
     * The caller should request runtime permission from an Activity.
     */
    fun showAgentNotification(
        agent: AgentType,
        message: String,
        notificationId: Int = agent.ordinal + 100
    ): Boolean {
        if (
            Build.VERSION.SDK_INT >= 33 &&
            ContextCompat.checkSelfPermission(
                context,
                Manifest.permission.POST_NOTIFICATIONS
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            return false
        }

        val notification = NotificationCompat.Builder(context, CHANNEL_ID)
            .setSmallIcon(android.R.drawable.ic_dialog_info)
            .setContentTitle("ONE EYE CORE • ${agent.displayName}")
            .setContentText(message)
            .setStyle(NotificationCompat.BigTextStyle().bigText(message))
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
            .setAutoCancel(true)
            .build()

        NotificationManagerCompat.from(context)
            .notify(notificationId, notification)

        return true
    }
}
