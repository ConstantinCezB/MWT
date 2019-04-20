package com.example.mwt.fragments.goals

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.mwt.R
import com.example.mwt.util.*
import kotlinx.android.synthetic.main.goals_fragment.view.*

class GoalsFragment: Fragment() {

    private var preference: SharedPreferences? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.goals_fragment, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        preference = context?.getSharedPreferences(SHARED_PREFERENCE_FILE, Context.MODE_PRIVATE)

        view.dayProgessAmount.text = String.format("%.2f",preference!!.getFloat(SHARED_PREFERENCE_AMOUNT_DAILY, DEFAULT_AMOUNT_DAILY_WEEKLY_MONTHLY))
        view.dayUserGoalAmount.attachEditText(preference!!, SHARED_PREFERENCE_GOAL_DAILY, DEFAULT_GOAL_DAILY)
        view.dayRecommendedGoalAmount.text = String.format("%.2f", preference!!.getFloat(SHARED_PREFERENCE_RECOMMENDED_AMOUNT, DEFAULT_GOAL_DAILY))
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
    }
}