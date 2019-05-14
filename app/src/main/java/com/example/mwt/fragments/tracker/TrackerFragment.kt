package com.example.mwt.fragments.tracker

import android.content.Context.MODE_PRIVATE
import android.content.SharedPreferences
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.SeekBar
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.mwt.R
import com.example.mwt.recyclerview.ContainerRecyclerViewAdapter
import com.example.mwt.util.*
import kotlinx.android.synthetic.main.tracker_fragment.*
import kotlinx.android.synthetic.main.tracker_fragment.view.*
import org.koin.android.viewmodel.ext.android.getViewModel

class TrackerFragment : Fragment() {

    private lateinit var viewModel: TrackerViewModel
    private lateinit var containerFavoriteRecyclerViewAdapter: ContainerRecyclerViewAdapter
    private lateinit var containerRecyclerViewAdapter: ContainerRecyclerViewAdapter
    private var preference: SharedPreferences? = null


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.tracker_fragment, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        preference = context?.getSharedPreferences(SHARED_PREFERENCE_FILE, MODE_PRIVATE)
        view.recyclerFavoriteContainerView.layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
        view.containerRecyclerView.layoutManager = GridLayoutManager(context, 2, RecyclerView.HORIZONTAL, false)

        view.goal_daily.text = preference!!.getInt(SHARED_PREFERENCE_GOAL_DAILY, DEFAULT_GOAL_DAILY).toString()

        preference!!.intLiveData(SHARED_PREFERENCE_AMOUNT_DAILY, DEFAULT_AMOUNT_DAILY_WEEKLY_MONTHLY).observe(this, Observer {
            // setting up the daily wheel
            val percentage = (it.toDouble() / preference!!.getInt(SHARED_PREFERENCE_GOAL_DAILY, DEFAULT_GOAL_DAILY) * 100)
            view.drinking_progress_bar.setProgress(percentage.toInt(), true)
            view.numerator_daily.text = it.toString()
            view.percentage_daily.text = String.format("%.2f%%", percentage)

            val percentageWeek = (preference!!.getInt(SHARED_PREFERENCE_AMOUNT_WEEKLY, DEFAULT_AMOUNT_DAILY_WEEKLY_MONTHLY).toDouble() / preference!!.getInt(SHARED_PREFERENCE_GOAL_WEEKLY, DEFAULT_GOAL_WEEKLY) * 100)
            view.drinking_progress_week_bar.setProgress(percentageWeek.toInt(), true)
            view.drinking_progress_week_percentage.text = String.format("%.2f%%", percentageWeek)

            val percentageMonth = (preference!!.getInt(SHARED_PREFERENCE_AMOUNT_MONTHLY, DEFAULT_AMOUNT_DAILY_WEEKLY_MONTHLY).toDouble() / preference!!.getInt(SHARED_PREFERENCE_GOAL_MONTHLY, DEFAULT_GOAL_MONTHLY) * 100)
            view.drinking_progress_month_bar.setProgress(percentageMonth.toInt(), true)
            view.drinking_progress_month_percentage.text = String.format("%.2f%%", percentageMonth)
        })

        preference?.stringLiveData(TIME_INTERVAL_PREVIOUS_WORKER_DATE, DEFAULT_INTERVAL_PREVIOUS_WORKER_DATE)?.observe(this,
                Observer {
                    view.current_day_text.text = it
                })

        view.seekBarPercentContainer.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {

            override fun onProgressChanged(seekBar: SeekBar, i: Int, b: Boolean) {
                viewModel.progress = seekBar.progress
            }

            override fun onStartTrackingTouch(seekBar: SeekBar?) {}

            override fun onStopTrackingTouch(seekBar: SeekBar?) {}
        })

    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = getViewModel()

        containerFavoriteRecyclerViewAdapter = ContainerRecyclerViewAdapter(viewModel, preference!!, true).also(recyclerFavoriteContainerView::setAdapter)

        containerRecyclerViewAdapter = ContainerRecyclerViewAdapter(viewModel, preference!!, false).also(containerRecyclerView::setAdapter)

        viewModel.getFavoriteContainers().observe(viewLifecycleOwner, Observer {
            containerFavoriteRecyclerViewAdapter.submitList(it)
        })

        viewModel.getNonFavoriteContainers().observe(viewLifecycleOwner, Observer {
            containerRecyclerViewAdapter.submitList(it)
        })

        seekBarPercentContainer.progress = viewModel.progress
    }
}
