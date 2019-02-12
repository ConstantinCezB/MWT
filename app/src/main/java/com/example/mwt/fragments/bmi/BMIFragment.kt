package com.example.mwt.fragments.bmi

import android.app.DatePickerDialog
import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.mwt.R
import com.example.mwt.util.*
import kotlinx.android.synthetic.main.bmi_fragment.view.*
import java.util.*

class BMIFragment: Fragment() {

    private var preference: SharedPreferences? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.bmi_fragment, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        preference = context?.getSharedPreferences(SHARED_PREFERENCE_FILE, Context.MODE_PRIVATE)
        val bmi = bmiCalculator()

        view.bmi_level.text = String.format("${resources.getString(R.string.your_bmi_level)}  %2.2f%%", bmi)
        view.bmi_bmiStatus.text = String.format("${resources.getString(R.string.status_fat_test)}  %s", categorySelectionBMI(bmi))
        view.bmi_progress_bar.setProgress((bmi / 40f * 100).toInt(), true)

        preference!!.stringLiveData(SHARED_PREFERENCE_DATE_OF_BIRTH, DEFAULT_DATE_OF_BIRTH).observe(this, androidx.lifecycle.Observer {
            view.setTextDateOfBirth()
        })

        view.constraintLayoutInput.setOnClickListener {
            view.constraintLayoutInputToDrop.showContent(view.bmi_input_edit_drop)
        }

        view.constraintLayoutActivity.setOnClickListener {
            view.constraintLayoutActivityToDrop.showContent(view.bmi_activity_edit_drop)
        }


        view.constraintLayoutChart.setOnClickListener {
            view.constraintLayoutChartToDrop.showContent(view.bmi_chart_edit_drop)
        }

        view.constraintLayoutLog.setOnClickListener {
            view.constraintLayoutLogToDrop.showContent(view.bmi_log_edit_drop)
        }

        view.datePickerDialogPrompt.setOnClickListener {
            showDateDialog()
        }

        view.spinnerGender.attachSinner(preference!!, getGenderSpinnerInitialPosition(), R.array.gender, SHARED_PREFERENCE_GENDER)

        view.editTextHeight.attachEditText(preference!!, SHARED_PREFERENCE_HEIGHT, DEFAULT_HEIGHT)

        view.editTextWeight.attachEditText(preference!!, SHARED_PREFERENCE_WEIGHT, DEFAULT_WEIGHT)

        view.spinnerActivityLevel.attachSinner(preference!!, getActivityLevelSpinnerInitialPosition(), R.array.activity_level, SHARED_PREFERENCE_ACTIVITY_LEVEL)

        view.spinnerSeason.attachSinner(preference!!, getSeasonSpinnerInitialPosition(), R.array.seasons, SHARED_PREFERENCE_SEASON)

    }

    private fun getGenderSpinnerInitialPosition(): Int {
        return when (preference!!.getString(SHARED_PREFERENCE_GENDER, DEFAULT_GENDER)) {
            "Female" -> 1
            else -> 0
        }
    }

    private fun getActivityLevelSpinnerInitialPosition(): Int {
        return when (preference!!.getString(SHARED_PREFERENCE_ACTIVITY_LEVEL, DEFAULT_ACTIVITY_LEVEL)) {
            "Low" -> 0
            "Medium" -> 1
            "Hard" -> 2
            else -> 3
        }
    }

    private fun getSeasonSpinnerInitialPosition(): Int {
        return when (preference!!.getString(SHARED_PREFERENCE_SEASON, DEFAULT_SEASON)) {
            "Winter" -> 0
            "Spring" -> 1
            "Summer" -> 2
            else -> 3
        }
    }

    private fun showDateDialog () {
        val calendar = Calendar.getInstance()
        val dialog = DatePickerDialog(context!!, DatePickerDialog.OnDateSetListener { _, year, month, dayOfMonth ->
            preference!!.setString(SHARED_PREFERENCE_DATE_OF_BIRTH, createDate(month + 1, dayOfMonth, year))
        }, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH))
        dialog.show()
    }

    private fun View.setTextDateOfBirth () {
        val dateString = preference!!.getString(SHARED_PREFERENCE_DATE_OF_BIRTH, DEFAULT_DATE_OF_BIRTH)
        if(dateString != "NULL"){
            this.datePickerDialogPrompt.text = dateString
        }
    }

    private fun createDate(month: Int, day: Int, year: Int) : String {
        val yearText = year.toString()
        var monthText = month.toString()
        var dayText = day.toString()

        if (month < 10) monthText = "0$monthText"
        if(day < 10) dayText = "0$dayText"

        return "$dayText/$monthText/$yearText"
    }

    private fun bmiCalculator(): Float {
        val height = preference!!.getString(SHARED_PREFERENCE_HEIGHT, DEFAULT_HEIGHT)!!.toFloat()
        val weight = preference!!.getString(SHARED_PREFERENCE_WEIGHT, DEFAULT_WEIGHT)!!.toFloat()
        return ((weight) / (height * height)) * 703
    }

    private fun categorySelectionBMI(BMI: Float): String {
        return if (BMI < 18.5) {
            "Underweight"
        } else if (BMI >= 18.5 && BMI < 25) {
            "Healthy weight"
        } else if (BMI >= 25.0 && BMI < 30.0) {
            "Overweight"
        } else if (BMI >= 30.0 && BMI < 40.0) {
            "Obese"
        } else {
            "Class 3 Obese"
        }
    }
}






