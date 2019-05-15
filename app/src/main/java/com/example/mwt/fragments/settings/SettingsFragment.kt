package com.example.mwt.fragments.settings

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Switch
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import com.example.mwt.R
import com.example.mwt.util.*
import kotlinx.android.synthetic.main.settings_fragment.view.*
import kotlinx.android.synthetic.main.time_interval_picker_layout.view.*

class SettingsFragment : Fragment() {

    private var preference: SharedPreferences? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.settings_fragment, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        preference = context?.getSharedPreferences(SHARED_PREFERENCE_FILE, Context.MODE_PRIVATE)
        var hours = 0
        var minutes = 15

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
        view.timeIntervalReminderConstraintLayout.let { constraint ->
            preference!!.intLiveData(SHARED_PREFERENCE_TIME_INTERVAL, DEFAULT_TIME_INTERVAL).observe(this, Observer {
                hours = it / 60
                minutes = it % 60
                view.timeIntervalReminderText.text = String.format("%dH %dM", hours, minutes)
            })

            constraint.setOnClickListener {
                showDialogEdit(view, hours, minutes)
            }
        }

        view.BMIRecordNotificationSwitch.let {
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

    private fun disableMainNotification(view: View, isChecked: Boolean) {
        val switch: List<Switch> = listOf(view.drinkingReminderSwitch, view.BMIRecordNotificationSwitch, view.achievementNotificationSwitch)
        switch.forEach {
            it.isEnabled = isChecked
        }
        view.timeIntervalReminderConstraintLayout.isEnabled = isChecked
        view.timeIntervalReminderText.isEnabled = isChecked
    }

    private fun disableIntakeNotification(view: View, isChecked: Boolean) {
        view.timeIntervalReminderConstraintLayout.isEnabled = isChecked
        view.timeIntervalReminderText.isEnabled = isChecked
    }

    @SuppressLint("InflateParams")
    private fun showDialogEdit(view: View, hours: Int, minutes: Int) {

        val mBuilder: AlertDialog.Builder = AlertDialog.Builder(view.context)
        val mView: View = LayoutInflater.from(view.context).inflate(R.layout.time_interval_picker_layout, null, false)
        mBuilder.setView(mView)
        val dialog: AlertDialog = mBuilder.create()

        val displayedValuesMinutesArray = arrayOfNulls<String>(4)
        for (i in displayedValuesMinutesArray.indices)
            displayedValuesMinutesArray[i] = "${15 * i}"

        mView.numberPickerHours.let {
            it.minValue = 0
            it.maxValue = 24
            it.value = hours
            it.setOnValueChangedListener { _, _, newVal ->
                if (newVal == 24) {
                    mView.numberPickerMinutes.value = 0
                    mView.numberPickerMinutes.isEnabled = false
                } else {
                    if (newVal == 0 && mView.numberPickerMinutes.value == 0) mView.numberPickerMinutes.value = 1
                    mView.numberPickerMinutes.isEnabled = true
                }
            }
        }

        mView.numberPickerMinutes.let {
            it.minValue = 0
            it.maxValue = displayedValuesMinutesArray.size - 1
            it.displayedValues = displayedValuesMinutesArray
            it.value = minutes / 15
            it.setOnValueChangedListener { _, _, newVal ->
                if (newVal == 0 && mView.numberPickerHours.value == 0) mView.numberPickerHours.value = 1
            }
        }

        mView.intervalPickerSave.setOnClickListener {
            preference!!.setInt(SHARED_PREFERENCE_TIME_INTERVAL, 60 * mView.numberPickerHours.value + 15 * mView.numberPickerMinutes.value)
            dialog.dismiss()
        }

        mView.intervalPickerCancel.setOnClickListener {
            dialog.dismiss()
        }

        dialog.show()
    }
}