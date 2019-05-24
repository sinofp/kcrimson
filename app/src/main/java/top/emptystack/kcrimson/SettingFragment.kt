package top.emptystack.kcrimson

import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import kotlinx.android.synthetic.main.fragment_setting.*
import org.jetbrains.anko.sdk27.coroutines.onCheckedChange
import org.jetbrains.anko.sdk27.coroutines.onTimeChanged
import org.jetbrains.anko.support.v4.defaultSharedPreferences
import org.jetbrains.anko.support.v4.toast


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

        // 用存储的数据初始化timepicker、开关和文本框
        timePicker.setIs24HourView(true)
        timePicker.hour = defaultSharedPreferences.getInt("notification_hour", 12)
        timePicker.minute = defaultSharedPreferences.getInt("notification_minute", 0)
        switch_notification.isChecked = defaultSharedPreferences.getBoolean("notification_enabled", true)
        if (!switch_notification.isChecked) {
            timePicker.visibility = View.INVISIBLE
            timePicker_prompt.visibility = View.INVISIBLE
        }

        timePicker.onTimeChanged { view, hourOfDay, minute ->
            defaultSharedPreferences
                .edit()
                .putInt("notification_hour", hourOfDay)
                .putInt("notification_minute", minute)
                .apply()
            toast("$hourOfDay:$minute")
        }

        switch_notification.onCheckedChange { buttonView, isChecked ->
            timePicker.visibility = when (isChecked) {
                true -> View.VISIBLE
                else -> View.INVISIBLE
            }
            timePicker_prompt.visibility = timePicker.visibility
            defaultSharedPreferences
                .edit()
                .putBoolean("notification_enabled", isChecked)
                .apply()
            toast(isChecked.toString())
        }
    }
}
