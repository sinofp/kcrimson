package top.emptystack.kcrimson

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent

class AlarmReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context?, intent: Intent?) {
        val events = getStoredEvents(context)
        if (events.isNotEmpty()) {
            val mustDoName = events.first().name
            val mustDoDDL = events.first().ddl
            val notificationHelper = NotificationHelper(context!!)
            notificationHelper.notify(
                1100,
                notificationHelper.getNotification("请尽快完成$mustDoName！", "${mustDoName}只剩下${mustDoDDL}天了")
            )
        }
    }
}