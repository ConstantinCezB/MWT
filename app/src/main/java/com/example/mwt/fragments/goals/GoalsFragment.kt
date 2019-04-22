package com.example.mwt.fragments.goals

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import com.example.mwt.R
import com.example.mwt.util.*
import kotlinx.android.synthetic.main.goals_fragment.view.*
import java.time.YearMonth


class GoalsFragment: Fragment() {

    private var preference: SharedPreferences? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.goals_fragment, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        preference = context?.getSharedPreferences(SHARED_PREFERENCE_FILE, Context.MODE_PRIVATE)
        val yearMonthObject = YearMonth.now()
        val daysInMonth = yearMonthObject.lengthOfMonth()
        val recommended = preference!!.getFloat(SHARED_PREFERENCE_RECOMMENDED_AMOUNT, DEFAULT_GOAL_DAILY)

        view.dayProgessAmount.text = String.format("%.2f",preference!!.getFloat(SHARED_PREFERENCE_AMOUNT_DAILY, DEFAULT_AMOUNT_DAILY_WEEKLY_MONTHLY))
        view.dayUserGoalAmount.attachEditText(preference!!, SHARED_PREFERENCE_GOAL_DAILY, DEFAULT_GOAL_DAILY, recommended)
        view.dayRecommendedGoalAmount.text = String.format("%.2f", recommended)

        view.weekProgessAmount.text = String.format("%.2f",preference!!.getFloat(SHARED_PREFERENCE_AMOUNT_WEEKLY, DEFAULT_AMOUNT_DAILY_WEEKLY_MONTHLY))
        view.weekUserGoalAmount.attachEditText(preference!!, SHARED_PREFERENCE_GOAL_WEEKLY, DEFAULT_GOAL_WEEKLY, recommended * 7)
        view.weekRecommendedGoalAmount.text = String.format("%.2f", recommended * 7)


        view.monthProgessAmount.text = String.format("%.2f",preference!!.getFloat(SHARED_PREFERENCE_AMOUNT_MONTHLY, DEFAULT_AMOUNT_DAILY_WEEKLY_MONTHLY))
        view.monthUserGoalAmount.attachEditText(preference!!, SHARED_PREFERENCE_GOAL_MONTHLY, DEFAULT_GOAL_MONTHLY, recommended * daysInMonth)
        view.monthRecommendedGoalAmount.text = String.format("%.2f", recommended * daysInMonth)

        preference?.floatLiveData(SHARED_PREFERENCE_GOAL_DAILY, DEFAULT_GOAL_DAILY)?.observe(this, Observer {
            val percentageDay = (preference!!.getFloat(SHARED_PREFERENCE_AMOUNT_DAILY, DEFAULT_AMOUNT_DAILY_WEEKLY_MONTHLY) / preference!!.getFloat(SHARED_PREFERENCE_GOAL_DAILY, DEFAULT_GOAL_DAILY) * 100)
            view.goal_drinking_progress_bar_day.setProgress(percentageDay.toInt(), true)
            view.goal_drinking_progress_day_percentage.text = String.format("%.2f%%", percentageDay)
        })

        preference?.floatLiveData(SHARED_PREFERENCE_GOAL_WEEKLY, DEFAULT_GOAL_WEEKLY)?.observe(this, Observer {
            val percentageWeek = (preference!!.getFloat(SHARED_PREFERENCE_AMOUNT_WEEKLY, DEFAULT_AMOUNT_DAILY_WEEKLY_MONTHLY) / preference!!.getFloat(SHARED_PREFERENCE_GOAL_WEEKLY, DEFAULT_GOAL_WEEKLY) * 100)
            view.goal_drinking_progress_bar_week.setProgress(percentageWeek.toInt(), true)
            view.goal_drinking_progress_week_percentage.text = String.format("%.2f%%", percentageWeek)
        })

        preference?.floatLiveData(SHARED_PREFERENCE_GOAL_MONTHLY, DEFAULT_GOAL_MONTHLY)?.observe(this, Observer {
            val percentageMonth = (preference!!.getFloat(SHARED_PREFERENCE_AMOUNT_MONTHLY, DEFAULT_AMOUNT_DAILY_WEEKLY_MONTHLY) / preference!!.getFloat(SHARED_PREFERENCE_GOAL_MONTHLY, DEFAULT_GOAL_MONTHLY) * 100)
            view.goal_drinking_progress_bar_month.setProgress(percentageMonth.toInt(), true)
            view.goal_drinking_progress_month_percentage.text = String.format("%.2f%%", percentageMonth)
        })
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
    }
}