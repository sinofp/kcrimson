package top.emptystack.kcrimson

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.ContextWrapper
import android.content.Intent
import android.graphics.Color

/**
 * Helper class to manage notification channels, and create notifications.
 */
internal class NotificationHelper
/**
 * Registers notification channels, which can be used later by individual notifications.
 * @param ctx The application context
 */
    (ctx: Context) : ContextWrapper(ctx) {
    private val manager: NotificationManager by lazy {
        getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
    }

    init {

        val channel = NotificationChannel(PRIMARY_CHANNEL,
            "提醒你优先做什么事", NotificationManager.IMPORTANCE_DEFAULT).apply {
            description = "提醒最快结束的死线还有几天，该去做那件事了！"
        }
        channel.lightColor = Color.GREEN
        channel.lockscreenVisibility = Notification.VISIBILITY_PRIVATE
        manager.createNotificationChannel(channel)

    }

    /**
     * Get a notification of type 1
     * Provide the builder rather than the notification it's self as useful for making notification
     * changes.
     * @param title the title of the notification
     * *
     * @param body the body text for the notification
     * *
     * @return the builder as it keeps a reference to the notification (since API 24)
     */
    fun getNotification(title: String, body: String): Notification.Builder {

        val intent = Intent(this, MainActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        }
        val pendingIntent: PendingIntent = PendingIntent.getActivity(this, 0, intent, 0)

        return Notification.Builder(applicationContext, PRIMARY_CHANNEL)
            .setContentTitle(title)
            .setContentText(body)
            .setSmallIcon(smallIcon)
            .setContentIntent(pendingIntent)
            .setAutoCancel(true)
    }



    /**
     * Send a notification.
     * @param id The ID of the notification
     * *
     * @param notification The notification object
     */
    fun notify(id: Int, notification: Notification.Builder) {
        manager.notify(id, notification.build())
    }

    /**
     * Get the small icon for this app
     * @return The small icon resource id
     */
    private val smallIcon: Int
        get() = android.R.drawable.stat_notify_chat


    companion object {
        val PRIMARY_CHANNEL = "default"
    }
}