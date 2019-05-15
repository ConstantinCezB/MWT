package com.example.mwt.fragments.goals

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.content.Context
import android.content.SharedPreferences
import android.graphics.Color
import android.os.Bundle
import android.text.InputFilter
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.mwt.R
import com.example.mwt.recyclerview.AchievmentRecyclerViewAdapter
import com.example.mwt.util.*
import kotlinx.android.synthetic.main.dialog_edit_goal.view.*
import kotlinx.android.synthetic.main.goals_fragment.*
import kotlinx.android.synthetic.main.goals_fragment.view.*
import org.koin.android.viewmodel.ext.android.getViewModel
import java.util.*


class GoalsFragment : Fragment() {

    private var preference: SharedPreferences? = null
    private lateinit var viewModel: GoalsViewModel
    private lateinit var achievementRecyclerViewAdapter: AchievmentRecyclerViewAdapter

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.goals_fragment, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        preference = context!!.getSharedPreferences(SHARED_PREFERENCE_FILE, Context.MODE_PRIVATE)!!
        val calendar = Calendar.getInstance()
        val daysInMonth = calendar.getActualMaximum(Calendar.DATE)
        val recommended = preference!!.getInt(SHARED_PREFERENCE_RECOMMENDED_AMOUNT, DEFAULT_GOAL_DAILY)
        val spinnerInitialPositionAchievements = preference!!.getString(SHARED_PREFERENCE_SPINNER_ACHIEVEMENTS, DEFAULT_SPINNER_ACHIEVEMENTS)


        val amountDay = preference!!.getInt(SHARED_PREFERENCE_AMOUNT_DAILY, DEFAULT_AMOUNT_DAILY_WEEKLY_MONTHLY)
        val amountWeek = preference!!.getInt(SHARED_PREFERENCE_AMOUNT_WEEKLY, DEFAULT_AMOUNT_DAILY_WEEKLY_MONTHLY)
        val amountMonth = preference!!.getInt(SHARED_PREFERENCE_AMOUNT_MONTHLY, DEFAULT_AMOUNT_DAILY_WEEKLY_MONTHLY)


        view.dayProgressAmount.text = amountDay.toString()
        preference?.intLiveData(SHARED_PREFERENCE_GOAL_DAILY, DEFAULT_GOAL_DAILY)?.observe(this, Observer {
            val percentageDay = amountDay.toDouble() / it * 100
            view.goal_drinking_progress_bar_day.setProgress(percentageDay.toInt(), true)
            view.goal_drinking_progress_day_percentage.text = String.format("%.2f%%", percentageDay)
            view.goal_day_user.text = it.toString()
        })
        view.goal_edit_day_btn.setOnClickListener {
            showDialogEdit(view, "day", SHARED_PREFERENCE_GOAL_DAILY, DEFAULT_GOAL_DAILY, SHARED_PREFERENCE_ALLOW_USER_DAY_GOAL ,recommended, 4)
        }

        view.weekProgessAmount.text = amountWeek.toString()
        preference?.intLiveData(SHARED_PREFERENCE_GOAL_WEEKLY, DEFAULT_GOAL_WEEKLY)?.observe(this, Observer {
            val percentageWeek = amountWeek.toDouble() / it * 100
            view.goal_drinking_progress_bar_week.setProgress(percentageWeek.toInt(), true)
            view.goal_drinking_progress_week_percentage.text = String.format("%.2f%%", percentageWeek)
            view.goal_week_user.text = it.toString()
        })
        view.goal_edit_week_btn.setOnClickListener {
            showDialogEdit(view, "week", SHARED_PREFERENCE_GOAL_WEEKLY, DEFAULT_GOAL_WEEKLY, SHARED_PREFERENCE_ALLOW_USER_WEEK_GOAL,recommended * 7, 5)
        }

        view.monthProgessAmount.text = amountMonth.toString()
        preference?.intLiveData(SHARED_PREFERENCE_GOAL_MONTHLY, DEFAULT_GOAL_MONTHLY)?.observe(this, Observer {
            val percentageMonth = amountMonth.toDouble() / it * 100
            view.goal_drinking_progress_bar_month.setProgress(percentageMonth.toInt(), true)
            view.goal_drinking_progress_month_percentage.text = String.format("%.2f%%", percentageMonth)
            view.goal_month_user.text = it.toString()
        })
        view.goal_edit_month_btn.setOnClickListener {
            showDialogEdit(view, "month", SHARED_PREFERENCE_GOAL_MONTHLY, DEFAULT_GOAL_MONTHLY, SHARED_PREFERENCE_ALLOW_USER_MONTH_GOAL,recommended * daysInMonth, 6)
        }


