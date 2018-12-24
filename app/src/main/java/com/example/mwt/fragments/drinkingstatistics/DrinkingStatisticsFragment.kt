package com.example.mwt.fragments.drinkingstatistics

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import com.example.mwt.R
import kotlinx.android.synthetic.main.drinking_statistics_fragment.view.*
import org.koin.android.viewmodel.ext.android.getViewModel

class DrinkingStatisticsFragment : Fragment() {

    private lateinit var viewModel: DrinkingStatisticsViewModel

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.drinking_statistics_fragment, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        view.chart_daily
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = getViewModel()

        viewModel.getAllPosts().observe(viewLifecycleOwner, Observer {

        })
    }


}

