package com.example.mwt.fragments.drinkingstatistics

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.mwt.R
import com.example.mwt.recyclerview.StatisticsRecyclerViewAdapter
import com.example.mwt.util.MyXAxisFormatter
import com.github.mikephil.charting.data.BarData
import com.github.mikephil.charting.data.BarDataSet
import com.github.mikephil.charting.data.BarEntry
import kotlinx.android.synthetic.main.drinking_statistics_fragment.*
import kotlinx.android.synthetic.main.drinking_statistics_fragment.view.*
import org.koin.android.viewmodel.ext.android.getViewModel

class DrinkingStatisticsFragment : Fragment() {

    private lateinit var viewModel: DrinkingStatisticsViewModel
    private lateinit var statisticsRecyclerViewAdapter: StatisticsRecyclerViewAdapter

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.drinking_statistics_fragment, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        view.recyclerStatisticsView.layoutManager = LinearLayoutManager(context, RecyclerView.VERTICAL, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = getViewModel()

        statisticsRecyclerViewAdapter = StatisticsRecyclerViewAdapter().also(recyclerStatisticsView::setAdapter)

        viewModel.getAllPosts().observe(viewLifecycleOwner, Observer {
            statisticsRecyclerViewAdapter.submitList(it)
            showNoEntriesDisplay(it.size)

            val barEntries = ArrayList<BarEntry>()
            val barDate = ArrayList<String>()
            val data = it.reversed()
            if(data.isNotEmpty()){
                for (i in data.indices) {
                    barEntries.add(BarEntry(i.toFloat(), data[i].progress.toFloat()))
                    barDate.add(data[i].date)
                }
            }else{
                barEntries.add(BarEntry(0f, 0f))
            }

            val barDataSet = BarDataSet(barEntries, "Amount drank")
            val barData = BarData(barDataSet)

            view!!.barGraphChartProgress.let { barChart ->
                barChart.data = barData
                barChart.setTouchEnabled(true)
                barChart.isDragEnabled = true
                barChart.setScaleEnabled(true)
                barChart.invalidate()
            }


        })
    }


    private fun showNoEntriesDisplay(adapterSize: Int) {
        if (adapterSize == 0) display_no_entries.visibility = View.VISIBLE
        else display_no_entries.visibility = View.GONE
    }


}

