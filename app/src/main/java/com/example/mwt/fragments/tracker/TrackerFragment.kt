package com.example.mwt.fragments.tracker

import android.content.SharedPreferences
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.mwt.R
import kotlinx.android.synthetic.main.tracker_fragment.view.*
import android.content.Context.MODE_PRIVATE
import android.widget.SeekBar
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.mwt.recyclerview.ContainerRecyclerViewAdapter
import com.example.mwt.util.intLiveData
import com.example.mwt.util.stringLiveData
import com.example.mwt.util.*
import kotlinx.android.synthetic.main.tracker_fragment.*
import org.koin.android.viewmodel.ext.android.getViewModel

class TrackerFragment : Fragment() {

    private lateinit var viewModel: TrackerViewModel
    private lateinit var containerFavoriteRecyclerViewAdapter: ContainerRecyclerViewAdapter
    private lateinit var containerRecyclerViewAdapter: ContainerRecyclerViewAdapter
    private var preference: SharedPreferences? = null
    private var visibilityEdit: Boolean = false


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.tracker_fragment, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        preference = context?.getSharedPreferences(SHARED_PREFERENCE_FILE, MODE_PRIVATE)
        view.recyclerFavoriteContainerView.layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
        view.containerRecyclerView.layoutManager = GridLayoutManager(context, 3, RecyclerView.VERTICAL, false)
        view.goal_daily.text = String.format("%.2f", preference!!.getFloat(SHARED_PREFERENCE_GOAL_DAILY, DEFAULT_GOAL_DAILY))

        preference?.floatLiveData(SHARED_PREFERENCE_AMOUNT_DAILY, DEFAULT_AMOUNT_DAILY_WEEKLY_MONTHLY)?.observe(this, Observer{
            val percentage = (it.toFloat() / preference!!.getFloat(SHARED_PREFERENCE_GOAL_DAILY, DEFAULT_GOAL_DAILY) * 100)
            view.drinking_progress_bar.setProgress(percentage.toInt(), true)
            view.numerator_daily.text = String.format("%.2f", it)
            view.percentage_daily.text =  String.format("%.2f%%", percentage)

        })

        preference?.stringLiveData(TIME_INTERVAL_PREVIOUS_WORKER_DATE, DEFAULT_INTERVAL_PREVIOUS_WORKER_DATE)?.observe(this,
                Observer {
                    view.current_day_text.text = it
                })

        view.pull_up_tab.setOnClickListener{
            if(visibilityEdit){
                pull_up.background = ContextCompat.getDrawable(context!!, R.drawable.ic_invert_colors_black_24dp)
                view.containersRecycler.visibility = View.GONE
                view.favorite_category_title.visibility = View.GONE
            } else {
                pull_up.background = ContextCompat.getDrawable(context!!, R.drawable.ic_invert_colors_off_black_24dp)
                view.containersRecycler.visibility = View.VISIBLE
                view.favorite_category_title.visibility = View.VISIBLE
            }
            visibilityEdit = !visibilityEdit
        }

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

        viewModel.getNonFavoriteContainers().observe(viewLifecycleOwner, Observer{
            containerRecyclerViewAdapter.submitList(it)
        })

        seekBarPercentContainer.progress = viewModel.progress
    }
}
