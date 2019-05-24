package top.emptystack.kcrimson

import android.app.AlarmManager
import android.app.PendingIntent
import android.app.TimePickerDialog
import android.content.Context.ALARM_SERVICE
import android.content.Intent
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import kotlinx.android.synthetic.main.fragment_setting.*
import org.jetbrains.anko.sdk27.coroutines.onCheckedChange
import org.jetbrains.anko.support.v4.defaultSharedPreferences
import org.jetbrains.anko.support.v4.toast
import java.util.*


class SettingFragment : Fragment() {


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_setting, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // 用存储的数据初始化开关
        switch_notification.isChecked = defaultSharedPreferences.getBoolean("notification_enabled", false)

        switch_notification.onCheckedChange { buttonView, isChecked ->
            defaultSharedPreferences
                .edit()
                .putBoolean("notification_enabled", isChecked)
                .apply()

            when (isChecked) {
                true -> {
                    // 开启闹钟只能是打开选项（默认是关闭的）->选择时间后才行
                    TimePickerDialog(
                        context, TimePickerDialog.OnTimeSetListener { view, hourOfDay, minute ->
                            startAlarmAt(hourOfDay, minute)
                            defaultSharedPreferences
                                .edit()
                                .putInt("notification_hour", hourOfDay)
                                .putInt("notification_minute", minute)
                                .apply()
                        }, defaultSharedPreferences.getInt("notification_hour", 12),
                        defaultSharedPreferences.getInt("notification_minute", 0),
                        true
                    ).show()
                    toast("你打开了提醒")
                }
                else -> {
                    cancelAlarm()
                    toast("你关闭了提醒")
                }
            }
        }
    }

    fun startAlarmAt(hour: Int, minute: Int) {

        val c = Calendar.getInstance()
        c.set(Calendar.HOUR_OF_DAY, hour)
        c.set(Calendar.MINUTE, minute)
        c.set(Calendar.SECOND, 0)

        val intent = Intent(context, AlarmReceiver::class.java)
        val pendingIntent = PendingIntent.getBroadcast(context, 0, intent, 0)

        if (c.before(Calendar.getInstance())) {
            // 防止它当时就弹窗
            c.add(Calendar.DAY_OF_YEAR, 1)
        }

        val alarmManager = activity!!.getSystemService(ALARM_SERVICE) as AlarmManager
        alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, c.timeInMillis, AlarmManager.INTERVAL_DAY, pendingIntent)
    }

    fun cancelAlarm() {
        val intent = Intent(context, AlarmReceiver::class.java)
        val pendingIntent = PendingIntent.getBroadcast(context, 0, intent, 0)
        val alarmManager = activity!!.getSystemService(ALARM_SERVICE) as AlarmManager
        alarmManager.cancel(pendingIntent)
    }
}