        view.recyclerViewAchivementGoal.layoutManager = LinearLayoutManager(context, RecyclerView.VERTICAL, false)

        view.ConstraintLayoutToDropAchivementGoal.setOnClickListener {
            view.ConstraintLayoutAchievementToDropGoal.showContent(view.bmi_log_edit_drop_achievement_goal)
        }

        view.spinnerTypeSelectAchievement.attachSinner(preference!!, stringToIntConversionSpinner(spinnerInitialPositionAchievements!!), R.array.achievementType, SHARED_PREFERENCE_SPINNER_ACHIEVEMENTS, Color.WHITE)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = getViewModel()

        preference?.stringLiveData(SHARED_PREFERENCE_SPINNER_ACHIEVEMENTS, DEFAULT_SPINNER_ACHIEVEMENTS)?.observe(this, Observer {
            val spinnerInitialPositionAchievements = preference!!.getString(SHARED_PREFERENCE_SPINNER_ACHIEVEMENTS, DEFAULT_SPINNER_ACHIEVEMENTS)
            achievementRecyclerViewAdapter = AchievmentRecyclerViewAdapter().also(recyclerViewAchivementGoal::setAdapter)

            viewModel.getAllPosts(spinnerInitialPositionAchievements!!).observe(viewLifecycleOwner, Observer {
                achievementRecyclerViewAdapter.submitList(it)
                showNoAchievement(it.size)
            })
        })
    }

    private fun showNoAchievement(adapterSize: Int) {
        if (adapterSize == 0) no_display_achievement.visibility = View.VISIBLE
        else no_display_achievement.visibility = View.GONE
    }

    private fun stringToIntConversionSpinner(string: String): Int {
        return when (string) {
            "Day" -> 0
            "Week" -> 1
            "Month" -> 2
            else -> 0
        }
    }

    @SuppressLint("InflateParams")
    private fun showDialogEdit(view: View, title: String,
                               preferenceValue: String, preferenceDefaultValue: Int,
                               preferenceValueAllowUserGoal: String,
                               recommendedAmountGoal: Int, maxLength: Int) {

        val mBuilder: AlertDialog.Builder = AlertDialog.Builder(view.context)
        val mView: View = LayoutInflater.from(view.context).inflate(R.layout.dialog_edit_goal, null, false)
        mBuilder.setView(mView)
        val dialog: AlertDialog = mBuilder.create()

        val amountGoal = preference!!.getInt(preferenceValue, preferenceDefaultValue)

        mView.edit_text_goal_title.text = "Edit ${title.toLowerCase()} goal"

        mView.recomended_amount_text.text = recommendedAmountGoal.toString()

        mView.switch_allow_user_defined.let {
            it.isChecked = preference!!.getBoolean(preferenceValueAllowUserGoal, DEFAULT_USER_GOAL)
            if (!it.isChecked){
                mView.editText_goal_user.isEnabled = false
            }


            it.setOnCheckedChangeListener { _, isChecked ->
                if (!isChecked) {
                    mView.editText_goal_user.text.clear()
                    mView.editText_goal_user.text.append(recommendedAmountGoal.toString())
                }
                mView.editText_goal_user.isEnabled = isChecked
            }
        }

        mView.editText_goal_user.let {
            it.text.append(amountGoal.toString())
            val filterArray = arrayOfNulls<InputFilter>(1)
            filterArray[0] = InputFilter.LengthFilter(maxLength)
            it.filters = filterArray
        }

        mView.edit_goal_accept_btn.setOnClickListener {
            preference!!.setInt(preferenceValue, mView.editText_goal_user.text.toString().toInt())
            preference!!.setBoolean(preferenceValueAllowUserGoal, mView.switch_allow_user_defined.isChecked)
            dialog.dismiss()
        }

        mView.edit_goal_cancel_btn.setOnClickListener {
            dialog.dismiss()
        }



        dialog.show()
    }
}