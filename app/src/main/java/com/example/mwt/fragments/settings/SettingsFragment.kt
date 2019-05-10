package com.example.mwt.fragments.settings

import android.app.AlertDialog
import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Switch
import androidx.fragment.app.Fragment
import com.example.mwt.R
import com.example.mwt.util.*
import kotlinx.android.synthetic.main.settings_fragment.view.*

class SettingsFragment : Fragment() {

    private var preference: SharedPreferences? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.settings_fragment, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        preference = context?.getSharedPreferences(SHARED_PREFERENCE_FILE, Context.MODE_PRIVATE)

        view.notificationSwitch.let {
            it.isChecked = preference!!.getBoolean(SHARED_PREFERENCE_NOTIFICATION, DEFAULT_NOTIFICATION)
            it.setOnCheckedChangeListener { _, isChecked ->
                preference!!.setBoolean(SHARED_PREFERENCE_NOTIFICATION, isChecked)
                disableMainNotification(view, isChecked)
            }
            disableMainNotification(view, it.isChecked)
        }

        view.drinkingReminderSwitch.let {
            it.isChecked = preference!!.getBoolean(SHARED_PREFERENCE_DRINKING_REMINDER, DEFAULT_DRINKING_REMINDER)
            it.setOnCheckedChangeListener { _, isChecked ->
                preference!!.setBoolean(SHARED_PREFERENCE_DRINKING_REMINDER, isChecked)
                disableIntakeNotification(view, isChecked)
            }
            disableIntakeNotification(view, it.isChecked)
        }
        view.timeIntervalReminderSpinner.setOnClickListener {
            showDialogEdit(view)
        }
        view.smartNotificationSwitch.let {
            it.isChecked = preference!!.getBoolean(SHARED_PREFERENCE_SMART_NOTIFICATION, DEFAULT_SMART_NOTIFICATION)
            it.setOnCheckedChangeListener { _, isChecked ->
                preference!!.setBoolean(SHARED_PREFERENCE_SMART_NOTIFICATION, isChecked)
            }
        }

        view.BMIRecordNotidicationSwitch.let {
            it.isChecked = preference!!.getBoolean(SHARED_PREFERENCE_BMI_RECORD_NOTIFICATION, DEFAULT_BMI_RECORD_NOTIFICATION)
            it.setOnCheckedChangeListener { _, isChecked ->
                preference!!.setBoolean(SHARED_PREFERENCE_BMI_RECORD_NOTIFICATION, isChecked)
            }
        }

        view.achievementNotificationSwitch.let {
            it.isChecked = preference!!.getBoolean(SHARED_PREFERENCE_ACHIEVEMENT_NOTIFICATION, DEFAULT_ACHIEVEMENT_NOTIFICATION)
            it.setOnCheckedChangeListener { _, isChecked ->
                preference!!.setBoolean(SHARED_PREFERENCE_ACHIEVEMENT_NOTIFICATION, isChecked)
            }
        }

        view.button_select_color_primary_dark.setOnClickListener {

        }

        view.button_select_color_primary.setOnClickListener {

        }

        view.button_select_color_accent.setOnClickListener {

        }
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
    }

    private fun disableMainNotification(view: View, isChecked: Boolean) {
        val switch: List<Switch> = listOf(view.drinkingReminderSwitch, view.BMIRecordNotidicationSwitch, view.achievementNotificationSwitch)
        switch.forEach {
            if (!isChecked) it.isChecked = isChecked
            it.isClickable = isChecked
            it.isEnabled = isChecked
        }
    }

    private fun disableIntakeNotification(view: View, isChecked: Boolean) {
        view.smartNotificationSwitch.let {
            if (!isChecked) it.isChecked = isChecked
            it.isClickable = isChecked
            it.isEnabled = isChecked
        }
        view.timeIntervalReminderSpinner.isEnabled = isChecked
    }

    private fun showDialogEdit(view: View) {

        val mBuilder: AlertDialog.Builder = AlertDialog.Builder(view.context)
        val mView: View = LayoutInflater.from(view.context).inflate(R.layout.time_interval_picker_layout, null, false)
        mBuilder.setView(mView)
        val dialog: AlertDialog = mBuilder.create()



        dialog.show()
    }

}