package com.example.mwt.fragments.drinkingstatistics

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import com.example.mwt.R
import com.github.mikephil.charting.data.Entry
import kotlinx.android.synthetic.main.drinking_statistics_fragment.view.*
import org.koin.android.viewmodel.ext.android.getViewModel
import com.example.mwt.db.dateprogressdb.DateProgressEntity
import com.github.mikephil.charting.charts.BarChart
import com.github.mikephil.charting.data.LineData
import com.github.mikephil.charting.data.LineDataSet


class DrinkingStatisticsFragment : Fragment() {

    private lateinit var viewModel: DrinkingStatisticsViewModel

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.drinking_statistics_fragment, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = getViewModel()

        viewModel.getAllPosts().observe(viewLifecycleOwner, Observer {
            entryCreator(it)
            val dataSet = LineData(LineDataSet(entryCreator(it), "Daily"))
            view?.chart_daily?.data = dataSet
        })
    }

    private fun entryCreator(dataObjects : List<DateProgressEntity>): java.util.ArrayList<Entry>{
        val entries: java.util.ArrayList<Entry> = ArrayList()

        for (data in dataObjects) {
           // turn your data into Entry objects
           entries.add(Entry(1.0f, data.progress.toFloat())) //TODO: find a way to store the x-axis
        }

        return entries
    }


}

