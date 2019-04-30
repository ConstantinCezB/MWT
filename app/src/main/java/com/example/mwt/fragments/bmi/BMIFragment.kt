package com.example.mwt.fragments.bmi

import android.app.DatePickerDialog
import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.mwt.R
import com.example.mwt.recyclerview.BMIHistoryRecyclerViewAdapter
import com.example.mwt.util.*
import kotlinx.android.synthetic.main.bmi_fragment.*
import kotlinx.android.synthetic.main.bmi_fragment.view.*
import org.koin.android.viewmodel.ext.android.getViewModel
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.*

class BMIFragment : Fragment() {

    private var preference: SharedPreferences? = null
    private lateinit var viewModel: BMIViewModel
    private lateinit var bmiHistoryRecyclerViewAdapter: BMIHistoryRecyclerViewAdapter

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.bmi_fragment, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        preference = context?.getSharedPreferences(SHARED_PREFERENCE_FILE, Context.MODE_PRIVATE)
        val bmi = bmiCalculator()

        view.bmi_level.text = String.format("${resources.getString(R.string.your_bmi_level)}  %2.2f", bmi)
        view.bmi_bmiStatus.text = String.format("${resources.getString(R.string.status_fat_test)}  %s", categorySelectionBMI(bmi))

        observeRecommendedChange(view)

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

        view.recyclerViewBMIHistory?.layoutManager = LinearLayoutManager(context, RecyclerView.VERTICAL, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = getViewModel()
        bmiHistoryRecyclerViewAdapter = BMIHistoryRecyclerViewAdapter().also(recyclerViewBMIHistory::setAdapter)
        viewModel.getAllPosts().observe(viewLifecycleOwner, Observer {
            bmiHistoryRecyclerViewAdapter.submitList(it)
            showNoBMILog(it.size)
        })
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

    private fun showDateDialog() {
        val calendar = Calendar.getInstance()
        val dialog = DatePickerDialog(context!!, DatePickerDialog.OnDateSetListener { _, year, month, dayOfMonth ->
            preference!!.setString(SHARED_PREFERENCE_DATE_OF_BIRTH, createDate(month + 1, dayOfMonth, year))
        }, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH))
        dialog.show()
    }

    private fun View.setTextDateOfBirth() {
        val dateString = preference!!.getString(SHARED_PREFERENCE_DATE_OF_BIRTH, DEFAULT_DATE_OF_BIRTH)
        if (dateString != "NULL") {
            this.datePickerDialogPrompt.text = dateString
        }
    }

    private fun createDate(month: Int, day: Int, year: Int): String {
        val yearText = year.toString()
        var monthText = month.toString()
        var dayText = day.toString()

        if (month < 10) monthText = "0$monthText"
        if (day < 10) dayText = "0$dayText"

        return "$yearText-$monthText-$dayText"
    }

    private fun bmiCalculator(): Float {
        val height = preference!!.getFloat(SHARED_PREFERENCE_HEIGHT, DEFAULT_HEIGHT)
        val weight = preference!!.getFloat(SHARED_PREFERENCE_WEIGHT, DEFAULT_WEIGHT)
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

    private fun observeRecommendedChange(view: View) {
        preference!!.stringLiveData(SHARED_PREFERENCE_DATE_OF_BIRTH, DEFAULT_DATE_OF_BIRTH).observe(this, androidx.lifecycle.Observer {
            calculateRecommendedIntake(preference!!, view)
            view.setTextDateOfBirth()
        })
        preference!!.stringLiveData(SHARED_PREFERENCE_GENDER, DEFAULT_GENDER).observe(this, androidx.lifecycle.Observer {
            calculateRecommendedIntake(preference!!, view)
        })
        preference!!.floatLiveData(SHARED_PREFERENCE_HEIGHT, DEFAULT_HEIGHT).observe(this, androidx.lifecycle.Observer {
            calculateRecommendedIntake(preference!!, view)
        })
        preference!!.floatLiveData(SHARED_PREFERENCE_WEIGHT, DEFAULT_WEIGHT).observe(this, androidx.lifecycle.Observer {
            calculateRecommendedIntake(preference!!, view)
        })
        preference!!.stringLiveData(SHARED_PREFERENCE_ACTIVITY_LEVEL, DEFAULT_ACTIVITY_LEVEL).observe(this, androidx.lifecycle.Observer {
            calculateRecommendedIntake(preference!!, view)
        })
        preference!!.stringLiveData(SHARED_PREFERENCE_SEASON, DEFAULT_SEASON).observe(this, androidx.lifecycle.Observer {
            calculateRecommendedIntake(preference!!, view)
        })
    }

    private fun calculateRecommendedIntake(preference: SharedPreferences, view: View) {
        val dateUserString = preference.getString(SHARED_PREFERENCE_DATE_OF_BIRTH, "1997-12-09")
        val currentDateString = preference.getString(TIME_INTERVAL_PREVIOUS_WORKER_DATE, DEFAULT_INTERVAL_PREVIOUS_WORKER_DATE)
        val dateUser = LocalDate.parse(dateUserString, DateTimeFormatter.ISO_DATE)
        val currentDateUser = LocalDate.parse(currentDateString, DateTimeFormatter.ISO_DATE)
        val weight = preference.getFloat(SHARED_PREFERENCE_WEIGHT, DEFAULT_WEIGHT)!!.toFloat()
        val activityLevelString = preference.getString(SHARED_PREFERENCE_ACTIVITY_LEVEL, DEFAULT_ACTIVITY_LEVEL)
        val seasonString = preference.getString(SHARED_PREFERENCE_SEASON, DEFAULT_SEASON)

        val seasonsArray: Array<String> = resources.getStringArray(R.array.seasons)
        val activityLevelArray: Array<String> = resources.getStringArray(R.array.activity_level)

        val yearDifference = currentDateUser.year - dateUser.year
        val age: Float = when {
            yearDifference < 30 -> 40.0f
            yearDifference in 30..55 -> 35.0f
            else -> 30.0f
        }

        val season: Float = when (seasonString) {
            seasonsArray[0] -> 0.0f
            seasonsArray[1] -> 16.907f
            seasonsArray[2] -> 33.814f
            seasonsArray[3] -> 16.907f
            else -> 0.0f
        }

        val activityLevel: Float = when (activityLevelString) {
            activityLevelArray[0] -> 16.907f
            activityLevelArray[1] -> 33.814f
            activityLevelArray[2] -> 50.72103f
            activityLevelArray[3] -> 67.62805f
            else -> 0.0f
        }

        val answer = (((weight / 2.2) * age) / 28.3) + season + activityLevel

        preference.setFloat(SHARED_PREFERENCE_RECOMMENDED_AMOUNT, answer.toFloat())
        preference.setFloat(SHARED_PREFERENCE_GOAL_DAILY, answer.toFloat())

        view.recommended_amount.text = String.format("%.2f oz", answer)
    }

    private fun showNoBMILog (adapterSize: Int){
        if (adapterSize == 0) no_display_bmi_log.visibility = View.VISIBLE
        else no_display_bmi_log.visibility = View.GONE
    }

}